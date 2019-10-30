package com.achpay.wallet.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.achpay.wallet.ACHApplication;
import com.achpay.wallet.R;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.database.dbmodel.RecentOrderDaoManager;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class CommonUtil {

    public static String getToken(Context mContext) {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(mContext).getStringValue(User.TOKEN))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(mContext).getStringValue(User.TOKEN));
    }

    public static String getMerchantId(Context mContext) {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(mContext).getStringValue(User.MERCHANT_ID))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(mContext).getStringValue(User.MERCHANT_ID));
    }

    public static String getMerchantId() {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getStringValue(User.MERCHANT_ID))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getStringValue(User.MERCHANT_ID));
    }

    public static String getQfpayMerchantId() {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getStringValue(User.QFPAY_MERCHANT_ID))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getStringValue(User.QFPAY_MERCHANT_ID));
    }

    public static String getMerchantName(Context mContext) {
        return SharedPreferenceUtil.getPref(mContext).getStringValue(User.MERCHANT_NAME);
    }

    public static String getSysId() {
        return SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getStringValue(User.SYS_ID);
    }

    public static String getSessionId(Context mContext) {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(mContext).getStringValue(User.SESSION_ID))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(mContext).getStringValue(User.SESSION_ID));
    }

    public static String getQfpaySessionId() {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getStringValue(User.QFPAY_SESSION_ID))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getStringValue(User.QFPAY_SESSION_ID));
    }

    public static String getLastOrderStatus(Context mContext) {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(mContext).getStringValue(User.LAST_ORDER_STATUS))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(mContext).getString(User.LAST_ORDER_STATUS, ""));
    }

    public static String getLastOrderDetail(Context mContext) {
        if (TextUtils.isEmpty(SharedPreferenceUtil.getPref(mContext).getStringValue(User.LAST_ORDER_DETAIL))) {
            return "";
        }
        return AES.decrypt(SharedPreferenceUtil.getPref(mContext).getString(User.LAST_ORDER_DETAIL, ""));
    }

    public static String getDeviceId() {
        return SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getString(User.DEVICE_ID, "");
    }

    public static String getCryptoCurrencyId() {
        return SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getString(User.SETTING_CRYPTOCURRENCY_ID, "1");
    }

    public static String getCryptoCurrency() {
        return SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getString(User.SETTING_CRYPTOCURRENCY, "BTC");
    }

    public static String getCurrencyId() {
        return SharedPreferenceUtil.getPref(ACHApplication.APPLICATION).getString(User.SETTING_CURRENCY_ID, "840");
    }

    public static String getCurrencyType(Context mContext) {
        return SharedPreferenceUtil.getPref(mContext).getString(User.SETTING_CURRENCY, "USD");
    }

    public static String getCurrencyTypeById(Context mContext, String currencyId) {
        String[] currencyNames = mContext.getResources().getStringArray(R.array.currency_name);
        String[] currencyIds = mContext.getResources().getStringArray(R.array.currency_id);

        for (int i = 0; i < currencyIds.length; i++) {
            if (currencyId.equals(currencyIds[i])) {
                return currencyNames[i];
            }
        }
        //没有则返回默认
        return SharedPreferenceUtil.getPref(mContext).getString(User.SETTING_CURRENCY, "USD");
    }

    public static String getCurrencyUnit(Context mContext) {
        return SharedPreferenceUtil.getPref(mContext).getString(User.SETTING_CURRENCY_UNIT, "US$");
    }

    public static void setCurrencyUnitById(Context mContext, String currencyId) {
        String[] currencyUnits = mContext.getResources().getStringArray(R.array.currency_unit);
        String[] currencyIds = mContext.getResources().getStringArray(R.array.currency_id);

        for (int i = 0; i < currencyIds.length; i++) {
            if (currencyId.equals(currencyIds[i])) {
                SharedPreferenceUtil.getPref(mContext).putStringValue(User.SETTING_CURRENCY_UNIT, currencyUnits[i]);
            }
        }
    }

    public static boolean isSupportedCurrency(Context mContext, String currencyId) {
        String[] currencyIds = mContext.getResources().getStringArray(R.array.currency_id);

        for (int i = 0; i < currencyIds.length; i++) {
            if (currencyId.equals(currencyIds[i])) {
                return true;
            }
        }
        return false;
    }

    public static String transOrderStatus(Context mContext, String status) {
        String translate = "";
        if (status.equals("CONFIRMING")) {
            translate = mContext.getString(R.string.order_detail_confirming);
        } else if (status.equals("SUCCESS")) {
            translate = mContext.getString(R.string.order_detail_success);
        } else if (status.equals("LSUCCESS")) {
            translate = mContext.getString(R.string.order_detail_lsuccess);
        } else if (status.equals("MSUCCESS")) {
            translate = mContext.getString(R.string.order_detail_msuccess);
        } else if (status.equals("DEALING")) {
            translate = mContext.getString(R.string.order_detail_dealing);
        } else if (status.equals("FAIL")) {
            translate = mContext.getString(R.string.order_detail_fail);
        } else if (status.equals("CLOSE")) {
            translate = mContext.getString(R.string.order_detail_close);
        }
        return translate;
    }

    public static String transStatementStatus(Context mContext, String status) {
        String translate = "";
        if (status.equals("trade")) {
            translate = mContext.getString(R.string.transaction_statement_trade);
        } else if (status.equals("exchange")) {
            translate = mContext.getString(R.string.transaction_statement_exchange);
        } else if (status.equals("settle")) {
            translate = mContext.getString(R.string.transaction_statement_settle);
        } else if (status.equals("rate")) {
            translate = mContext.getString(R.string.transaction_statement_trade);
        }
        return translate;
    }

    /**
     * 根据时间和五位随机数创建订单号
     *
     * @return 返回创建好的订单号
     */
    public static String createOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String newDate = sdf.format(new Date());
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return newDate + rannum;
    }

    /**
     * 对传递过来的字符串进行base64加密
     *
     * @param str 待加密的字符串
     * @return 字符串base64加密后的结果
     */
    public static String Base64(@NonNull String str) {
        String result = "";
        try {
            result = new String(Base64.encodeBase64(str.getBytes("UTF-8")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对传递过来的字符串进行md5加密
     *
     * @param str 待加密的字符串
     * @return 字符串Md5加密后的结果
     */
    public static String MD5(@NonNull String str) {
        return new String(Hex.encodeHex(DigestUtils.md5(str)));
    }

    /**
     * 利用MD5进行加密
     *
     * @param file 待加密的文件
     * @return 加密后的字符串
     */
    public static String MD5File(File file) {
        FileInputStream fis = null;
        String md5 = null;
        try {
            fis = new FileInputStream(file);
            //md5 = DigestUtils.md5Hex(fis);//服务端用此方法
            md5 = new String(Hex.encodeHex(DigestUtils.md5(fis)));//Android端没有encodeHexString方法，只能使用这种方式
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(md5)) {
            return "";
        }
        return md5.toUpperCase();
    }

    public static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;

        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public static byte[] hexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("指令字符串长度必须为偶数 !!!");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 获取今天的日期
     *
     * @return 今天的日期
     */
    public static String getTodayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d = new Date();
        return format.format(d);
    }

    /**
     * 获取上周的日期
     *
     * @return 上周的日期
     */
    public static String getLastWeekDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);
        Date d = c.getTime();
        return format.format(d);
    }

    /**
     * 获取上个月的日期
     *
     * @return 上个月的日期
     */
    public static String getLastMonthDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date d = c.getTime();
        return format.format(d);
    }

    /**
     * 获取去年的今天的日期
     *
     * @return 去年的今天的日期
     */
    public static String getLastYearDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date d = c.getTime();
        return format.format(d);
    }

    /**
     * 将unix时间转换为中国标准时间
     *
     * @return 时间
     */
    public static String formatDateTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(time);
        return format.format(date);
    }

    /**
     * 获取当前时区
     *
     * @return 时区
     */
    public static String getTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    /**
     * 获取当前时间
     *
     * @return 时间值
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

    /**
     * 判断当前币种是否支持隔离见证
     *
     * @param coinId 比重名称
     * @return 结果
     */
    public static boolean supportSegwit(String coinId) {
        if (coinId.equals("1")) {
            return true;
        }
        if (coinId.equals("4")) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前币种是否支持隔离见证
     *
     * @param coinId 比重名称
     * @return 结果
     */
    public static boolean isVite(String coinId) {
        if (coinId.equals("47") || coinId.equals("54")) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前币种是否支持闪电网络
     *
     * @param coinId 币种ID
     * @return 结果
     */
    public static boolean supportLightningNetwork(String coinId) {
        if (TextUtils.isEmpty(coinId)) {
            return false;
        }

        return CryptocurrencyManger.getInstance().queryCryptocurrencyById(coinId).getCoinType().equals(TransParams.LIGHTNING);
    }

    /**
     * 判断当前币种是否支持闪电网络
     *
     * @param coinId 币种ID
     * @return 结果
     */
    public static boolean supportRaidenNetwork(String coinId) {
        if (TextUtils.isEmpty(coinId)) {
            return false;
        }

        if (coinId.equals("3")) {
            return false;
        }

        return CryptocurrencyManger.getInstance().queryCryptocurrencyById(coinId).getCoinType().equals(TransParams.LIGHTNING);
    }

    public static boolean copyAddress(Context mContext, @NotNull String address) {
        return AppUtil.copytoClipboard(mContext, "ADDRESS", address);
    }

    public static void clearLoginInfo(Context mContext) {

        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mContext);
        CookieManager.getInstance().removeAllCookie();
        cookieSyncManager.sync();

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.TOKEN, "");

        //merchantId
        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.MERCHANT_ID, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.MERCHANT_NAME, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.SESSION_ID, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.LAST_ORDER_DETAIL, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.QFPAY_MERCHANT_ID, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.SETTING_CURRENCY_ID, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.SETTING_CURRENCY, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.SETTING_CRYPTOCURRENCY_ID, "");

        SharedPreferenceUtil
                .getPref(mContext)
                .putStringValue(User.SETTING_CRYPTOCURRENCY, "");

        CryptocurrencyManger.getInstance().deleteAll();
        RecentOrderDaoManager.getInstance().deleteAll();
    }
}
