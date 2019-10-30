package com.achpay.wallet.mvp.input;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class InputAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
    private List<Fragment> fragments = new ArrayList<>();
    private String[] mTitles;

    public InputAdapter(FragmentManager fm , String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
        fragments.add(AmountInputFragment.newInstance(mTitles[0].trim(), mTitles[1].trim()));

        fragments.add(CoinInputFragment.newInstance(mTitles[0].trim(), mTitles[1].trim()));
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
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
