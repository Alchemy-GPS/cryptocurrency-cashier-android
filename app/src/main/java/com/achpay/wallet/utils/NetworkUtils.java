package com.achpay.wallet.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.achpay.wallet.R;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class NetworkUtils {
    /**
     * 检查网络是否连接
     * @param context 全局context
     * @return true 已连接 false 未连接
     */
    public static boolean checkNetworkConnect(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 获取当前的网络状态
     * @param context 全局context
     * @return
     * 没有网络-NO
     * WIFI网络-WIFI
     * 4G网络-4G
     * 3G网络-3G
     * 2G网络-2G
     * 未知-Unknown
     */
    public static String getAPNType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {      //无网络
            return "NO";
        }

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {       //wifi
            return "WIFI";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                return "4G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSUPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B
                    || nSubType == TelephonyManager.NETWORK_TYPE_EHRPD
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPAP
                    && !telephonyManager.isNetworkRoaming()) {
                return "3G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    || nSubType == TelephonyManager.NETWORK_TYPE_1xRTT
                    || nSubType == TelephonyManager.NETWORK_TYPE_IDEN
                    && !telephonyManager.isNetworkRoaming()) {
                return "2G";
            } else {
                return "Unknown";
            }
        }

        return "Unknown";
    }
/*

    */
/**
     * 获取bks文件的sslsocketfactory
     * @param context
     * @return
     *//*

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        final String CLIENT_TRUST_PASSWORD = "123456";//信任证书密码，该证书默认密码是123456
        final String CLIENT_AGREEMENT = "TLS";//使用协议
        final String CLIENT_TRUST_KEYSTORE = "BKS";
        SSLContext sslContext = null;
        try {
            //取得SSL的SSLContext实例
            sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
            //取得TrustManagerFactory的X509密钥管理器实例
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //取得BKS密库实例
            KeyStore tks = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
            InputStream is = context.getResources().openRawResource(R.raw.traint);
            try {
                tks.load(is, CLIENT_TRUST_PASSWORD.toCharArray());
            } finally {
                is.close();
            }
            //初始化密钥管理器
            trustManager.init(tks);
            //初始化SSLContext
            sslContext.init(null, trustManager.getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SslContextFactory", e.getMessage());
        }
        return sslContext.getSocketFactory();
    }
*/
}
