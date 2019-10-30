package com.achpay.wallet.network;

import com.achpay.wallet.model.CoinToCurrencyResponse;
import com.achpay.wallet.model.CoinTypeResponse;
import com.achpay.wallet.model.CurrencyToCoinResponse;
import com.achpay.wallet.model.LightningUriResponse;
import com.achpay.wallet.model.LoginQfpayResponse;
import com.achpay.wallet.model.LoginResponse;
import com.achpay.wallet.model.OrderCloseResponse;
import com.achpay.wallet.model.OrderCreateResponse;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.RaidenBalanceResponse;
import com.achpay.wallet.model.RaidenPayResponse;
import com.achpay.wallet.model.RegisterResponse;
import com.achpay.wallet.model.SessionAuthResponse;
import com.achpay.wallet.model.StatementResponse;
import com.achpay.wallet.model.SysCurrencyResponse;
import com.achpay.wallet.model.TestModel;
import com.achpay.wallet.model.UpdateAppResponse;
import com.achpay.wallet.model.UserInfoResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/09  17:04
 */

public interface ApiService {

    @POST("merchantInfo/account")
    Observable<String> test(@Body RequestBody requestBody);

    @POST("merchantInfo/login")
    Observable<UniteResponse<LoginResponse>> userLogin(@Body RequestBody requestBody);

    @POST("merchantInfo/create")
    Observable<UniteResponse<RegisterResponse>> register(@Body RequestBody requestBody);

    @POST("merchantInfo/logout")
    Observable<UniteResponse<TestModel>> userLogout(@Body RequestBody requestBody);

    @POST("appVersion/get")
    Observable<UpdateAppResponse> checkAppUpdate(@Body RequestBody requestBody);

    @POST("merchantInfo/merchantCryptocurrencs")
    Observable<UniteResponse<CoinTypeResponse>> getCoinType(@Body RequestBody requestBody);

    @POST("exchange/rate")
    Observable<UniteResponse<CurrencyToCoinResponse>> transCurrencyToCoin(@Body RequestBody requestBody);

    @POST("exchange/rate")
    Observable<UniteResponse<CoinToCurrencyResponse>> transCoinToCurrency(@Body RequestBody requestBody);

    @POST("trade/create")
    Observable<UniteResponse<OrderCreateResponse>> createOrder(@Body RequestBody requestBody);

    @POST("trade/close")
    Observable<UniteResponse<OrderCloseResponse>> closeOrder(@Body RequestBody requestBody);

    @POST("merchantInfo/account")
    Observable<UniteResponse<UserInfoResponse>> getUserInfo(@Body RequestBody requestBody);

    @POST("merchantInfo/accountQuery")
    Observable<UniteResponse<StatementResponse>> getStatementHistory(@Body RequestBody requestBody);

    @POST("trade/query")
    Observable<UniteResponse<OrderResponse>> getOrderHistory(@Body RequestBody requestBody);

    @GET("utils/getsyscurrency")
    Observable<UniteResponse<List<SysCurrencyResponse>>> getSysCurrency();

    @GET()
    Observable<LightningUriResponse> getLightningUri(@Url String url);

    @GET()
    Observable<RaidenBalanceResponse> getTunnelBalance(@Url String url);

    @GET()
    Observable<RaidenPayResponse> raidenPay(@Url String url);

    @GET()
    Observable<SessionAuthResponse> sessionAuth(@Url String url, @Query("sessionid") String session_id);

    @FormUrlEncoded
    @POST()
    Observable<LoginQfpayResponse> qfpayLogin(@Url String url, @Field("username") String username, @Field("password") String password);

    /*@GET("api/cook/list")
    Observable<TngouResponse<List<Cook>>> getCookList(@Query("page") int page, @Query("rows") int rows);*/
}
