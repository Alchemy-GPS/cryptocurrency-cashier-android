package com.achpay.wallet.mvp.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.login.LoginActivity;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;

public class SettingActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RelativeLayout mSettingExtra;
    private RelativeLayout mSettingDiscount;
    private CheckBox mSettingDiscountCb;
    private CheckBox mSettingExtraCb;
    private TextView mAccountAddress;
    private TextView mAccountPeriod;
    private TextView mAccountSettlement;
    private TextView mAccountSettleType;
    private long titleBarFirstClickTime;
    private int titleBarClickTimes;
    private RelativeLayout mSettingNetwork;
    private View mLineAboveNetWork;

    private RelativeLayout mGrantPoints;
    private View mLineGrantPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        RelativeLayout mTitleBar = findViewById(R.id.setting_titlebar);
        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);

        mTitle.setText(getString(R.string.setting_title));
        mTitleBar.setOnClickListener(this);
        mTitleBack.setOnClickListener(this);

        mAccountAddress = findViewById(R.id.account_detail_address);
        mAccountPeriod = findViewById(R.id.account_detail_period);
        mAccountSettlement = findViewById(R.id.account_detail_settlement);
        mAccountSettleType = findViewById(R.id.account_detail_settletype);

        mSettingDiscountCb = findViewById(R.id.setting_discount_cb);
        RelativeLayout mSettingDiscountRl = findViewById(R.id.setting_discount_rl);

        mSettingDiscount = findViewById(R.id.setting_discount);

        mSettingExtraCb = findViewById(R.id.setting_extra_cb);
        RelativeLayout mSettingExtraRl = findViewById(R.id.setting_extra_rl);

        mSettingExtra = findViewById(R.id.setting_extra);

        RelativeLayout mSettingCurrency = findViewById(R.id.setting_currency_type);
        RelativeLayout mSettingLaguage = findViewById(R.id.setting_default_language);
        mSettingNetwork = findViewById(R.id.setting_net_work);
        mLineAboveNetWork = findViewById(R.id.above_setting_net_work);
        mGrantPoints = findViewById(R.id.setting_grant_points);
        mLineGrantPoints = findViewById(R.id.above_setting_grant_points);

        RelativeLayout mQuit = findViewById(R.id.setting_quit);

        mSettingDiscountCb.setOnCheckedChangeListener(this);
        mSettingExtraCb.setOnCheckedChangeListener(this);

        mSettingDiscountRl.setOnClickListener(this);
        mSettingExtraRl.setOnClickListener(this);

        mSettingDiscount.setOnClickListener(this);
        mSettingExtra.setOnClickListener(this);
        mSettingCurrency.setOnClickListener(this);
        mSettingLaguage.setOnClickListener(this);
        mQuit.setOnClickListener(this);
        mSettingNetwork.setOnClickListener(this);
        mGrantPoints.setOnClickListener(this);

        String settleType = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_TYPE);
        String settlePeriod = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_PERIOD);
        String settleAmount = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_AMOUNT);
        String settleAddress = SharedPreferenceUtil.getPref(this).getStringValue(User.SETTLE_ADDRESS);

        mAccountAddress.setText(settleAddress);
        mAccountPeriod.setText(settlePeriod);
        mAccountSettlement.setText(settleAmount);
        mAccountSettleType.setText(settleType);

        String settingAmountType = SharedPreferenceUtil.getPref(this).getString(User.SETTING_AMOUNT_TYPE, User.SETTING_AMOUNT_DISCOUNT);

        if (User.SETTING_AMOUNT_DISCOUNT.equals(settingAmountType)) {
            mSettingDiscountCb.setChecked(true);

            mSettingExtraCb.setChecked(false);

            mSettingDiscount.setVisibility(View.VISIBLE);

            mSettingExtra.setVisibility(View.GONE);
        } else if (User.SETTING_AMOUNT_EXTRA.equals(settingAmountType)) {
            mSettingExtraCb.setChecked(true);

            mSettingDiscountCb.setChecked(false);

            mSettingExtra.setVisibility(View.VISIBLE);

            mSettingDiscount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        } else if (v.getId() == R.id.setting_titlebar) {
            //设置优惠
            titleBarClickTimes++;
            if (titleBarClickTimes == 1) {
                titleBarFirstClickTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - titleBarFirstClickTime < 5000) {
                if (titleBarClickTimes == 5) {
                    mSettingNetwork.setVisibility(View.VISIBLE);
                    mLineAboveNetWork.setVisibility(View.VISIBLE);
                    mLineGrantPoints.setVisibility(View.VISIBLE);
                    mGrantPoints.setVisibility(View.VISIBLE);
                    titleBarClickTimes = 0;
                }
            } else {
                titleBarClickTimes = 0;
            }
        } else if (v.getId() == R.id.setting_discount) {
            //设置优惠
            Intent intent = new Intent(this, SettingDiscountActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.setting_extra) {
            //设置附加费
            Intent intent = new Intent(this, SettingExtraActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.setting_currency_type) {
            ///设置法币
            Intent intent = new Intent(this, SettingCurrencyTypeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.setting_default_language) {
            //设置语言
            Intent intent = new Intent(this, SettingLanguageActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.setting_net_work) {
            //设置网络
            Intent intent = new Intent(this, SettingNetworkActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.setting_grant_points) {
            //发放积分
            Intent intent = new Intent(this, GrantPointsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.setting_discount_rl) {
            if (mSettingDiscountCb.isChecked()) {
                mSettingDiscountCb.setChecked(false);
            } else {
                mSettingDiscountCb.setChecked(true);
            }
        } else if (v.getId() == R.id.setting_extra_rl) {
            if (mSettingExtraCb.isChecked()) {
                mSettingExtraCb.setChecked(false);
            } else {
                mSettingExtraCb.setChecked(true);
            }
        } else if (v.getId() == R.id.setting_quit) {
            CommonUtil.clearLoginInfo(this);

            setResult(TransParams.ACTIVITY_RESULT_DESTROY);

            WSClientMessage message = new WSClientMessage();
            message.setMessage(TransParams.CLOSE_WEBSOCKET);
            EventBusUtil.post(message);

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            this.finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.setting_discount_cb) {
            if (isChecked) {
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_AMOUNT_TYPE, User.SETTING_AMOUNT_DISCOUNT);

                mSettingDiscount.setVisibility(View.VISIBLE);

                mSettingExtra.setVisibility(View.GONE);

                mSettingExtraCb.setChecked(false);
            } else {
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_AMOUNT_TYPE, User.SETTING_AMOUNT_EXTRA);

                mSettingExtra.setVisibility(View.VISIBLE);

                mSettingDiscount.setVisibility(View.GONE);

                mSettingExtraCb.setChecked(true);
            }
        } else if (buttonView.getId() == R.id.setting_extra_cb) {
            if (isChecked) {
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_AMOUNT_TYPE, User.SETTING_AMOUNT_EXTRA);

                mSettingExtra.setVisibility(View.VISIBLE);

                mSettingDiscount.setVisibility(View.GONE);

                mSettingDiscountCb.setChecked(false);
            } else {
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_AMOUNT_TYPE, User.SETTING_AMOUNT_DISCOUNT);

                mSettingDiscount.setVisibility(View.VISIBLE);

                mSettingExtra.setVisibility(View.GONE);

                mSettingDiscountCb.setChecked(true);
            }
        }
    }

}
