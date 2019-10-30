package com.achpay.wallet.mvp.setting;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.utils.NumberUtils;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.widget.MyRadioGroup;

public class SettingDiscountActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
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
        setContentView(R.layout.activity_setting_discount);
        mConfirm = findViewById(R.id.discount_setting_confirm);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.discount_setting_title));
        mTitleBack.setOnClickListener(this);

        mCashEt = findViewById(R.id.discount_setting_cash_et);
        mCashLayout = findViewById(R.id.discount_setting_cash_rl);
        mCashBox = findViewById(R.id.discount_setting_cash_cb);
        RelativeLayout mCashBoxLayout = findViewById(R.id.discount_setting_cash_cb_layout);

        mPercentEt = findViewById(R.id.discount_setting_percent_et);
        mPercentLayout = findViewById(R.id.discount_setting_percent_rl);
        mPercentBox = findViewById(R.id.discount_setting_percent_cb);
        RelativeLayout mPercentBoxLayout = findViewById(R.id.discount_setting_percent_cb_layout);


        mCashBoxLayout.setOnClickListener(this);
        mPercentBoxLayout.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mCashBox.setOnCheckedChangeListener(this);
        mPercentBox.setOnCheckedChangeListener(this);

        String discountType = SharedPreferenceUtil.getPref(this).getString(User.DISCOUNT_TYPE, User.DISCOUNT_CASH);

        if (User.DISCOUNT_CASH.equals(discountType)) {
            String cashDiscountAmount = SharedPreferenceUtil.getPref(this).getString(User.DISCOUNT_CASH_AMOUNT, "0");

            mCashBox.setChecked(true);

            mCashLayout.setVisibility(View.VISIBLE);

            mPercentLayout.setVisibility(View.GONE);

            mPercentBox.setChecked(false);

            mCashEt.setText(cashDiscountAmount);

            setSelection(mCashEt);

        } else if (User.DISCOUNT_PERCENT.equals(discountType)) {
            String percentDiscountAmount = SharedPreferenceUtil.getPref(this).getString(User.DISCOUNT_PERCENT_AMOUNT, "0");

            mPercentEt.setText(percentDiscountAmount);

            mPercentBox.setChecked(true);

            mPercentLayout.setVisibility(View.VISIBLE);

            mCashLayout.setVisibility(View.GONE);

            mCashBox.setChecked(false);

            setSelection(mPercentEt);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        } else if (v.getId() == R.id.discount_setting_confirm) {
            setDiscount();
        } else if (v.getId() == R.id.discount_setting_cash_cb_layout) {
            if (mCashBox.isChecked()) {
                mCashBox.setChecked(false);
            } else {
                mCashBox.setChecked(true);
            }
        } else if (v.getId() == R.id.discount_setting_percent_cb_layout) {
            if (mPercentBox.isChecked()) {
                mPercentBox.setChecked(false);
            } else {
                mPercentBox.setChecked(true);
            }
        }
    }

    private void setDiscount() {
        if (mCashBox.isChecked()) {
            String cashDiscount = mCashEt.getText().toString().trim();

            if (NumberUtils.isWrongCash(cashDiscount)) {
                Toast.makeText(this, getString(R.string.discount_setting_error_cash), Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferenceUtil.getPref(this).putStringValue(User.DISCOUNT_TYPE, User.DISCOUNT_CASH);

            SharedPreferenceUtil.getPref(this).putStringValue(User.DISCOUNT_CASH_AMOUNT, cashDiscount);

        } else if (mPercentBox.isChecked()) {
            String percentDiscount = mPercentEt.getText().toString().trim();

            if (NumberUtils.isWrongPercent(percentDiscount)) {
                Toast.makeText(this, getString(R.string.discount_setting_error_percent), Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferenceUtil.getPref(this).putStringValue(User.DISCOUNT_TYPE, User.DISCOUNT_PERCENT);

            SharedPreferenceUtil.getPref(this).putStringValue(User.DISCOUNT_PERCENT_AMOUNT, percentDiscount);
        }

        SettingDiscountActivity.this.finish();
    }

    private void setSelection(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.discount_setting_cash_cb) {
            if (isChecked) {
                mCashLayout.setVisibility(View.VISIBLE);

                mPercentLayout.setVisibility(View.GONE);

                mPercentBox.setChecked(false);
            } else {
                mPercentLayout.setVisibility(View.VISIBLE);

                mCashLayout.setVisibility(View.GONE);

                mPercentBox.setChecked(true);
            }
        } else if (buttonView.getId() == R.id.discount_setting_percent_cb) {
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
