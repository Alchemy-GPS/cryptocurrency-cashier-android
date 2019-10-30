package com.achpay.wallet.network;


import com.achpay.wallet.ACHApplication;
import com.achpay.wallet.Constants;
import com.achpay.wallet.model.CoinToCurrencyRequest;
import com.achpay.wallet.model.CoinToCurrencyResponse;
import com.achpay.wallet.model.CoinTypeRequest;
import com.achpay.wallet.model.CoinTypeResponse;
import com.achpay.wallet.model.CurrencyToCoinRequest;
import com.achpay.wallet.model.CurrencyToCoinResponse;
import com.achpay.wallet.model.LightningUriResponse;
import com.achpay.wallet.model.LoginQfpayResponse;
import com.achpay.wallet.model.LoginRequest;
import com.achpay.wallet.model.LoginResponse;
import com.achpay.wallet.model.OrderCreateRequest;
import com.achpay.wallet.model.OrderCreateResponse;
import com.achpay.wallet.model.OrderDetailRequest;
import com.achpay.wallet.model.OrderHistoryIntervalRequest;
import com.achpay.wallet.model.OrderHistoryTypeRequest;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.RaidenBalanceResponse;
import com.achpay.wallet.model.RaidenPayResponse;
import com.achpay.wallet.model.RegisterRequest;
import com.achpay.wallet.model.RegisterResponse;
import com.achpay.wallet.model.SessionAuthResponse;
import com.achpay.wallet.model.StatementRequest;
import com.achpay.wallet.model.StatementResponse;
import com.achpay.wallet.model.SysCurrencyResponse;
import com.achpay.wallet.model.UpdateAppRequest;
import com.achpay.wallet.model.UpdateAppResponse;
import com.achpay.wallet.model.UserInfoRequest;
import com.achpay.wallet.model.UserInfoResponse;
import com.achpay.wallet.network.converter.StringConverterFactory;
import com.achpay.wallet.network.cookie.ClearableCookieJar;
import com.achpay.wallet.network.cookie.PersistentCookieJar;
import com.achpay.wallet.network.cookie.cache.SetCookieCache;
import com.achpay.wallet.network.cookie.persistence.SharedPrefsCookiePersistor;
import com.achpay.wallet.utils.GsonUtil;
import com.achpay.wallet.utils.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo.util
 * @date 2016/12/12  10:38
 */

public class RetrofitUtil {

    private static final long DEFAULT_TIMEOUT = 30L;
    private static RetrofitUtil mInstance;
    private Retrofit mRetrofit;
    private ApiService mApiService;
    private static ClearableCookieJar cookieJar;
    private UniteRequest mRequest;

    /**
     * 私有构造方法
     */
    private RetrofitUtil() {
        OkHttpClient client = getClient();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiService = mRetrofit.create(ApiService.class);
        mRequest = new UniteRequest();
    }

    public static OkHttpClient getClient() {

        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ACHApplication.APPLICATION));

        return new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
    }

    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

    public static void recreate() {
        mInstance = null;
        synchronized (RetrofitUtil.class) {
            mInstance = new RetrofitUtil();
        }
    }

    private RequestBody modelToBody(Object object) {
        String json = GsonUtil.objectToJson(object);

        Log.i("REQUEST_JSON == " + json);

        return RequestBody.create(MediaType.parse("application/json"), json);
    }

    private RequestBody modelToRequestBody(Object object) {
        if (mRequest == null) {
            mRequest = new UniteRequest();
        }
        mRequest.setData(object);
        mRequest.setToken("token");
        String json = GsonUtil.objectToJson(mRequest);

        Log.i("REQUEST_JSON == " + json);

        return RequestBody.create(MediaType.parse("application/json"), json);
    }

    /**
     * 新的登录接口
     *
     * @param subscriber
     */
    public void userLogin(LoginRequest loginRequest, Observer<UniteResponse<LoginResponse>> subscriber) {
        mApiService.userLogin(modelToRequestBody(loginRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 注册接口
     *
     * @param subscriber
     */
    public void register(RegisterRequest request, Observer<UniteResponse<RegisterResponse>> subscriber) {
        mApiService.register(modelToRequestBody(request))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 钱方登录接口
     *
     * @param subscriber
     */
    public void qfpayLogin(String username,String password, Observer<LoginQfpayResponse> subscriber) {
        mApiService.qfpayLogin(Constants.QFPAY_LOGIN_URL, username, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 钱方session验证接口
     *
     * @param subscriber
     */
    public void sessionAuth(String session_id,Observer<SessionAuthResponse> subscriber) {
        mApiService.sessionAuth(Constants.SESSION_AUTH, session_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 查询更新接口
     *
     * @param subscriber
     */
    public void checkAppUpdate(UpdateAppRequest request, Observer<UpdateAppResponse> subscriber) {
        mApiService.checkAppUpdate(modelToBody(request))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取用户信息接口
     *
     * @param subscriber
     */
    public void getUserInfo(UserInfoRequest userInfoRequest, Observer<UniteResponse<UserInfoResponse>> subscriber) {
        mApiService.getUserInfo(modelToRequestBody(userInfoRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取商户币种接口
     *
     * @param subscriber
     */
    public void getCoinType(CoinTypeRequest coinTypeRequest, Observer<UniteResponse<CoinTypeResponse>> subscriber) {
        mApiService.getCoinType(modelToRequestBody(coinTypeRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 法币转数字货币接口
     *
     * @param subscriber
     */
    public void transCurrencyToCoin(CurrencyToCoinRequest currencyToCoinRequest, Observer<UniteResponse<CurrencyToCoinResponse>> subscriber) {

        mApiService.transCurrencyToCoin(modelToRequestBody(currencyToCoinRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 法币转数字货币接口
     *
     * @param subscriber
     */
    public void transCoinToCurrency(CoinToCurrencyRequest coinToCurrencyRequest, Observer<UniteResponse<CoinToCurrencyResponse>> subscriber) {

        mApiService.transCoinToCurrency(modelToRequestBody(coinToCurrencyRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 创建订单接口
     *
     * @param subscriber
     */
    public void createOrder(OrderCreateRequest request, Observer<UniteResponse<OrderCreateResponse>> subscriber) {

        mApiService.createOrder(modelToRequestBody(request))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 订单历史按币种查询接口
     *
     * @param subscriber
     */
    public void getOrderHistoryOfType(OrderHistoryTypeRequest orderHistoryTypeRequest, Observer<UniteResponse<OrderResponse>> subscriber) {
        mApiService.getOrderHistory(modelToRequestBody(orderHistoryTypeRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 订单历史按时间查询接口
     *
     * @param subscriber
     */
    public void getOrderHistoryOfInterval(OrderHistoryIntervalRequest orderHistoryIntervalRequest, Observer<UniteResponse<OrderResponse>> subscriber) {
        mApiService.getOrderHistory(modelToRequestBody(orderHistoryIntervalRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 订单详情查询接口
     *
     * @param subscriber
     */
    public void getOrderDetail(OrderDetailRequest orderDetailRequest, Observer<UniteResponse<OrderResponse>> subscriber) {
        mApiService.getOrderHistory(modelToRequestBody(orderDetailRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 账单历史按查询接口
     *
     * @param subscriber
     */
    public void getStatementHistory(StatementRequest statementRequest, Observer<UniteResponse<StatementResponse>> subscriber) {
        mApiService.getStatementHistory(modelToRequestBody(statementRequest))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取系统支持的法币接口
     *
     * @param subscriber
     */
    public void getSysCurrency(Observer<UniteResponse<List<SysCurrencyResponse>>> subscriber) {
        mApiService.getSysCurrency()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雷电网络查询通道余额接口
     *
     * @param subscriber
     */
    public void getLightningUri(String url,Observer<LightningUriResponse> subscriber) {
        mApiService.getLightningUri(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雷电网络查询通道余额接口
     *
     * @param subscriber
     */
    public void getTunnelBalance(String url,Observer<RaidenBalanceResponse> subscriber) {
        mApiService.getTunnelBalance(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 雷电网络支付接口
     *
     * @param subscriber
     */
    public void raidenPay(String url,Observer<RaidenPayResponse> subscriber) {
        mApiService.raidenPay(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
