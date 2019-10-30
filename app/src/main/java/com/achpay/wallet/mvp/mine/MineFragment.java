package com.achpay.wallet.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.OrderStatusMessage;
import com.achpay.wallet.model.UserInfoResponse;
import com.achpay.wallet.model.event.SettingCurrencyEvent;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.about.AboutActivity;
import com.achpay.wallet.mvp.assets.AssetDetailActivity;
import com.achpay.wallet.mvp.lightningnetwork.LightningNetworkActivity;
import com.achpay.wallet.mvp.setting.SettingActivity;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示用户相关信息
 * 头像,总余额,各个币种的余额
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineView, View.OnClickListener, OnRefreshListener {

    private TextView mAccountName;
    private ImageView mIcon;
    private TextView mCoinAmount;
    private TextView mCoinType;
    private TextView mCurrencyType;
    private TextView mCurrencyAmount;
    private RelativeLayout mSetting;
    private RelativeLayout mAbount;

    private MineCoinAdapter mMineCoinAdapter;
    private List<UserInfoResponse.AccountInfos> mList;
    private RelativeLayout mAssetDetail;
    private SmartRefreshLayout mRefreshLayout;

    public static MineFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        MineFragment fragment = new MineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected MinePresenter bindPresenter() {
        return new MinePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_main_mine, container, false);

        initView(mainView);

        return mainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    private void initView(View mainView) {
        EventBusUtil.register(this);

        mAbount = mainView.findViewById(R.id.account_detail_about);
        mSetting = mainView.findViewById(R.id.account_detail_setting);

        RecyclerView mRecyclerView = mainView.findViewById(R.id.mine_assetdetail_rv);
        mAccountName = mainView.findViewById(R.id.account_merchant_name);
        mCoinAmount = mainView.findViewById(R.id.account_merchant_coinamount);
        mCurrencyAmount = mainView.findViewById(R.id.account_merchant_currencyamount);
        mCoinType = mainView.findViewById(R.id.account_merchant_cointype);
        mCurrencyType = mainView.findViewById(R.id.account_merchant_currencytype);
        mIcon = mainView.findViewById(R.id.account_merchant_icon);
        mAssetDetail = mainView.findViewById(R.id.account_detail_asset);
        mRefreshLayout = mainView.findViewById(R.id.fragment_mine_refresh);

        RelativeLayout mLightingLayout = mainView.findViewById(R.id.account_lightning_network_layout);

        mRefreshLayout.setOnRefreshListener(this);
        mSetting.setOnClickListener(this);
        mAbount.setOnClickListener(this);
        mAssetDetail.setOnClickListener(this);
        mLightingLayout.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getSelfActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mList = new ArrayList<>();

        mMineCoinAdapter = new MineCoinAdapter(getSelfActivity());

        mRecyclerView.setAdapter(mMineCoinAdapter);

        mAccountName.setText(CommonUtil.getMerchantName(getSelfActivity()));

        MvpPre.getUserInfo();
    }

    @Override
    public void setUserInfo(UserInfoResponse userInfoResponse) {
        mCoinType.setText(getString(R.string.empty_space_one).concat(userInfoResponse.getTargetCryptoUnit()));

        mCurrencyType.setText(userInfoResponse.getTargetCurrencyUnit());

        mCoinAmount.setText(userInfoResponse.getTotalcryptocurrency());

        mCurrencyAmount.setText(userInfoResponse.getTotalcurrency());

        //mIcon 设置头像
    }

    @Override
    public void setDatas(List<UserInfoResponse.AccountInfos> merchantAccounts) {
        mList = merchantAccounts;
        mMineCoinAdapter.setDatas(mList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingCurrencyEvent(SettingCurrencyEvent event) {
        if (event != null && event.getMessage().equals(ResponseParam.SUCCESS)) {
            MvpPre.getUserInfo();
        }
    }

    /**
     * 账户信息(余额)发生变化的时候
     * @param message 变化事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountChanged(OrderStatusMessage message) {
        if (message.getResult().equals(ResponseParam.SUCCESS)
                ||message.getResult().equals(ResponseParam.MSUCCESS)
                ||message.getResult().equals(ResponseParam.LSUCCESS)) {
            MvpPre.getUserInfo();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TransParams.ACTIVITY_REQUEST_NORMAL) {
            if (resultCode == TransParams.ACTIVITY_RESULT_DESTROY) {
                getSelfActivity().finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.account_detail_about) {
            Intent intent = new Intent(getSelfActivity(), AboutActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.account_detail_asset) {
            Intent intent = new Intent(getSelfActivity(), AssetDetailActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.account_detail_setting) {
            Intent intent = new Intent(getSelfActivity(), SettingActivity.class);
            startActivityForResult(intent,TransParams.ACTIVITY_REQUEST_NORMAL);
        }else if (v.getId() == R.id.account_lightning_network_layout) {
            Intent intent = new Intent(getSelfActivity(), LightningNetworkActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        MvpPre.getUserInfo();
    }

    @Override
    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
    }
}
