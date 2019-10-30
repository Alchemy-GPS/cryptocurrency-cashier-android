package com.achpay.wallet.mvp.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.WelcomeActivity;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.CoinTypeRequest;
import com.achpay.wallet.model.CoinTypeResponse;
import com.achpay.wallet.model.LoginRequest;
import com.achpay.wallet.model.LoginResponse;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.main.MainActivity;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.service.MessageService;
import com.achpay.wallet.service.ProtectService;
import com.achpay.wallet.utils.AES;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.NativeMethods;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static com.achpay.wallet.model.params.User.QFPAY_SYS_ID;

public class LoginWebviewActivity extends Activity {

    private WebView mLoginWebview;
    private ProgressBar mProgressBar;
    private String AUTH_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_webview);

        mLoginWebview = findViewById(R.id.login_webview);
        mProgressBar = findViewById(R.id.login_web_progressbar);

        AUTH_URL = Constants.OAUTH_URL
                + "?client_id=" + NativeMethods.getClientId()
                + "&redirect_uri=" + Constants.OAUTH_REDIRECT
                + "&scope=user_baseinfo"
                + "&response_type=code"
                + "&state="+Constants.OAUTH_STATE;

        //AUTH_URL = "https://openapi-test.qfpay.com/oauth/v2/authorize?client_id=2DAB13A0AF4D4031820149BCD58188D0&redirect_uri=http://47.104.90.114:8080/boss-template/getcode&scope=user_baseinfo&response_type=code";

        try {
            AUTH_URL = URLDecoder.decode(AUTH_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mLoginWebview.addJavascriptInterface(new JavaScriptInterface(), "ANDROID");

        WebSettings mWebSettings = mLoginWebview.getSettings();

        mWebSettings.setJavaScriptEnabled(true);

        mWebSettings.setSavePassword(true);

        mWebSettings.setSaveFormData(true);

        mWebSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mLoginWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.i("WEBVIEW_URL == " + url);
                webView.loadUrl(url);
                return Build.VERSION.SDK_INT < 26;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.i("onReceivedSslError");
                handler.proceed();
//                super.onReceivedSslError(view, handler, error);
            }
        });

        mLoginWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);//设置进度值
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                }
            }
        });

        mLoginWebview.loadUrl(AUTH_URL);
    }

    @Override
    protected void onDestroy() {
        mLoginWebview.destroy();
        super.onDestroy();
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
                        startMainActivity();
                    }
                } else if (response.getReturnCode().equals(ResponseParam.SESSION_TIMEOUT)) {
                    CommonUtil.clearLoginInfo(LoginWebviewActivity.this);
                    Intent activity = new Intent(LoginWebviewActivity.this, LoginWebviewActivity.class);
                    startActivity(activity);
                    LoginWebviewActivity.this.finish();
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

    private void startMainActivity() {
        Intent intent = new Intent(LoginWebviewActivity.this, MainActivity.class);

        String uriString = "achpay://achpay.org/params?userId=" + CommonUtil.getMerchantId(this) + "&userName=" + CommonUtil.getMerchantName(this);

        intent.setData(Uri.parse(uriString));

        startActivity(intent);

        LoginWebviewActivity.this.finish();
    }

    private void loginError() {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(LoginWebviewActivity.this);
        CookieManager.getInstance().removeAllCookie();
        cookieSyncManager.sync();
        Toast.makeText(LoginWebviewActivity.this, getString(R.string.login_login_fail), Toast.LENGTH_SHORT).show();
        mLoginWebview.loadUrl(AUTH_URL);
    }

    /**
     * js和android交互的类
     */
    private class JavaScriptInterface {
        /**
         * @JavascriptInterface 这个注解在4.2及以后要加在方法上
         * 　在js里面点击一个按钮，android这边将js传过来的内容传递给要跳转到的界面
         */
        @JavascriptInterface
        public void login(String userName, String userId) {

            Log.i("userName == " + userName + "===== userId == " + userId);

            SharedPreferenceUtil
                    .getPref(LoginWebviewActivity.this)
                    .putStringValue(User.SYS_ID, QFPAY_SYS_ID);

//            String password = CommonUtil.Base64(CommonUtil.MD5(userId + NativeMethods.getPWDSalt()));

//            String password = "4e7a45324e444a6c4e6a686d4d445930595759334f5449314e5752694f4451794e7a5a694f44597a4d7a633d";
//            LoginRequest request = new LoginRequest("13552125443", password, "APP");

            String password = "4d6a59315a575978593245784d4749334f445a68597a4a684e7a497859545a6d4e32466d4e544d335a44633d";
//            LoginRequest request = new LoginRequest("1", password, "APP");
            LoginRequest request = new LoginRequest();

            RetrofitUtil.getInstance().userLogin(request, new BaseSubscriber<UniteResponse<LoginResponse>>() {

                @Override
                public void onResponse(UniteResponse<LoginResponse> response) {

                    Log.i(GsonUtil.objectToJson(response));

                    if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                        LoginResponse loginResponse = response.getData();
                        SharedPreferenceUtil
                                .getPref(LoginWebviewActivity.this)
                                .putStringValue(User.MERCHANT_ID, AES.encrypt(loginResponse.getMerchantId()));

                        SharedPreferenceUtil
                                .getPref(LoginWebviewActivity.this)
                                .putStringValue(User.MERCHANT_NAME, loginResponse.getMerchantName());

                        SharedPreferenceUtil
                                .getPref(LoginWebviewActivity.this)
                                .putStringValue(User.SESSION_ID, AES.encrypt(loginResponse.getSessionId()));

                        if (!AppUtil.isServiceRunning(LoginWebviewActivity.this, "com.achpay.wallet.service.MessageService")) {
                            Intent intent = new Intent(LoginWebviewActivity.this, MessageService.class);
                            LoginWebviewActivity.this.startService(intent);
                        }

                        if (!AppUtil.isServiceRunning(LoginWebviewActivity.this, "com.achpay.wallet.service.ProtectService")) {
                            Intent intent = new Intent(LoginWebviewActivity.this, ProtectService.class);
                            LoginWebviewActivity.this.startService(intent);
                        }

                        WSClientMessage message = new WSClientMessage();
                        message.setMessage(TransParams.CHECK_WEBSOCKET);
                        EventBusUtil.post(message);

                        List<Cryptocurrency> mCryptocurrencyList = CryptocurrencyManger.getInstance().queryAllCryptocurrency();

                        if (mCryptocurrencyList == null || mCryptocurrencyList.size() == 0) {
                            initCryptocurrency();
                        } else {
                            startMainActivity();
                        }
                    }else {
                        //登录失败
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
    }
}
