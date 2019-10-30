package com.achpay.wallet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

public class DaoManager {
    private static final String TAG = DaoManager.class.getSimpleName();

    private Context context;

    //多线程中要被共享的使用volatile关键字修饰
    private volatile static DaoManager manager = new DaoManager();
    private static DaoMaster mDaoMaster;
    private static DbHelper mHelper;
    private static DaoSession mDaoSession;

    /**
     * 单例模式获得操作数据库对象
     * @return
     */
    public static DaoManager getInstance(){
        if(manager==null){
            synchronized (DaoManager.class){
                if(manager==null){
                    manager=new DaoManager();
                }
            }
        }
        return manager;
    }

    public void init(Context context){
        this.context = context;
        mHelper = new DbHelper(context);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     * @return
     */
    public DaoMaster getDaoMaster(){
        if(mDaoMaster == null) {
            DbHelper helper = new DbHelper(context);
            mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     * @return
     */
    public DaoSession getDaoSession(){
        if(mDaoSession == null){
            if(mDaoMaster == null){
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug(){
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection(){
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper(){
        if(mHelper != null){
            mHelper.close();
            mHelper = null;
        }
    }

    public void closeDaoSession(){
        if(mDaoSession != null){
            mDaoSession.clear();
            mDaoSession = null;
        }
    }
}
