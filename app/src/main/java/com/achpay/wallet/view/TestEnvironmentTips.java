package com.achpay.wallet.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.achpay.wallet.R;

public class TestEnvironmentTips extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView mCancel;
    private TextView mConfirm;

    public TestEnvironmentTips(@NonNull Context context) {
        this(context, R.style.GlobalDialog);
    }

    public TestEnvironmentTips(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private void init() {
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_test_environment, null);
        mCancel = layoutView.findViewById(R.id.dialog_updte_cancel);
        mConfirm = layoutView.findViewById(R.id.dialog_updte_confirm);
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        this.setContentView(layoutView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_updte_cancel) {
            this.dismiss();
        } else if (v.getId() == R.id.dialog_updte_confirm) {
            this.dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
    }
}
