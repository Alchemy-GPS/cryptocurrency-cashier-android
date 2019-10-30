package com.achpay.wallet.mvp.transaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseFragment;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.event.TransactionSelectedEvent;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.cointype.TabFragmentAdapter;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.widget.tabs.SlidingTabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends BaseFragment<TransactionPresenter> implements TransactionView{
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;

    public static TransactionFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        TransactionFragment fragment = new TransactionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected TransactionPresenter bindPresenter() {
        return new TransactionPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        EventBusUtil.register(this);

        TransParams.TRANSACTION_TAB_TITLES[0] = getString(R.string.transaction_title_order);
        TransParams.TRANSACTION_TAB_TITLES[1] = getString(R.string.transaction_title_statement);

        View mainView = inflater.inflate(R.layout.fragment_main_transaction, container, false);

        initView(mainView);

        return mainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    private void initView(View mainView) {
        mTabLayout = mainView.findViewById(R.id.fragment_transaction_tablayout);

        mViewPager = mainView.findViewById(R.id.fragment_transaction_viewpager);

        TransatonFragmentAdapter mAdapter = new TransatonFragmentAdapter(getSelfActivity().getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabLayout.redrawIndicator(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view) {
                    TextView textView = view.findViewById(R.id.tab_item_title);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    textView.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view) {
                    TextView textView = view.findViewById(R.id.tab_item_title);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    textView.setTextColor(getResources().getColor(R.color.grey_aa));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(getSelfActivity()).inflate(R.layout.item_tab_layout, null);
        TextView textView = view.findViewById(R.id.tab_item_title);
        textView.setText(TransParams.TRANSACTION_TAB_TITLES[position]);
        if (position == 0) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textView.setTextColor(getResources().getColor(R.color.white));
        }
        return view;
    }

    /**
     * 页面选中时,刷新页面
     * @param event 选中事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPageSelected(TransactionSelectedEvent event) {
        if (event != null) {
            if (event.isNeedRefresh()) {

            }
        }
    }
}
