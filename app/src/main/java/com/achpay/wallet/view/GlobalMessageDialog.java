package com.achpay.wallet.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.achpay.wallet.R;

import java.io.Serializable;

public class GlobalMessageDialog extends Dialog {
    private Context mContext;
    private TextView mTitle;
    private TextView mCancel;
    private TextView mConfirm;
    private TextView mContent;
    private View mainView;
    private OnConfirmClickListener confirmListener;
    private OnCancelClickListener cancelListener;
    private Serializable message;

    public GlobalMessageDialog(@NonNull Context context) {
        this(context, R.style.GlobalDialog);
    }

    public GlobalMessageDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mainView = LayoutInflater.from(mContext).inflate(R.layout.dialog_global_message, null);
        mContent = mainView.findViewById(R.id.dialog_global_content);
        mTitle = mainView.findViewById(R.id.dialog_global_title);
        mCancel = mainView.findViewById(R.id.dialog_global_cancel);
        mConfirm = mainView.findViewById(R.id.dialog_global_confirm);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    if (message != null) {
                        confirmListener.onConfirmClick(message);
                    } else {
                        confirmListener.onConfirmClick();
                    }
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onCancelClick();
                }
            }
        });

        this.setContentView(mainView);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public void setCancel(String cancel) {
        mCancel.setText(cancel);
    }

    public void setConfirm(String confirm) {
        mConfirm.setText(confirm);
    }

    public void setContent(String content) {
        this.mContent.setText(content);
    }

    public void setTransferMessage(Serializable message) {
        this.message = message;
    }

    public void setOnConfirmClickListener(OnConfirmClickListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(Serializable message);
        void onConfirmClick();
    }

    public interface OnCancelClickListener {
        void onCancelClick();
    }
}
