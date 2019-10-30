package com.achpay.wallet.database.dbmodel;

import com.achpay.wallet.database.DaoManager;

import java.util.List;

public class RecentOrderDaoManager {
    private static final String TAG = "AbnormalOrderManager";
    private DaoManager mManager;
    private volatile static RecentOrderDaoManager manager = new RecentOrderDaoManager();

    private RecentOrderDaoManager() {
        mManager = DaoManager.getInstance();
    }

    public static RecentOrderDaoManager getInstance() {
        if (manager == null) {
            synchronized (RecentOrderDaoManager.class) {
                if (manager == null) {
                    manager = new RecentOrderDaoManager();
                }
            }
        }
        return manager;
    }

    /**
     * 完成recentOrder记录的插入，如果表未创建，先创建RecentOrder表
     * 如果记录条数超过200 则删除最老的一条
     *
     * @param recentOrder
     * @return
     */
    public boolean insertRecentOrder(RecentOrder recentOrder) {
        List<RecentOrder> abnormalOrderList = mManager.getDaoSession().getRecentOrderDao().loadAll();
        int size = abnormalOrderList.size();
        if (size > 200) {
            deleteRecentOrder(abnormalOrderList.get(0));
        }
        return mManager.getDaoSession().getRecentOrderDao().insertOrReplace(recentOrder) != -1;
    }

    /**
     * 修改一条数据
     *
     * @param recentOrder
     * @return
     */
    public boolean updateRecentOrder(RecentOrder recentOrder) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getRecentOrderDao().update(recentOrder);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @param recentOrder
     * @return
     */
    public boolean deleteRecentOrder(RecentOrder recentOrder) {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().getRecentOrderDao().delete(recentOrder);
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
            mManager.getDaoSession().deleteAll(RecentOrder.class);
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
    public List<RecentOrder> queryAllRecentOrder() {
        return mManager.getDaoSession().getRecentOrderDao().loadAll();
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public RecentOrder queryRecentOrderById(String key) {
        return mManager.getDaoSession().getRecentOrderDao().load(key);
    }
}
