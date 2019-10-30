package com.achpay.wallet.network.converter;

import android.support.annotation.NonNull;

import com.achpay.wallet.utils.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(@NonNull ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}
