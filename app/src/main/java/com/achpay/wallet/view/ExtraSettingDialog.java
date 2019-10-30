package com.achpay.wallet.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
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

public class ExtraSettingDialog extends Dialog implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Context mContext;
    private View mainView;
    private OnConfirmListener listener;
    private CheckBox mCashBox;
    private CheckBox mPercentBox;
    private EditText mCashAmount;
    private EditText mPercentAmount;
    private TextView mCancel;
    private TextView mConfirm;
    private String cashExtraAmount;
    private String percentExtraAmount;

    public ExtraSettingDialog(@NonNull Context context) {
        this(context, R.style.DiscountDialog);
    }

    public ExtraSettingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
        initView();
        initData();
    }

    private void initView() {
        mainView = LayoutInflater.from(mContext).inflate(R.layout.dialog_extra_setting, null);
        mCashBox = mainView.findViewById(R.id.dialog_cash_extra_cb);
        mPercentBox = mainView.findViewById(R.id.dialog_percent_extra_cb);
        mCashAmount = mainView.findViewById(R.id.dialog_cash_extra_et);
        mPercentAmount = mainView.findViewById(R.id.dialog_percent_extra_et);
        mCancel = mainView.findViewById(R.id.dialog_extra_cancel);
        mConfirm = mainView.findViewById(R.id.dialog_extra_confirm);
        RelativeLayout mCashBoxLayout = mainView.findViewById(R.id.dialog_cash_extra_rl);
        RelativeLayout mPercentBoxLayout = mainView.findViewById(R.id.dialog_percent_extra_rl);

        mCashBoxLayout.setOnClickListener(this);
        mPercentBoxLayout.setOnClickListener(this);

        mCashBox.setOnCheckedChangeListener(this);
        mPercentBox.setOnCheckedChangeListener(this);

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        this.setContentView(mainView);
    }

    public void initData() {
        String extraType = SharedPreferenceUtil.getPref(mContext).getString(User.EXTRA_TYPE,User.EXTRA_CASH);

        if (User.EXTRA_CASH.equals(extraType)) {
            mCashBox.setChecked(true);

            cashExtraAmount = SharedPreferenceUtil.getPref(mContext).getString(User.EXTRA_CASH_AMOUNT, "0");

            mCashAmount.setText(cashExtraAmount);

            mCashAmount.setVisibility(View.VISIBLE);

            mPercentAmount.setVisibility(View.GONE);

            mPercentBox.setChecked(false);

            setSesection(mCashAmount);
        } else if (User.EXTRA_PERCENT.equals(extraType)) {
            mPercentBox.setChecked(true);

            percentExtraAmount = SharedPreferenceUtil.getPref(mContext).getString(User.EXTRA_PERCENT_AMOUNT, "0");

            mPercentAmount.setText(percentExtraAmount);

            mPercentAmount.setVisibility(View.VISIBLE);

            mCashAmount.setVisibility(View.GONE);

            mCashBox.setChecked(false);

            setSesection(mPercentAmount);
        }
    }

    public void setOnConfirmistener(OnConfirmListener listener) {
        this.listener = listener;
    }

    private void setSesection(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.dialog_cash_extra_cb) {
            if (isChecked) {
                mCashAmount.setVisibility(View.VISIBLE);

                mPercentAmount.setVisibility(View.GONE);

                mPercentBox.setChecked(false);
            } else {
                mPercentAmount.setVisibility(View.VISIBLE);

                mCashAmount.setVisibility(View.GONE);

                mPercentBox.setChecked(true);
            }
        } else if (buttonView.getId() == R.id.dialog_percent_extra_cb) {
            if (isChecked) {
                mPercentAmount.setVisibility(View.VISIBLE);

                mCashAmount.setVisibility(View.GONE);

                mCashBox.setChecked(false);
            } else {
                mCashAmount.setVisibility(View.VISIBLE);

                mPercentAmount.setVisibility(View.GONE);

                mCashBox.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_extra_cancel) {
            this.dismiss();
        } else if (v.getId() == R.id.dialog_extra_confirm) {
            if (mCashBox.isChecked()) {
                String cashExtra = mCashAmount.getText().toString().trim();

                if (NumberUtils.isWrongCash(cashExtra)) {
                    Toast.makeText(mContext, mContext.getString(R.string.discount_setting_error_cash), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!cashExtra.equals(this.cashExtraAmount) && listener != null) {
                    this.listener.onExtraConfirm(true, cashExtra);
                }
            } else if (mPercentBox.isChecked()) {
                String percentExtra = mPercentAmount.getText().toString().trim();

                if (NumberUtils.isWrongPercent(percentExtra)) {
                    Toast.makeText(mContext, mContext.getString(R.string.discount_setting_error_percent), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!percentExtra.equals(this.percentExtraAmount) && listener != null) {
                    this.listener.onExtraConfirm(false, percentExtra);
                }
            }
            this.dismiss();
        } else if (v.getId() == R.id.dialog_cash_extra_rl) {
            if (mCashBox.isChecked()) {
                mCashBox.setChecked(false);
            } else {
                mCashBox.setChecked(true);
            }

        } else if (v.getId() == R.id.dialog_percent_extra_rl) {
            if (mPercentBox.isChecked()) {
                mPercentBox.setChecked(false);
            } else {
                mPercentBox.setChecked(true);
            }
        }
    }

    public interface OnConfirmListener {
        void onExtraConfirm(boolean isCashExtra, String extraAmount);
    }
}
