package com.achpay.wallet;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;

import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.CoinTypeRequest;
import com.achpay.wallet.model.CoinTypeResponse;
import com.achpay.wallet.model.UserInfoRequest;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.login.LoginActivity;
import com.achpay.wallet.mvp.main.MainActivity;
import com.achpay.wallet.network.ApiService;
import com.achpay.wallet.network.BaseSubscriber;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.network.UniteRequest;
import com.achpay.wallet.network.UniteResponse;
import com.achpay.wallet.network.converter.StringConverterFactory;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.widget.ClassicHeader;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class WelcomeActivity extends Activity {
    private long startTime;

    private long delayTime = 800;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        startTime = System.currentTimeMillis();

        changeAppLanguage();

        resetRefreshParameter();

        initData();
//        test();
    }

    private void test() {
        UserInfoRequest request = new UserInfoRequest();
        request.setSign("ACHPAY");

        OkHttpClient client = RetrofitUtil.getClient();

        Retrofit mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.APP_HOST)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService mApiService = mRetrofit.create(ApiService.class);

        mApiService.test(modelToRequestBody(request))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private RequestBody modelToRequestBody(Object object) {
        UniteRequest mRequest = new UniteRequest();

        mRequest.setData(object);
        mRequest.setToken("token");
        String json = GsonUtil.objectToJson(mRequest);

        Log.i("REQUEST_JSON == " + json);

        return RequestBody.create(MediaType.parse("application/json"), json);
    }

    public void changeAppLanguage() {

        int selectedLanguage = SharedPreferenceUtil
                .getPref(this)
                .getInt(User.SETTING_LANGUAGE_SELECTION, -1);

        if (selectedLanguage == 0) {//简体中文
            locale = Locale.CHINA;
        } else if (selectedLanguage == 1) {
            locale = Locale.TAIWAN;
        } else if (selectedLanguage == 2) {
            locale = Locale.ENGLISH;
        } else if (selectedLanguage == 3) {
            locale = Locale.JAPAN;
        } else {
            locale = getResources().getConfiguration().locale;

            String sysLanguage = locale.getLanguage();

            //缺省设置判断  如果不是以上几种语言的话,则默认设置为英语
            if (!sysLanguage.equals(Locale.CHINA.getLanguage()) &&
                    !sysLanguage.equals(Locale.CHINESE.getLanguage()) &&
                    !sysLanguage.equals(Locale.ENGLISH.getLanguage()) &&
                    !sysLanguage.equals(Locale.JAPAN.getLanguage()) &&
                    !sysLanguage.equals(Locale.JAPANESE.getLanguage()) &&
                    !sysLanguage.equals(Locale.TRADITIONAL_CHINESE.getLanguage()) &&
                    !sysLanguage.equals(Locale.TAIWAN.getLanguage()) ) {

                locale = Locale.ENGLISH;
            }
        }

        Locale.setDefault(locale);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    private void initData() {
        CoinTypeRequest request = new CoinTypeRequest(1);

        RetrofitUtil.getInstance().getCoinType(request, new Observer<UniteResponse<CoinTypeResponse>>() {
            @Override
            public void onNext(UniteResponse<CoinTypeResponse> response) {
                Log.i(GsonUtil.objectToJson(response));
                if (response.getReturnCode().equals(ResponseParam.SUCCESS_CODE)) {
                    List<Cryptocurrency> mList = response.getData().getSupportCryptosList();
                    if (mList != null && mList.size() != 0) {
                        CryptocurrencyManger.getInstance().insertCryptocurrencys(mList);
                    }
                    delayStartMainActivity();
                } else if (response.getReturnCode().equals(ResponseParam.SESSION_TIMEOUT)) {
                    CommonUtil.clearLoginInfo(WelcomeActivity.this);

                    long endTime = System.currentTimeMillis();

                    if (endTime - startTime < delayTime) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent activity = new Intent(WelcomeActivity.this, LoginActivity.class);
                                startActivity(activity);
                                WelcomeActivity.this.finish();
                            }
                        }, delayTime - (endTime - startTime));
                    } else {
                        Intent activity = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(activity);
                        WelcomeActivity.this.finish();
                    }
                } else {
                    delayStartMainActivity();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                delayStartMainActivity();
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void delayStartMainActivity() {
        long endTime = System.currentTimeMillis();
        if (endTime - startTime < delayTime) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, delayTime - (endTime - startTime));
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        String merchantId = CommonUtil.getMerchantId(this);
        String merchantName = CommonUtil.getMerchantName(this);
        String sessionId = CommonUtil.getSessionId(this);

        Intent intent = new Intent();
        //为空则为未登录
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(merchantId)) {
//            boolean isOAuth = SharedPreferenceUtil.getPref(this).getBoolean(User.LOGIN_ACCESS_OAUTH, true);
            intent.setClass(this, LoginActivity.class);
        } else {
            intent.setClass(this, MainActivity.class);
            intent.putExtra(User.MERCHANT_ID, merchantId);
            intent.putExtra(User.MERCHANT_NAME, merchantName);
        }
        startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private void resetRefreshParameter() {
        ClassicHeader.REFRESH_HEADER_PULLDOWN = getString(R.string.referesh_header_pulldown);//"下拉可以刷新";
        ClassicHeader.REFRESH_HEADER_REFRESHING = getString(R.string.refresh_header_refreshing);//"正在刷新...";
        ClassicHeader.REFRESH_HEADER_LOADING = getString(R.string.refresh_header_loading);//"正在加载...";
        ClassicHeader.REFRESH_HEADER_RELEASE = getString(R.string.refresh_header_release);//"释放立即刷新";
        ClassicHeader.REFRESH_HEADER_FINISH = getString(R.string.refresh_header_finish);//"刷新完成";
        ClassicHeader.REFRESH_HEADER_FAILED = getString(R.string.refresh_header_failed);//"刷新失败";
        ClassicHeader.REFRESH_HEADER_LASTTIME = getString(R.string.refresh_header_lasttime);//"上次更新 M-d HH:mm";
        ClassicHeader.REFRESH_HEADER_SECOND_FLOOR = getString(R.string.refresh_header_second_floor);//"释放进入二楼"

        ClassicsFooter.REFRESH_FOOTER_PULLUP = getString(R.string.refresh_footer_pullup);//"上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = getString(R.string.refresh_footer_release);//"释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_LOADING = getString(R.string.refresh_footer_loading);//"正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = getString(R.string.refresh_footer_refreshing);//"正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = getString(R.string.refresh_footer_finish);//"加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = getString(R.string.refresh_footer_failed);//"加载失败";
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = getString(R.string.refresh_footer_allloaded);//"全部加载完成";
    }

    private void setBitmapByScale() {
        ImageView im = findViewById(R.id.imageview);

        Display display = getWindowManager().getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        int screenWidth = point.x;
        int screenHeight = point.y;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int desH = screenHeight * width / screenWidth;

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, (height - desH), width, desH);

        im.setImageBitmap(newBitmap);
    }
}
