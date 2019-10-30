package com.achpay.wallet.mvp.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.model.SettingExpenseInfo;
import com.achpay.wallet.model.SettingExpenseResult;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.NumberUtils;
import com.crazysunj.cardslideview.CardHandler;

public class SettingExpenseHandler implements CardHandler<SettingExpenseInfo> {
    private transient OnCancelListener mOnCancelListener;
    private transient OnConfirmListener mOnConfirmListener;
    private transient Context mContext;

    public SettingExpenseHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onBind(Context context, SettingExpenseInfo data, final int position, int mode) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_expense_setting, null);

        TextView mCancel = view.findViewById(R.id.expense_cancel);
        TextView mConfrim = view.findViewById(R.id.expense_confirm);

        TextView mTitle = view.findViewById(R.id.expense_setting_title);
        TextView mCash = view.findViewById(R.id.expense_setting_cash);
        TextView mPercent = view.findViewById(R.id.expense_setting_percent);

        RelativeLayout mCashCheckLayout = view.findViewById(R.id.expense_cash_discount_rl);
        RelativeLayout mPercentCheckLayout = view.findViewById(R.id.expense_percent_discount_rl);

        final CheckBox mCashCheck = view.findViewById(R.id.expense_cash_discount_cb);
        final CheckBox mPercentCheck = view.findViewById(R.id.expense_percent_discount_cb);

        final EditText mCashAmount = view.findViewById(R.id.expense_cash_discount_et);
        final EditText mPercentAmount = view.findViewById(R.id.expense_percent_discount_et);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCancelListener != null) {
                    mOnCancelListener.onCancel();
                }
            }
        });

        mConfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmListener != null) {
                    SettingExpenseResult result = new SettingExpenseResult();

                    if (mCashCheck.isChecked()) {
                        String cashDiscount = mCashAmount.getText().toString().trim();
                        if (NumberUtils.isWrongCash(cashDiscount)) {
                            Toast.makeText(mContext, mContext.getString(R.string.discount_setting_error_cash), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        result.setCash(true);
                        result.setAmount(cashDiscount);
                    } else if (mPercentCheck.isChecked()) {
                        String percentDiscount = mPercentAmount.getText().toString().trim();

                        if (NumberUtils.isWrongPercent(percentDiscount)) {
                            Toast.makeText(mContext, mContext.getString(R.string.discount_setting_error_percent), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        result.setCash(false);
                        result.setAmount(percentDiscount);
                        //添加前后金额是否一样
                    }

                    if (position == 0) {
                        result.setDiscount(true);
                    } else {
                        result.setDiscount(false);
                    }

                    EventBusUtil.post(result);

                    mOnConfirmListener.onConfirm();
                }
            }
        });

        mCashCheckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择了现金
                if (mCashCheck.isChecked()) {
                    mCashCheck.setChecked(false);
                } else {
                    mCashCheck.setChecked(true);
                }
            }
        });
        mPercentCheckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择了百分比
                if (mPercentCheck.isChecked()) {
                    mPercentCheck.setChecked(false);
                } else {
                    mPercentCheck.setChecked(true);
                }
            }
        });

        mCashCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCashAmount.setVisibility(View.VISIBLE);

                    mPercentAmount.setVisibility(View.GONE);

                    mPercentCheck.setChecked(false);
                } else {
                    mPercentAmount.setVisibility(View.VISIBLE);

                    mCashAmount.setVisibility(View.GONE);

                    mPercentCheck.setChecked(true);
                }
            }
        });
        mPercentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPercentAmount.setVisibility(View.VISIBLE);

                    mCashAmount.setVisibility(View.GONE);

                    mCashCheck.setChecked(false);
                } else {
                    mCashAmount.setVisibility(View.VISIBLE);

                    mPercentAmount.setVisibility(View.GONE);

                    mCashCheck.setChecked(true);
                }
            }
        });

        mTitle.setText(data.getTitle());
        mCash.setText(data.getCashExpenseDes());
        mPercent.setText(data.getPercentExpenseDes());

        mCashAmount.setHint(data.getCashExpenseHint());
        mPercentAmount.setHint(data.getPercentExpenseHint());

        if (data.isCashExpense()) {
            mCashCheck.setChecked(true);
        } else {
            mPercentCheck.setChecked(true);
        }

        return view;
    }

    public void setOnCancelListener(OnCancelListener mOnCancelListener) {
        this.mOnCancelListener = mOnCancelListener;
    }

    public void setOnConfirmListener(OnConfirmListener mOnConfirmListener) {
        this.mOnConfirmListener = mOnConfirmListener;
    }

    public interface OnCancelListener {
        void onCancel();
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
