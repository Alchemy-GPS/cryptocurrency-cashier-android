package com.achpay.wallet.database;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.RecentOrder;

import com.achpay.wallet.database.CryptocurrencyDao;
import com.achpay.wallet.database.RecentOrderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cryptocurrencyDaoConfig;
    private final DaoConfig recentOrderDaoConfig;

    private final CryptocurrencyDao cryptocurrencyDao;
    private final RecentOrderDao recentOrderDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        cryptocurrencyDaoConfig = daoConfigMap.get(CryptocurrencyDao.class).clone();
        cryptocurrencyDaoConfig.initIdentityScope(type);

        recentOrderDaoConfig = daoConfigMap.get(RecentOrderDao.class).clone();
        recentOrderDaoConfig.initIdentityScope(type);

        cryptocurrencyDao = new CryptocurrencyDao(cryptocurrencyDaoConfig, this);
        recentOrderDao = new RecentOrderDao(recentOrderDaoConfig, this);

        registerDao(Cryptocurrency.class, cryptocurrencyDao);
        registerDao(RecentOrder.class, recentOrderDao);
    }
    
    public void clear() {
        cryptocurrencyDaoConfig.clearIdentityScope();
        recentOrderDaoConfig.clearIdentityScope();
    }

    public CryptocurrencyDao getCryptocurrencyDao() {
        return cryptocurrencyDao;
    }

    public RecentOrderDao getRecentOrderDao() {
        return recentOrderDao;
    }

}
