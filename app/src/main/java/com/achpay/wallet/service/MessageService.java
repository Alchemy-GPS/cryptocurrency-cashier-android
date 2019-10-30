package com.achpay.wallet.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.database.dbmodel.RecentOrder;
import com.achpay.wallet.database.dbmodel.RecentOrderDaoManager;
import com.achpay.wallet.model.OrderStatusMessage;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.event.SessionTimeoutEvent;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.login.LoginActivity;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.network.websocket.EchoWebSocketListener;
import com.achpay.wallet.network.websocket.WebsocketProxy;
import com.achpay.wallet.network.websocket.WsStatus;
import com.achpay.wallet.receiver.GlobalMessageBroadcastReceiver;
import com.achpay.wallet.utils.AES;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.view.GlobalMessageDialogActivity;
import com.android.process.aidl.IProcessService;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MessageService extends Service implements WebsocketProxy {

    private final String TAG = "MessageService == ";

    private GlobalMessageBroadcastReceiver messageBroadcastReceiver;
    private WebSocket mWebSocket;
    private WebSocketFactory factory;
    private EchoWebSocketListener mWebSocketListener;

    private Runnable mReconnectTask = new Runnable() {
        @Override
        public void run() {
            recreateWebsocket();
        }
    };

    private static final String CHANNEL_ID_STRING = "ACHPAY_MESSAGE_SERVICE";

    @Override
    public void onCreate() {
        super.onCreate();

        mLocalBinder = new LocalBinder();

        if (mMessageServiceConn == null) {
            mMessageServiceConn = new MessageServiceConnection();
        }

        //适配8.0 service
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationChannel mChannel = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            mChannel = new NotificationChannel(CHANNEL_ID_STRING, getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
//            notificationManager.createNotificationChannel(mChannel);
//            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
//            startForeground(1, notification);
//        }

        EventBusUtil.register(this);
        this.messageBroadcastReceiver = new GlobalMessageBroadcastReceiver();
        this.messageBroadcastReceiver.register(this);
        mWebSocketListener = new EchoWebSocketListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG + "onStartCommand");

        bindService(new Intent(this, ProtectService.class), mMessageServiceConn, Context.BIND_IMPORTANT);

        if (mWebSocket == null) {
            createWebsocket();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG + "onDestroy");

        unbindService(mMessageServiceConn);

        if (mWebSocket != null) {
            mWebSocket.disconnect(3000, "BackgroundService Closed");
        }
        if (this.messageBroadcastReceiver != null) {
            this.messageBroadcastReceiver.unRegister(this);
        }
        EventBusUtil.unregister(this);
    }

    private void createWebsocket() {

        String session_id = CommonUtil.getSessionId(this);
        String merchant_id = CommonUtil.getMerchantId(this);

        Log.i(TAG + "session_id == " + session_id);

        if (factory == null) {
            factory = new WebSocketFactory();
        }

        if (mWebSocketListener == null) {
            mWebSocketListener = new EchoWebSocketListener(this);
        }

        try {
            mWebSocket = factory.createSocket(Constants.WS_HOST, 5000);

            mWebSocket.addHeader("sessionid", session_id).addHeader("merchantid", merchant_id);

            mWebSocket.addListener(mWebSocketListener);

//            mWebSocket.setPingInterval(3000);

            mWebSocket.setFrameQueueSize(5);//设置帧队列最大值为5

            mWebSocket.setMissingCloseFrameAllowed(false);//设置不允许服务端关闭连接却未发送关闭帧

            mWebSocket.connectAsynchronously();

            Log.i(TAG + "WebSocket created");
        } catch (IOException e) {
            e.printStackTrace();
            mWebSocket = null;
        }
    }

    private Handler mHandler = new Handler();
    private WsStatus mStatus;
    private int reconnectCount = 0;//重连次数
    private long minInterval = 3000;//重连最小时间间隔
    private long maxInterval = 600000;//重连最大时间间隔

    public void reconnect() {
        /*if (!isNetConnect()) {
            reconnectCount = 0;
            Log.i("重连失败网络不可用");
            return;
        }*/
        if (mWebSocket == null || (!mWebSocket.isOpen() && getStatus() != WsStatus.CONNECTING)) {//当前连接断开了且不是正在重连状态

            reconnectCount++;
            setStatus(WsStatus.CONNECTING);

            long reconnectTime = minInterval;
            if (reconnectCount > 3) {
                long temp = minInterval * (reconnectCount - 2);
                reconnectTime = temp > maxInterval ? maxInterval : temp;
            }

            Log.i("准备开始第" + reconnectCount + "次重连,重连间隔 : " + reconnectTime + " -- url:" + Constants.WS_HOST);
            mHandler.postDelayed(mReconnectTask, reconnectTime);
        }
    }

    private void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(mReconnectTask);
    }

    private void setStatus(WsStatus status) {
        this.mStatus = status;
    }

    private WsStatus getStatus() {
        return mStatus;
    }

    private void recreateWebsocket() {
        if (mWebSocket == null) {
            createWebsocket();
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        Log.i(TAG + "onConnected");
        setStatus(WsStatus.CONNECT_SUCCESS);
        cancelReconnect();
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) {

        Log.i(TAG + " : onMessage---" + text);

        UniteResponse<OrderStatusMessage> response = GsonUtil.jsonToResponse(text, OrderStatusMessage.class);

        if (response != null && response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {

            OrderStatusMessage message = response.getData();

            if (message != null) {

                if (AES.encrypt(GsonUtil.objectToJson(message)).equals(SharedPreferenceUtil.getPref(this).getStringValue(User.LAST_ORDER_DETAIL))) {
                    //收到的消息和上一条相同则终止
                    return;
                }

                SharedPreferenceUtil.getPref(this).putStringValue(User.LAST_ORDER_DETAIL, AES.encrypt(GsonUtil.objectToJson(message)));

                String mRecentOrderDetail = AES.encrypt(GsonUtil.objectToJson(message.getOrderId()
                        + message.getMerchantId()
                        + message.getTerminalId()
                        + message.getResult()
                        + message.getConfirmBlockNums()));

                RecentOrder mRecentOrder = RecentOrderDaoManager.getInstance().queryRecentOrderById(message.getOrderId());

                if (mRecentOrder == null) {
                    //1.表中没有该数据,则插入数据(默认没有被读过)
                    RecentOrderDaoManager.getInstance().insertRecentOrder(new RecentOrder(message.getOrderId(), message.getReceiveTime(), message.getResult(), mRecentOrderDetail, false, false, false));
                    //少收的情况
                    if (message.getResult().equals(ResponseParam.LSUCCESS)) {
                        showDialog(message.getOrderId());
                    }
                } else {
                    //2.表中有该数据
                    //如果跟上一条一样,则不做任何操作
                    if (mRecentOrderDetail.equals(mRecentOrder.getOrderDetail())) {
                        return;
                    } else {
                        mRecentOrder.setIsRead(false);
                        mRecentOrder.setOrderDetail(mRecentOrderDetail);
                        RecentOrderDaoManager.getInstance().updateRecentOrder(mRecentOrder);
                    }

                    if (!mRecentOrder.getIsDialogShown() && message.getResult().equals(ResponseParam.LSUCCESS)) {
                        Log.i(TAG + "RecentOrder is not read");
                        showDialog(message.getOrderId());
                    }
                }


                //震动
                //AppUtil.vibrator(this, 300);
                EventBusUtil.post(message);
            }
        }
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) {
        exception.printStackTrace();
        Log.i(TAG + " : onConnectError---" + exception.getError().name());
        mWebSocket = null;

        setStatus(WsStatus.CONNECT_FAIL);
        reconnect();//连接错误的时候调用重连方法
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
        int clientCloseCode = clientCloseFrame.getCloseCode();
        Log.i(TAG + "onDisconnected :: ClientCloseCode == " + clientCloseCode);

        mWebSocket = null;

        setStatus(WsStatus.CONNECT_FAIL);

        //1.用户退出 2.session过期 这两种情况不自动重连
        if (clientCloseCode != 3001 && clientCloseCode != 3002) {
            Log.i(TAG + "Reconnect");
            reconnect();
        }
    }

    /**
     * 接收来自客户端内部的消息,并在收到后做相应的处理
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onClientMessage(WSClientMessage message) {
        if (message != null) {
            if (message.getMessage().equals(TransParams.CHECK_WEBSOCKET)) {
                if (mWebSocket == null) {
                    createWebsocket();
                } else {
                    if (!mWebSocket.isOpen()) {
                        reconnect();
                    }
                }
            } else if (message.getMessage().equals(TransParams.CLOSE_WEBSOCKET)) {
                if (mWebSocket != null) {
                    mWebSocket.disconnect(3001, "User logout");
                }
            }
        }
    }

    /**
     * session过期,跳到登录界面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onSessionTimeout(SessionTimeoutEvent event) {
        if (event != null && event.getCode().equals(ResponseParam.SESSION_TIMEOUT)) {
            Log.i(TAG + "session expired");

            CommonUtil.clearLoginInfo(this);

            if (mWebSocket != null) {
                mWebSocket.disconnect(3002, "session expired");
            }

            Intent activity = new Intent(this, LoginActivity.class);
            activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(activity);
        }
    }

    private void showDialog(String orderId) {
        if (AppUtil.checkAppProcessState(this, ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
            if (!AppUtil.isCurrentActivityState(this, AppUtil.getName(GlobalMessageDialogActivity.class))) {
                //强制更新窗口是否弹出
                if (!SharedPreferenceUtil.getPref(this).getBoolean(TransParams.UPDATE_DIALOG_SHOWING, false)) {
                    GlobalMessageBroadcastReceiver.sendLSuccess(this, orderId);
                }
            }
        }
    }

    private void showDialog(Serializable message) {
        if (AppUtil.checkAppProcessState(this, ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
            if (!AppUtil.isCurrentActivityState(this, AppUtil.getName(GlobalMessageDialogActivity.class))) {
                GlobalMessageBroadcastReceiver.sendBroadcast(this, message);
            }
        }
    }


    private LocalBinder mLocalBinder;

    private MessageServiceConnection mMessageServiceConn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    /**
     * 通过AIDL实现进程间通信
     */
    class LocalBinder extends IProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "MessageService";
        }
    }

    /**
     * 连接远程服务
     */
    class MessageServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // 与远程服务通信
                IProcessService process = IProcessService.Stub.asInterface(service);
                Log.i("连接" + process.getServiceName() + "服务成功");
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e("连接 ProtectService 失败");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // RemoteException连接过程出现的异常，才会回调,unbind不会回调
            // 监测，远程服务已经死掉，则重启远程服务
            Log.i("ProtectService 远程服务挂掉了,远程服务被杀死");

            // 启动远程服务
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //startForegroundService(new Intent(MessageService.this, ProtectService.class));
            } else {
                startService(new Intent(MessageService.this, ProtectService.class));
            }

            // 绑定远程服务
            bindService(new Intent(MessageService.this, ProtectService.class), mMessageServiceConn, Context.BIND_IMPORTANT);
        }
    }
}
