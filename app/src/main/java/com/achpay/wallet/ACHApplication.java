package com.achpay.wallet;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.achpay.wallet.database.DaoManager;

public class ACHApplication extends MultiDexApplication {
    public static ACHApplication APPLICATION;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APPLICATION = this;

        DaoManager.getInstance().init(this);
    }
}
