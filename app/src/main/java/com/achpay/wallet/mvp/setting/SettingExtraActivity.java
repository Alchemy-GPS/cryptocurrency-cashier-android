package com.achpay.wallet.mvp.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.utils.NumberUtils;
import com.achpay.wallet.utils.SharedPreferenceUtil;

public class SettingExtraActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText mCashEt;
    private EditText mPercentEt;
    private Button mConfirm;
    private RelativeLayout mCashLayout;
    private CheckBox mCashBox;
    private RelativeLayout mPercentLayout;
    private CheckBox mPercentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_extra);

        mConfirm = findViewById(R.id.extra_setting_confirm);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.extra_setting_title));
        mTitleBack.setOnClickListener(this);

        mCashEt = findViewById(R.id.extra_setting_cash_et);
        mCashLayout = findViewById(R.id.extra_setting_cash_rl);
        mCashBox = findViewById(R.id.extra_setting_cash_cb);
        RelativeLayout mCashBoxLayout = findViewById(R.id.extra_setting_cash_cb_layout);


        mPercentEt = findViewById(R.id.extra_setting_percent_et);
        mPercentLayout = findViewById(R.id.extra_setting_percent_rl);
        mPercentBox = findViewById(R.id.extra_setting_percent_cb);
        RelativeLayout mPercentBoxLayout = findViewById(R.id.extra_setting_percent_cb_layout);

        mCashBoxLayout.setOnClickListener(this);
        mPercentBoxLayout.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mCashBox.setOnCheckedChangeListener(this);
        mPercentBox.setOnCheckedChangeListener(this);

        String extraType = SharedPreferenceUtil.getPref(this).getString(User.EXTRA_TYPE,User.EXTRA_CASH);

        if (User.EXTRA_CASH.equals(extraType)) {
            String cashExtraAmount = SharedPreferenceUtil.getPref(this).getString(User.EXTRA_CASH_AMOUNT, "0");

            mCashBox.setChecked(true);

            mCashLayout.setVisibility(View.VISIBLE);

            mPercentLayout.setVisibility(View.GONE);

            mPercentBox.setChecked(false);

            mCashEt.setText(cashExtraAmount);

            setSesection(mCashEt);

        } else if (User.EXTRA_PERCENT.equals(extraType)) {
            String percentExtraAmount = SharedPreferenceUtil.getPref(this).getString(User.EXTRA_PERCENT_AMOUNT, "0");

            mPercentEt.setText(percentExtraAmount);

            mPercentBox.setChecked(true);

            mPercentLayout.setVisibility(View.VISIBLE);

            mCashLayout.setVisibility(View.GONE);

            mCashBox.setChecked(false);

            setSesection(mPercentEt);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        } else if (v.getId() == R.id.extra_setting_confirm) {
            setExtra();
        }else if (v.getId() == R.id.extra_setting_cash_cb_layout) {
            if (mCashBox.isChecked()) {
                mCashBox.setChecked(false);
            } else {
                mCashBox.setChecked(true);
            }

        } else if (v.getId() == R.id.extra_setting_percent_cb_layout) {
            if (mPercentBox.isChecked()) {
                mPercentBox.setChecked(false);
            } else {
                mPercentBox.setChecked(true);
            }
        }
    }

    private void setExtra() {
        if (mCashBox.isChecked()) {
            String cashExtra = mCashEt.getText().toString().trim();

            if (NumberUtils.isWrongCash(cashExtra)) {
                Toast.makeText(this, getString(R.string.discount_setting_error_cash), Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferenceUtil.getPref(this).putStringValue(User.EXTRA_TYPE, User.EXTRA_CASH);

            SharedPreferenceUtil.getPref(this).putStringValue(User.EXTRA_CASH_AMOUNT, cashExtra);

        } else if (mPercentBox.isChecked()) {
            String percentExtra = mPercentEt.getText().toString().trim();

            if (NumberUtils.isWrongPercent(percentExtra)) {
                Toast.makeText(this, getString(R.string.discount_setting_error_percent), Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferenceUtil.getPref(this).putStringValue(User.EXTRA_TYPE, User.EXTRA_PERCENT);

            SharedPreferenceUtil.getPref(this).putStringValue(User.EXTRA_PERCENT_AMOUNT, percentExtra);
        }

        SettingExtraActivity.this.finish();
    }

    private void setSesection(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.extra_setting_cash_cb) {
            if (isChecked) {
                mCashLayout.setVisibility(View.VISIBLE);

                mPercentLayout.setVisibility(View.GONE);

                mPercentBox.setChecked(false);
            } else {
                mPercentLayout.setVisibility(View.VISIBLE);

                mCashLayout.setVisibility(View.GONE);

                mPercentBox.setChecked(true);
            }
        } else if (buttonView.getId() == R.id.extra_setting_percent_cb) {
            if (isChecked) {
                mPercentLayout.setVisibility(View.VISIBLE);

                mCashLayout.setVisibility(View.GONE);

                mCashBox.setChecked(false);
            } else {
                mCashLayout.setVisibility(View.VISIBLE);

                mPercentLayout.setVisibility(View.GONE);

                mCashBox.setChecked(true);
            }
        }
    }
}
