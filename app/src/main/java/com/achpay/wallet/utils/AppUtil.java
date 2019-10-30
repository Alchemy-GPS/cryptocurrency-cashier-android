package com.achpay.wallet.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.achpay.wallet.ACHApplication;
import com.achpay.wallet.R;

import org.greenrobot.greendao.annotation.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppUtil {

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取App包名
     *
     * @param context 上下文
     * @return App包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    public static String getAppVersionName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取系统版本号(Android版本)
     *
     * @return 系统版本号(Android版本)
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取厂商名称
     *
     * @return 厂商名称
     */
    public static String getDeviceName() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     *
     * @return 设备型号
     */
    public static String getDeviceVersion() {
        return Build.MODEL;
    }

    /**
     * 获取唯一设备号
     * @param context 上下文
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static boolean isNetConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) ACHApplication.APPLICATION
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查询手机是否处于WiFi状态
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 用途：判断当前app的进程所属于的状态
     * <p>
     * RunningAppProcessInfo类中的一下状态：
     * <p>
     * 1.IMPORTANCE_FOREGROUND ：这个进程在运行前台的UI，即用户在和屏幕UI进行交互。
     * <p>
     * 2.IMPORTANCE_BACKGROUND ：这个进程包含可以展开的后台代码。
     * <p>
     * 3.IMPORTANCE_FOREGROUND_SERVICE：该进程正在运行前台的服务，例如：用户没立即在运用程序中，但是同时该运用程序还在播放音乐。
     * <p>
     * 4.IMPORTANCE_TOP_SLEEPING：该进程在运行前台的服务，但是手机处于睡眠状态，因此用户是不可见的。
     * 这意味着用户不是真的意识到到这进程，因为用户没有看到或者与进程互动，但是它也是很重要的。因为它是他们期望的返回，一次解锁设备。
     * <p>
     * 5.IMPORTANCE_VISIBLE ：
     * <p>
     * 手机测试结果：
     * 1.正在运行app的activity的（包含锁屏）时候属于IMPORTANCE_FOREGROUND 。
     * <p>
     * 2.运行activity的app，然后按home键，时候处于IMPORTANCE_BACKGROUND。
     * <p>
     * 3.销毁了全部的activity，还保留service的时候属于IMPORTANCE_BACKGROUND。
     * <p>
     * 4.只保留service，然后锁屏的时候属于IMPORTANCE_BACKGROUND。
     * <p>
     * 5.运行当前app，然后切换到其他运用的时候属于IMPORTANCE_BACKGROUND。
     *
     * @param context
     * @return
     */
    public static boolean checkAppProcessState(Context context, final int code) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //返回在设备上运行的运用程序进程的列表
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcessInfos) {
            if (runningAppProcessInfo.processName.equals(context.getPackageName())) {//匹配到当前app的进程。
                if (runningAppProcessInfo.importance == code) {//进程是否处于指定进程
                    return true;
                } else {
                    return false;
                }
            } else continue;
        }
        return false;
    }

    /**
     * //RunningTaskInfo.topActivity是activity栈中栈顶的activity，即当前屏幕上的activity的名字
     * //名字：com.xxxx.qmclient.activity.DialogActivity
     * 检查某个类是否处于栈顶
     *
     * @param context
     * @param activityName
     * @return
     */
    public static boolean isCurrentActivityState(Context context, String activityName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null && runningTaskInfos.size() > 0) {
            for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
                if (activityName.equals(runningTaskInfo.topActivity.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过包名+类名组件
     *
     * @param mClass
     * @return
     */
    public static String getPackagePathActivityName(Class<?> mClass) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(mClass.getPackage().getName());
        stringBuffer.append(".");
        stringBuffer.append(mClass.getSimpleName());
        return stringBuffer.toString();
    }

    /**
     * 直接过取带包路径的类名
     *
     * @param mClass
     * @return
     */
    public static String getName(Class<?> mClass) {
        return mClass.getName();
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     * @author SHANHY
     * @date 2015年10月28日
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     * @author SHANHY
     * @date 2015年10月28日
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean copytoClipboard(Context mContext, @NotNull String label, @NotNull String content) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText(label, content);
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            return true;
        }
        return false;
    }

    /**
     * 保持屏幕常亮
     *
     * @param mContext Activity页面
     */
    public static void keepScreenOn(Activity mContext) {
        Window window = mContext.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 关闭保持屏幕常亮
     *
     * @param mContext Activity页面
     */
    public static void closeKeepScreenOn(Activity mContext) {
        Window window = mContext.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void vibrator(Context mContext,long milliseconds) {
        Vibrator vibrator = (Vibrator)mContext.getSystemService(Service.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(milliseconds);
        }
    }

    /**
     * 不启动键盘,并显示光标
     * @param mContext
     * @param editText
     */
    public static void disableShowInput(final Context mContext, final EditText editText) {
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(editText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    //隐藏系统软键盘
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
                    }
                }
            }
        });
    }

    public static void closeActivityOnSchedule(final Activity activity, long time) {
        Observable.timer(time, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (isForeground(activity)) {
                            activity.finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isForeground(activity)) {
                            activity.finish();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取Manifest文件中的配置值
     * @param context 上下文
     * @param name manifest中配置的meta name的值
     * @return
     */
    public static String getAppMetaData(Context context, String name) {
        if (context == null || TextUtils.isEmpty(name)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(name);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * 获取渠道名称
     * @param context 上下文
     * @return
     */
    public static String getAppChannel(Context context) {
       return getAppMetaData(context, "APP_CHANNEL");
    }

    public static Bitmap drawableToBitmap(Context mContext,int drawableId) {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = mContext.getDrawable(drawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } else {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
        }

        return bitmap;
    }

    /**
     * 判断是否有虚拟底部按钮
     *
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 获取底部虚拟按键高度
     *
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
