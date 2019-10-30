package com.achpay.wallet;

import com.achpay.wallet.model.params.User;
import com.achpay.wallet.utils.SharedPreferenceUtil;

public class Constants {
    //    APP中的Log开关
    public static final String serverVersion = "3.0";
    public static boolean logEnabled;

    //    生产访问的API地址
    public static String IP;
    public static String LIGHTNING_ADDRESS_URL;
    public static String QFPAY_LOGIN_URL;
    public static String SESSION_AUTH;
    public static String BITCOIN_EXPLORE_URL;
    public static String ETHEREUM_EXPLORE_URL;
    public static String APP_HOST;

    static {
        if (BuildConfig.BUILD_TYPE.equals("release")) {
            //生产地址
            IP = "app.alchemy.foundation/foundation-web";
            APP_HOST = "https://" + IP + "/";
            LIGHTNING_ADDRESS_URL = "http://54.169.18.25:9091/lightningNetwork/getUris";
            QFPAY_LOGIN_URL = "https://openapi.qfpay.com/tool/v1/simple_login";
            SESSION_AUTH = "https://openapi.qfpay.com/tool/v1/user_info";
            BITCOIN_EXPLORE_URL = "https://live.blockcypher.com/btc/address/";
            ETHEREUM_EXPLORE_URL = "https://etherscan.io/address/";
            logEnabled = false;


//            IP = "3.0.150.229:9096/foundation-web";
//            APP_HOST = "http://" + IP + "/";
//            LIGHTNING_ADDRESS_URL = "http://3.0.150.229:9091/lightningNetwork/getUris";
//            QFPAY_LOGIN_URL = "https://openapi-test.qfpay.com/tool/v1/simple_login";
//            SESSION_AUTH = "https://openapi-test.qfpay.com/tool/v1/user_info";
//            BITCOIN_EXPLORE_URL = "https://live.blockcypher.com/btc-testnet/address/";
//            ETHEREUM_EXPLORE_URL = "https://ropsten.etherscan.io/address/";
//            logEnabled = true;

        } else {
            //测试地址
//            IP = "test.alchemy.foundation/foundation-web";
//            APP_HOST = "https://" + IP + "/";
            IP = "3.0.150.229:9096/foundation-web";
            APP_HOST = "http://" + IP + "/";
            LIGHTNING_ADDRESS_URL = "http://3.0.150.229:9091/lightningNetwork/getUris";
            QFPAY_LOGIN_URL = "https://openapi-test.qfpay.com/tool/v1/simple_login";
            SESSION_AUTH = "https://openapi-test.qfpay.com/tool/v1/user_info";
            BITCOIN_EXPLORE_URL = "https://live.blockcypher.com/btc-testnet/address/";
            ETHEREUM_EXPLORE_URL = "https://ropsten.etherscan.io/address/";
            logEnabled = true;


            //生产地址
//            IP = "app.alchemy.foundation/foundation-web";
//            APP_HOST = "https://" + IP + "/";
//            LIGHTNING_ADDRESS_URL = "http://54.169.18.25:9091/lightningNetwork/getUris";
//            QFPAY_LOGIN_URL = "https://openapi.qfpay.com/tool/v1/simple_login";
//            SESSION_AUTH = "https://openapi.qfpay.com/tool/v1/user_info";
//            BITCOIN_EXPLORE_URL = "https://live.blockcypher.com/btc/address/";
//            ETHEREUM_EXPLORE_URL = "https://etherscan.io/address/";
//            logEnabled = false;


        }
    }

    public static String WS_HOST = "ws://" + IP + "/websocket/socketServer.do";


    public static String OAUTH_URL = "https://openapi-test.qfpay.com/oauth/v2/authorize";
    public static String OAUTH_REDIRECT = "http://47.89.16.234/access/getcode";
    public static String OAUTH_STATE = SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getString(User.OAUTH_STATE, "TEST");
}
