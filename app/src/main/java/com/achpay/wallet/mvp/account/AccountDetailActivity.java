package com.achpay.wallet.mvp.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.base.presenter.IPresenter;
import com.achpay.wallet.base.view.BaseActivity;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.setting.SettingActivity;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.widget.CircleImageView;

public class AccountDetailActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView mAccountIcon;
    private TextView mAccountName;
    private TextView mAccountAddress;
    private TextView mAccountPeriod;
    private TextView mAccountSettlement;
    private TextView mAccountSettleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        TextView mLogOut = findViewById(R.id.account_detail_quit);
        RelativeLayout mSetting = findViewById(R.id.account_detail_setting);

        mAccountIcon = findViewById(R.id.account_detail_icon);
        mAccountName = findViewById(R.id.account_detail_merchantname);
        mAccountAddress = findViewById(R.id.account_detail_address);
        mAccountPeriod = findViewById(R.id.account_detail_period);
        mAccountSettlement = findViewById(R.id.account_detail_settlement);
        mAccountSettleType = findViewById(R.id.account_detail_settletype);


        mTitle.setText(getString(R.string.account_detail_title));
        mTitleBack.setOnClickListener(this);
        mLogOut.setOnClickListener(this);
        mSetting.setOnClickListener(this);

        initAccountData();
    }

    @Override
    protected IPresenter bindPresenter() {
        return null;
    }

    private void initAccountData() {
        String settleType = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_TYPE);
        String settlePeriod = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_PERIOD);
        String settleAmount = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_AMOUNT);
        String settleAddress = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_ADDRESS);
        String merchantName = CommonUtil.getMerchantName(this);

        mAccountName.setText(merchantName);
        mAccountAddress.setText(settleAddress);
        mAccountPeriod.setText(settlePeriod);
        mAccountSettlement.setText(settleAmount);
        mAccountSettleType.setText(settleType);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        }else if (v.getId() == R.id.account_detail_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.account_detail_quit) {
            CommonUtil.clearLoginInfo(this);

            setResult(TransParams.ACTIVITY_RESULT_DESTROY);

            WSClientMessage message = new WSClientMessage();
            message.setMessage(TransParams.CLOSE_WEBSOCKET);
            EventBusUtil.post(message);

            this.finish();
        }
    }
}
