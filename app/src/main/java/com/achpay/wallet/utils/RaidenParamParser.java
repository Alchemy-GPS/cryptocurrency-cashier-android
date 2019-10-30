package com.achpay.wallet.utils;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RaidenParamParser {

    public static Parameter parseString(String result) {
        Parameter parameter = new Parameter();

        if (TextUtils.isEmpty(result)) {
            return null;
        }

        String bitcoinPaymentURIWithoutScheme = result.replaceFirst( ":", "");

        ArrayList<String> bitcoinPaymentURIElements = new ArrayList<>(Arrays.asList(bitcoinPaymentURIWithoutScheme.split("\\?")));

        if (bitcoinPaymentURIElements.size() <= 1) {
            return null;
        }

        List<String> queryParametersList = Arrays.asList(bitcoinPaymentURIElements.get(1).split("&"));

        HashMap<String, String> queryParametersMap = new HashMap<String, String>();

        for (String query : queryParametersList) {
            int firstEqual = query.indexOf("=");

            String key = query.substring(0, firstEqual);
            String value = query.replace(key + "=", "");

            queryParametersMap.put(key, value);
        }
        parameter.setQueryParametersMap(queryParametersMap);

        return parameter;
    }


    public static class Parameter implements Serializable {
        private String token;
        private String proxy;
        private String amount;
        private String sysFlowNo;
        private HashMap<String, String> queryParametersMap;

        public HashMap<String, String> getQueryParametersMap() {
            return queryParametersMap;
        }

        public void setQueryParametersMap(HashMap<String, String> queryParametersMap) {
            this.queryParametersMap = queryParametersMap;
        }

        public String getToken() {
            if (this.queryParametersMap != null) {
                String amount = queryParametersMap.get("token");
                if (!TextUtils.isEmpty(amount)) {
                    return amount;
                }
            }
            return "";
        }

        public String getProxy() {
            if (this.queryParametersMap != null) {
                String amount = queryParametersMap.get("proxy");
                if (!TextUtils.isEmpty(amount)) {
                    return amount;
                }
            }
            return "";
        }

        public String getAmount() {
            if (this.queryParametersMap != null) {
                String amount = queryParametersMap.get("amount");
                if (!TextUtils.isEmpty(amount)) {
                    return amount;
                }
            }
            return "";
        }

        public String getSysFlowNo() {
            if (this.queryParametersMap != null) {
                String amount = queryParametersMap.get("sysFlowNo");
                if (!TextUtils.isEmpty(amount)) {
                    return amount;
                }
            }
            return "";
        }
    }
}
