package com.achpay.wallet.mvp.assets;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.base.presenter.IPresenter;
import com.achpay.wallet.base.view.BaseActivity;
import com.achpay.wallet.model.UserInfoResponse;
import com.achpay.wallet.mvp.order.OrderDetailActivity;
import com.achpay.wallet.utils.Log;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class AssetDetailActivity extends BaseActivity<AssetsPresenter> implements AssetsView, View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    private List<UserInfoResponse.AccountInfos> mList;
    private AssetCoinAdapter mAdapter;
    private KProgressHUD progressHUD;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);
        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.assets_detail_title));
        mTitleBack.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.assets_detail_rv);
        mRefreshLayout = findViewById(R.id.history_refresh);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mRefreshLayout.setEnableLoadMore(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new AssetCoinAdapter(this);

        mList = new ArrayList<>();

        mRecyclerView.setAdapter(mAdapter);

        showProgress();

        MvpPre.getUserInfo(getSelfActivity());
    }

    @Override
    protected AssetsPresenter bindPresenter() {
        return new AssetsPresenter(this);
    }

    @Override
    public void setDatas(List<UserInfoResponse.AccountInfos> merchantAccounts) {
        mList = merchantAccounts;
        mAdapter.setDatas(mList);
    }

    @Override
    public void addOrderHistory(List<UserInfoResponse.AccountInfos> merchantAccounts) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mRefreshLayout.setNoMoreData(false);
        MvpPre.getUserInfo(getSelfActivity());
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        Log.i("加载更多");
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
                    .setSize(125, 125)
                    .setLabel(getString(R.string.progress_wait))
                    .setCancellable(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressHUD.dismiss();
                            AssetDetailActivity.this.finish();
                        }
                    });
        }

        if (!progressHUD.isShowing()) {
            progressHUD.show();
        }
    }

    @Override
    public void dismissProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        }
    }
}
