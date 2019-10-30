package com.achpay.wallet.mvp.main;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.achpay.wallet.base.presenter.BasePresenter;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.CoinTypeRequest;
import com.achpay.wallet.model.CoinTypeResponse;
import com.achpay.wallet.model.LoginRequest;
import com.achpay.wallet.model.LoginResponse;
import com.achpay.wallet.model.SessionAuthResponse;
import com.achpay.wallet.model.UpdateAppRequest;
import com.achpay.wallet.model.UpdateAppResponse;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.cointype.CoinTypeActivity;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.observer.DownloadChangeObserver;
import com.achpay.wallet.service.MessageService;
import com.achpay.wallet.service.ProtectService;
import com.achpay.wallet.utils.AES;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import java.io.File;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.achpay.wallet.model.params.User.QFPAY_SYS_ID;

public class MainPresenter extends BasePresenter<MainView> {

    private DownloadChangeObserver observer;
    private Activity mContext;
    private String amount;

    public MainPresenter(MainView view) {
        super(view);
    }

    public void checkService(Context mContext) {
        if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.MessageService")) {
            Intent intent = new Intent(mContext, MessageService.class);
            mContext.startService(intent);
        }

        if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.ProtectService")) {
            Intent intent = new Intent(mContext, ProtectService.class);
            mContext.startService(intent);
        }

        WSClientMessage message = new WSClientMessage();
        message.setMessage(TransParams.CHECK_WEBSOCKET);
        EventBusUtil.post(message);
    }

    public void checkLoginInfo(Activity mContext) {
        this.mContext = mContext;
        Intent intent = mContext.getIntent();
        Uri uri = intent.getData();
        if (uri != null) {//由第三方APP跳转过来携带的信息
            String source = uri.getQueryParameter("source");
            String session = uri.getQueryParameter("session");
            amount = uri.getQueryParameter("amount");
            String currency = uri.getQueryParameter("currency");

            String userId = uri.getQueryParameter("userId");
            String userName = uri.getQueryParameter("userName");
            String orderMessage = uri.getQueryParameter("orderInfo");

            if (!TextUtils.isEmpty(source) && !TextUtils.isEmpty(session)) {
                if (source.equals(User.QFPAY_SOURCE)) {//钱方用户
                    sessionAuth(session);
                }
            }
        } else {
            if (MvpRef != null) {
                MvpRef.get().loginSuccess();
            }
        }
    }

    private void selectCrypto() {
        if (MvpRef != null) {
            MvpRef.get().dismiss();
        }
        Intent intent = new Intent(mContext, CoinTypeActivity.class);
        intent.putExtra(TransParams.AMOUNT, amount);
        mContext.startActivity(intent);
        mContext.finish();
    }

    private void merchantAuth(String merchantId, String merchantName, String sessionId) {

        Log.i("MERCHANT_ID_LOCAL", CommonUtil.getMerchantId(mContext));
        Log.i("MERCHANT_ID", merchantId);
        Log.i("MERCHANT_NAME_LOCAL", CommonUtil.getMerchantName(mContext));
        Log.i("MERCHANT_NAME", merchantName);

        if (TextUtils.isEmpty(CommonUtil.getMerchantName(mContext)) || TextUtils.isEmpty(CommonUtil.getMerchantId(mContext))) {
            //为空则重新登录后再加载页面(首次登录)
            CryptocurrencyManger.getInstance().deleteAll();
            achLogin(merchantId, merchantName, sessionId, QFPAY_SYS_ID);
        } else if (CommonUtil.getQfpayMerchantId().equals(merchantId) && CommonUtil.getMerchantName(mContext).equals(merchantName) && !TextUtils.isEmpty(CommonUtil.getSessionId(mContext))) {
            //不为空且用户名相同,直接加载页面
            selectCrypto();
        } else {
            //不为空,但是userId不同 则用新的用户名登录后再加载页面
            CryptocurrencyManger.getInstance().deleteAll();
            achLogin(merchantId, merchantName, sessionId, QFPAY_SYS_ID);
        }
    }

    private void sessionAuth(final String session) {
        RetrofitUtil.getInstance().sessionAuth(session, new BaseSubscriber<SessionAuthResponse>() {
            @Override
            public void onResponse(SessionAuthResponse response) {

                Log.i(GsonUtil.objectToJson(response));

                if (response.getRespcd().equals(ResponseParam.SUCCESS_CODE) && response.getUserInfo() != null) {
                    String merchantId = response.getUserInfo().getUserid();
                    String merchantName = response.getUserInfo().getShopname();
                    merchantAuth(merchantId, merchantName, session);
                } else {
                    loginError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loginError();
            }
        });
    }

    public void achLogin(String merchantId, String merchantName, String sessionId, String sysId) {
        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.SYS_ID, sysId);

        LoginRequest request = new LoginRequest(merchantId, merchantName, sessionId, sysId);

        RetrofitUtil.getInstance().userLogin(request, new BaseSubscriber<UniteResponse<LoginResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {
                MvpRef.get().showLoading();
            }

            @Override
            public void onResponse(UniteResponse<LoginResponse> response) {
                Log.i("LOGINRESPONSE == " + GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    LoginResponse loginResponse = response.getData();

                    String currencyId = String.valueOf(loginResponse.getSettleCurrencyId());

                    CommonUtil.setCurrencyUnitById(mContext, currencyId);

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.MERCHANT_ID, AES.encrypt(loginResponse.getMerchantId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.MERCHANT_NAME, loginResponse.getMerchantName());

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SESSION_ID, AES.encrypt(loginResponse.getSessionId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.QFPAY_MERCHANT_ID, AES.encrypt(loginResponse.getQianfId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CURRENCY_ID, String.valueOf(loginResponse.getSettleCurrencyId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CRYPTOCURRENCY_ID, String.valueOf(loginResponse.getSettleCryptocurrencyId()));

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CURRENCY, loginResponse.getSettltCurrency());

                    SharedPreferenceUtil
                            .getPref(mContext)
                            .putStringValue(User.SETTING_CRYPTOCURRENCY, loginResponse.getSettleCryptocurrency());

                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.MessageService")) {
                        Intent intent = new Intent(mContext, MessageService.class);
                        mContext.startService(intent);
                    }

                    if (!AppUtil.isServiceRunning(mContext, "com.achpay.wallet.service.ProtectService")) {
                        Intent intent = new Intent(mContext, ProtectService.class);
                        mContext.startService(intent);
                    }

                    WSClientMessage message = new WSClientMessage();
                    message.setMessage(TransParams.CHECK_WEBSOCKET);
                    EventBusUtil.post(message);

                    List<Cryptocurrency> mCryptocurrencyList = CryptocurrencyManger.getInstance().queryAllCryptocurrency();

                    if (mCryptocurrencyList == null || mCryptocurrencyList.size() == 0) {
                        initCryptocurrency();
                    } else {
                        selectCrypto();
                    }
                } else {
                    loginError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loginError();
            }
        });
    }

    private void initCryptocurrency() {
        CoinTypeRequest request = new CoinTypeRequest(1);

        RetrofitUtil.getInstance().getCoinType(request, new BaseSubscriber<UniteResponse<CoinTypeResponse>>() {
            @Override
            public void onResponse(UniteResponse<CoinTypeResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    List<Cryptocurrency> mList = response.getData().getSupportCryptosList();
                    if (mList != null && mList.size() != 0) {
                        CryptocurrencyManger.getInstance().insertCryptocurrencys(mList);
                        selectCrypto();
                    }
                } else {
                    loginError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loginError();
            }
        });
    }

    private void loginError() {
        if (MvpRef != null) {
            MvpRef.get().loginFail();
        }
    }

    public void checkAppUpdate(final Context mContext) {

        UpdateAppRequest request = new UpdateAppRequest(AppUtil.getAppVersionName(mContext), AppUtil.getPackageName(mContext));

        RetrofitUtil.getInstance().checkAppUpdate(request, new BaseSubscriber<UpdateAppResponse>() {

            @Override
            public void onResponse(UpdateAppResponse updateAppResponse) {
                Log.i("UpdateAppResponse== " + GsonUtil.objectToJson(updateAppResponse));
                if (updateAppResponse != null && updateAppResponse.getReturnMsg().equals(ResponseParam.SUCCESS)) {

                    if (updateAppResponse.getNewVersion().equals(ResponseParam.TRUE)) {
                        SharedPreferenceUtil.getPref(mContext).putStringValue(User.APK_MD5_VALUE, updateAppResponse.getApkMd5());

                        long downloadId = SharedPreferenceUtil.getPref(mContext).getLong(User.DOWNLOAD_ID, 0);

                        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                        observer = new DownloadChangeObserver(null, mContext);

                        observer.setDownloadId(downloadId);

                        mContext.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, observer);

                        switch (observer.queryDownloadStatus()) {

                            case DownloadManager.STATUS_FAILED://下载失败
                                Log.i("下载失败");
                                downloadManager.remove(downloadId);
                                if (MvpRef != null) {
                                    MvpRef.get().showUpdate(updateAppResponse);
                                }
                                break;
                            case DownloadManager.STATUS_PAUSED://下载暂停
                                Log.i("下载暂停");
                                //删除原来下载的apk
                                downloadManager.remove(downloadId);
                                //下载应用 退出App
                                MvpRef.get().showUpdate(updateAppResponse);
                                break;
                            case DownloadManager.STATUS_PENDING://正在请求链接地址
                                Log.i("正在请求");
                                MvpRef.get().showDownloadProgress();
                                break;
                            case DownloadManager.STATUS_RUNNING://正在下载中
                                Log.i("正在下载");
                                MvpRef.get().showDownloadProgress();
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL://下载成功
                                Log.i("下载成功");
                                break;
                            default:
                                Log.i("未下载");
                                if (MvpRef != null) {
                                    MvpRef.get().showUpdate(updateAppResponse);
                                }
                                break;
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 使用DownloadManager下载新版本的apk
     *
     * @param url apk的下载链接
     */
    public void download(String url, Context mContext) {
        File apk = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ACHPAY.apk");
        if (apk.exists()) {
            boolean deleted = apk.delete();
        }

        Log.i("downloadURL === " + url);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("ACHPAY.apk");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setVisibleInDownloadsUi(true);
        request.setAllowedOverRoaming(false);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ACHPAY.apk");

        DownloadManager mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        if (mDownloadManager != null) {
            long downloadId = mDownloadManager.enqueue(request);
            if (observer != null) {
                observer.setDownloadId(downloadId);
            }
            SharedPreferenceUtil.getPref(mContext).putLongValue("DOWNLOAD_ID", downloadId);
        }
    }
}
