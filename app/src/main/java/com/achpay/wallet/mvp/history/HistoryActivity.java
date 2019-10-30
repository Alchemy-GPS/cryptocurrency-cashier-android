package com.achpay.wallet.mvp.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseActivity;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.input.InputActivity;
import com.achpay.wallet.utils.CommonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class HistoryActivity extends BaseActivity<HistoryPresenter> implements HistoryView, View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    private TextView mCoinName;
    private TextView mCoinAmount;
    private TextView mCurrencyAmount;
    private TextView mCurrencyName;
    private RecyclerView mRecyclerView;
    private RelativeLayout mReceipt;
    private String coinName;
    private HistoryAdapter mHistoryAdapter;
    private KProgressHUD progressHUD;
    private int currentPage;
    private SmartRefreshLayout mRefreshLayout;
    private int coinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);

        mTitle.setText(getString(R.string.history_title));
        mTitleBack.setOnClickListener(this);

        mCoinName = findViewById(R.id.history_coin_name);
        mCoinAmount = findViewById(R.id.history_coin_amount);

        mCurrencyAmount = findViewById(R.id.history_total_currency);
        mCurrencyName = findViewById(R.id.history_currency_type);
        mReceipt = findViewById(R.id.history_receipt);
        mRecyclerView = findViewById(R.id.history_rv);
        mRefreshLayout = findViewById(R.id.history_refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mHistoryAdapter = new HistoryAdapter(this);

        mRecyclerView.setAdapter(mHistoryAdapter);

        mReceipt.setOnClickListener(this);

        coinName = getIntent().getStringExtra(TransParams.COIN_NAME);
        coinId = getIntent().getIntExtra(TransParams.COIN_ID, 1);
        String coinAmount = getIntent().getStringExtra(User.TOTAL_COIN);
        String currencyAmount = getIntent().getStringExtra(User.TOTAL_CURRENCY);
        String currencyType = CommonUtil.getCurrencyType(this);

        mCoinName.setText(coinName);
        mCoinAmount.setText(coinAmount);
        mCurrencyName.setText(currencyType);
        mCurrencyAmount.setText(currencyAmount);

        showProgress();
        MvpPre.getOrderHistory(this, coinId);
        currentPage = 1;
    }

    @Override
    protected HistoryPresenter bindPresenter() {
        return new HistoryPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_receipt:
                Intent intent = new Intent(this, InputActivity.class);
                intent.putExtra(TransParams.COIN_NAME, coinName);
                startActivity(intent);
                break;
            case R.id.left_titlebar_back:
                this.finish();
                break;
        }
    }

    @Override
    public void setOrderHistory(List<OrderResponse.OrderDetail> mDetails) {
        mHistoryAdapter.setDatas(mDetails);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
            currentPage = 1;
        }
    }

    @Override
    public void addOrderHistory(List<OrderResponse.OrderDetail> mDetails) {
        mHistoryAdapter.addDatas(mDetails);
        currentPage = currentPage + 1;
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mRefreshLayout.setNoMoreData(false);
        MvpPre.getOrderHistory(this, coinId);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        MvpPre.getMoreHistory(this, coinId, currentPage + 1);
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
    public void showProgress() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setSize(100, 100)
                    .setCancellable(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressHUD.dismiss();
                            HistoryActivity.this.finish();
                        }
                    });
        }
        progressHUD.show();
    }

    @Override
    public void dismissProgress() {
        progressHUD.dismiss();
    }
}
