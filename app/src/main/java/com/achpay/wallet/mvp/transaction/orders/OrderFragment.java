package com.achpay.wallet.mvp.transaction.orders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseFragment;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.utils.EventBusUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends BaseFragment<OrderPresenter> implements OrderView, OnRefreshListener, OnLoadMoreListener {
    List<OrderResponse.OrderDetail> mList;
    private OrderAdapter mOrderAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private int currentPage;

    public static OrderFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected OrderPresenter bindPresenter() {
        return new OrderPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        EventBusUtil.register(this);

        View mainView = inflater.inflate(R.layout.fragment_transaction_order, container, false);

        initView(mainView);

        return mainView;
    }

    private void initView(View mainView) {
        mRecyclerView = mainView.findViewById(R.id.fragment_transaction_order_rv);

        mRefreshLayout = mainView.findViewById(R.id.transaction_order_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getSelfActivity()));

        mList = new ArrayList<>();

        mOrderAdapter = new OrderAdapter(getSelfActivity());

        mRecyclerView.setAdapter(mOrderAdapter);

        currentPage = 1;

        MvpPre.getIntervalHistory(TransParams.LAST_MONTH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setTransactionHistory(List<OrderResponse.OrderDetail> mDetails) {
        mOrderAdapter.setDatas(mDetails);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
            currentPage = 1;
        }
    }

    @Override
    public void addTransactionHistory(List<OrderResponse.OrderDetail> mDetails) {
        mOrderAdapter.addDatas(mDetails);
        currentPage = currentPage + 1;
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void stopLoading(boolean haveMore) {
        if (mRefreshLayout.isLoading()) {
            if (!haveMore) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                mRefreshLayout.finishLoadMore();
            }
        }
    }

    @Override
    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mRefreshLayout.setNoMoreData(false);
        MvpPre.getIntervalHistory(TransParams.LAST_MONTH);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        MvpPre.getMoreIntervalHistory(TransParams.LAST_MONTH, currentPage + 1);
    }
}
