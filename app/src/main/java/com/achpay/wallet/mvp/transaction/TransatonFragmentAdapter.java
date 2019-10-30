package com.achpay.wallet.mvp.transaction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.transaction.orders.OrderFragment;
import com.achpay.wallet.mvp.transaction.statements.StatementFragment;

import java.util.ArrayList;
import java.util.List;

public class TransatonFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public TransatonFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(OrderFragment.newInstance(TransParams.TRANSACTION_TAB_TITLES[0]));
        fragments.add(StatementFragment.newInstance(TransParams.TRANSACTION_TAB_TITLES[1]));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
