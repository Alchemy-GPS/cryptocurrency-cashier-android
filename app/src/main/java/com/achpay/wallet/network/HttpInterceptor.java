package com.achpay.wallet.network;

import com.achpay.wallet.utils.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpInterceptor implements Interceptor {
    private final static String TAG = "REQUEST_URL == ";

    @Override
    public Response intercept(Chain chain) throws IOException {

        String url = chain.request().url().toString();

        Log.i(TAG + url);

        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .build();

        /*
        Response response = chain.proceed(request);

        String s = response.body().string();

        Log.i("原始报文 == "+ s);
        */
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        Log.i("原始报文 == " + responseBody.string());
        return response;
        //return chain.proceed(request);
    }
}