package com.achpay.wallet.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.achpay.wallet.R;
import com.achpay.wallet.utils.Log;
import com.android.process.aidl.IProcessService;

public class ProtectService extends Service {

    String TAG = "ProtectService";

    private ServiceBinder mServiceBinder;

    private ProtectServiceConnection mProtectServiceConn;

    private static final String CHANNEL_ID_STRING = "ACHPAY_PROTECT_SERVICE";


    @Override
    public void onCreate() {
        super.onCreate();

        mServiceBinder = new ServiceBinder();

        if (mProtectServiceConn == null) {
            mProtectServiceConn = new ProtectServiceConnection();
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

        Log.i(TAG + " onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG + " onStartCommand");

        //  绑定远程服务
        bindService(new Intent(this, MessageService.class), mProtectServiceConn, Context.BIND_IMPORTANT);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mProtectServiceConn);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    /**
     * 通过AIDL实现进程间通信
     */
    class ServiceBinder extends IProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "ProtectService";
        }
    }

    /**
     * 连接远程服务
     */
    class ProtectServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // 与远程服务通信
                IProcessService process = IProcessService.Stub.asInterface(service);
                Log.i("连接" + process.getServiceName() + "服务成功");
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e("连接 MessageService 失败");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // RemoteException连接过程出现的异常，才会回调,unbind不会回调
            // 监测，远程服务已经死掉，则重启远程服务
            Log.i("MessageService 远程服务挂掉了,远程服务被杀死");

            // 启动远程服务
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //startForegroundService(new Intent(ProtectService.this, MessageService.class));
            } else {
                startService(new Intent(ProtectService.this, MessageService.class));
            }

            // 绑定远程服务
            bindService(new Intent(ProtectService.this, MessageService.class), mProtectServiceConn, Context.BIND_IMPORTANT);
        }
    }
}
