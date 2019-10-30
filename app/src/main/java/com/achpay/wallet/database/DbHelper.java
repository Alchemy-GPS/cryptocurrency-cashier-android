package com.achpay.wallet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper extends DaoMaster.OpenHelper {
    private static String DBNAME = "wallet.db";

    public DbHelper(Context context) {
        super(context, DBNAME, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion < newVersion) {
            MigrationHelper.getInstance().migrate(db, CryptocurrencyDao.class, RecentOrderDao.class);
            //更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
            //MigrationHelper.getInstance().migrate(db, UserDao.class,XXDao.class);
        }
    }
}
