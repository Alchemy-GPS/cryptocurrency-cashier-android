package com.achpay.wallet.mvp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.achpay.wallet.model.event.TransactionSelectedEvent;
import com.achpay.wallet.mvp.checkout.CheckoutFragment;
import com.achpay.wallet.mvp.checkout.WipayCheckoutFragment;
import com.achpay.wallet.mvp.mine.MineFragment;
import com.achpay.wallet.mvp.transaction.TransactionFragment;
import com.achpay.wallet.utils.EventBusUtil;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {"收款", "账单", "我的"};
    public TransactionSelectedEvent needRefreshEvent = new TransactionSelectedEvent(true);

    public MainAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(CheckoutFragment.newInstance(titles[0]));
//        fragments.add(WipayCheckoutFragment.newInstance(titles[0]));
        fragments.add(TransactionFragment.newInstance(titles[1]));
        fragments.add(MineFragment.newInstance(titles[2]));
        if (needRefreshEvent != null) {
            needRefreshEvent = new TransactionSelectedEvent(true);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {//账单页
            EventBusUtil.post(needRefreshEvent);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
