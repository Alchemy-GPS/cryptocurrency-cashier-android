package com.achpay.wallet.database.dbmodel;

import com.achpay.wallet.database.CryptocurrencyDao;
import com.achpay.wallet.database.DaoManager;
import com.achpay.wallet.model.CoinTypeResponse;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class CryptocurrencyManger {
    private static final String TAG = "CryptocurrencyManger";
    private DaoManager mManager;
    private volatile static CryptocurrencyManger manager = new CryptocurrencyManger();

    private CryptocurrencyManger() {
        mManager = DaoManager.getInstance();
    }

    public static CryptocurrencyManger getInstance(){
        if(manager==null){
            synchronized (CryptocurrencyManger.class){
                if(manager==null){
                    manager=new CryptocurrencyManger();
                }
            }
        }
        return manager;
    }

    /**
     * 完成cryptocurrency记录的插入，如果表未创建，先创建Cryptocurrency表
     *
     * @param cryptocurrency
     * @return
     */
    public boolean insertCryptocurrency(Cryptocurrency cryptocurrency) {
        return mManager.getDaoSession().getCryptocurrencyDao().insert(cryptocurrency) != -1;
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @param mList
     * @return
     */
    public void insertCryptocurrencys(List<Cryptocurrency> mList) {
        CryptocurrencyDao mCryptocurrencyDao = mManager.getDaoSession().getCryptocurrencyDao();
        mCryptocurrencyDao.insertOrReplaceInTx(mList,false);
    }

    /**
     * 修改一条数据
     *
     * @param cryptocurrency
     * @return
     */
    public boolean updateCryptocurrency(Cryptocurrency cryptocurrency) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getCryptocurrencyDao().update(cryptocurrency);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @param cryptocurrency
     * @return
     */
    public boolean deleteCryptocurrency(Cryptocurrency cryptocurrency) {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().getCryptocurrencyDao().delete(cryptocurrency);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(Cryptocurrency.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<Cryptocurrency> queryAllCryptocurrency() {
        return mManager.getDaoSession().getCryptocurrencyDao().loadAll();
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public Cryptocurrency queryCryptocurrencyById(String key) {
        return mManager.getDaoSession().getCryptocurrencyDao().load(key);
    }

    /**
     * 根据币种名字查询记录
     *
     * @param name
     * @return
     */
    public Cryptocurrency queryCryptocurrencyByName(String name) {
        QueryBuilder<Cryptocurrency> queryBuilder = mManager.getDaoSession().getCryptocurrencyDao().queryBuilder();
        return queryBuilder.where(CryptocurrencyDao.Properties.CryptoCurrency.eq(name)).unique();
    }
}
