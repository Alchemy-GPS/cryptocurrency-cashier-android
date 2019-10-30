package com.achpay.wallet.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class DiscountSettingDialog extends Dialog implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Context mContext;
    private View mainView;
    private OnConfirmListener listener;
    private CheckBox mCashBox;
    private CheckBox mPercentBox;
    private EditText mCashAmount;
    private EditText mPercentAmount;
    private TextView mCancel;
    private TextView mConfirm;
    private String cashDiscountAmount;
    private String percentDiscountAmount;

    public DiscountSettingDialog(@NonNull Context context) {
        this(context, R.style.DiscountDialog);
    }

    public DiscountSettingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
        initView();
        initData();
    }

    private void initView() {
        mainView = LayoutInflater.from(mContext).inflate(R.layout.dialog_discount_setting, null);
        mCashBox = mainView.findViewById(R.id.dialog_cash_discount_cb);
        mPercentBox = mainView.findViewById(R.id.dialog_percent_discount_cb);
        mCashAmount = mainView.findViewById(R.id.dialog_cash_discount_et);
        mPercentAmount = mainView.findViewById(R.id.dialog_percent_discount_et);
        mCancel = mainView.findViewById(R.id.dialog_discount_cancel);
        mConfirm = mainView.findViewById(R.id.dialog_discount_confirm);
        RelativeLayout mCashBoxLayout = mainView.findViewById(R.id.dialog_cash_discount_rl);
        RelativeLayout mPercentBoxLayout = mainView.findViewById(R.id.dialog_percent_discount_rl);

        mCashBoxLayout.setOnClickListener(this);
        mPercentBoxLayout.setOnClickListener(this);

        mCashBox.setOnCheckedChangeListener(this);
        mPercentBox.setOnCheckedChangeListener(this);

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        this.setContentView(mainView);
    }

    public void initData() {
        String discountType = SharedPreferenceUtil.getPref(mContext).getString(User.DISCOUNT_TYPE,User.DISCOUNT_CASH);

        if (User.DISCOUNT_CASH.equals(discountType)) {
            mCashBox.setChecked(true);

            cashDiscountAmount = SharedPreferenceUtil.getPref(mContext).getString(User.DISCOUNT_CASH_AMOUNT, "0");

            mCashAmount.setText(cashDiscountAmount);

            mCashAmount.setVisibility(View.VISIBLE);

            mPercentAmount.setVisibility(View.GONE);

            mPercentBox.setChecked(false);

            setSesection(mCashAmount);
        } else if (User.DISCOUNT_PERCENT.equals(discountType)) {
            mPercentBox.setChecked(true);

            percentDiscountAmount = SharedPreferenceUtil.getPref(mContext).getString(User.DISCOUNT_PERCENT_AMOUNT, "0");

            mPercentAmount.setText(percentDiscountAmount);

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
        if (buttonView.getId() == R.id.dialog_cash_discount_cb) {
            if (isChecked) {
                mCashAmount.setVisibility(View.VISIBLE);

                mPercentAmount.setVisibility(View.GONE);

                mPercentBox.setChecked(false);
            } else {
                mPercentAmount.setVisibility(View.VISIBLE);

                mCashAmount.setVisibility(View.GONE);

                mPercentBox.setChecked(true);
            }
        } else if (buttonView.getId() == R.id.dialog_percent_discount_cb) {
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
        if (v.getId() == R.id.dialog_discount_cancel) {
            this.dismiss();
        } else if (v.getId() == R.id.dialog_discount_confirm) {
            if (mCashBox.isChecked()) {
                String cashDiscount = mCashAmount.getText().toString().trim();
                if (NumberUtils.isWrongCash(cashDiscount)) {
                    Toast.makeText(mContext, mContext.getString(R.string.discount_setting_error_cash), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!cashDiscount.equals(this.cashDiscountAmount) && listener != null) {
                    this.listener.onDiscountConfirm(true, cashDiscount);
                }
            } else if (mPercentBox.isChecked()) {
                String percentDiscount = mPercentAmount.getText().toString().trim();

                if (NumberUtils.isWrongPercent(percentDiscount)) {
                    Toast.makeText(mContext, mContext.getString(R.string.discount_setting_error_percent), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!percentDiscount.equals(this.percentDiscountAmount) && listener != null) {
                    this.listener.onDiscountConfirm(false, percentDiscount);
                }
            }
            this.dismiss();
        } else if (v.getId() == R.id.dialog_cash_discount_rl) {
            if (mCashBox.isChecked()) {
                mCashBox.setChecked(false);
            } else {
                mCashBox.setChecked(true);
            }

        } else if (v.getId() == R.id.dialog_percent_discount_rl) {
            if (mPercentBox.isChecked()) {
                mPercentBox.setChecked(false);
            } else {
                mPercentBox.setChecked(true);
            }
        }
    }

    public interface OnConfirmListener {
        void onDiscountConfirm(boolean isCashDiscount,String discountAmount);
    }
}
