package com.achpay.wallet.mvp.transaction.statements;

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
import com.achpay.wallet.model.StatementResponse;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.transaction.orders.OrderAdapter;
import com.achpay.wallet.mvp.transaction.orders.OrderFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class StatementFragment extends BaseFragment<StatementPresenter> implements StatementView, OnRefreshListener, OnLoadMoreListener {
    @Override
    protected StatementPresenter bindPresenter() {
        return new StatementPresenter(this);
    }

    List<StatementResponse.Detail> mList;
    private StatementAdapter mStatementAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private int currentPage;

    public static StatementFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        StatementFragment fragment = new StatementFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        EventBusUtil.register(this);

        View mainView = inflater.inflate(R.layout.fragment_transaction_statement, container, false);

        initView(mainView);

        return mainView;
    }

    private void initView(View mainView) {
        mRefreshLayout = mainView.findViewById(R.id.transaction_statement_refresh);
        mRecyclerView = mainView.findViewById(R.id.fragment_transaction_statement_rv);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getSelfActivity()));

        mList = new ArrayList<>();

        mStatementAdapter = new StatementAdapter(getSelfActivity());

        mRecyclerView.setAdapter(mStatementAdapter);

        currentPage = 1;

        MvpPre.getStatementHistory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setStatementHistory(List<StatementResponse.Detail> mDetails) {
        mStatementAdapter.setDatas(mDetails);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
            currentPage = 1;
        }
    }

    @Override
    public void addStatementHistory(List<StatementResponse.Detail> mDetails) {
        mStatementAdapter.addDatas(mDetails);
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
        MvpPre.getStatementHistory();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        MvpPre.getMoreIntervalHistory(currentPage + 1);
    }
}
