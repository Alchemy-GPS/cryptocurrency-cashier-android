package com.achpay.wallet.mvp.cointype;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.cointype.pages.CoinFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public TabFragmentAdapter(FragmentManager fm, String amount, String currencyId) {
        super(fm);
        fragments.add(CoinFragment.newInstance(TransParams.COINTYPE_TAB_TITLES[0], amount, currencyId));
        fragments.add(CoinFragment.newInstance(TransParams.COINTYPE_TAB_TITLES[1], amount, currencyId));
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
