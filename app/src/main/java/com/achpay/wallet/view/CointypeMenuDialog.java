package com.achpay.wallet.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.achpay.wallet.R;

public class CointypeMenuDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView mCreateQrcode;
    private TextView mScanQrcode;
    private TextView mCancel;

    private OnCreateQRListener mOnCreateQRListener;
    private OnScanQRListener mOnScanQRListener;

    public CointypeMenuDialog(@NonNull Context context) {
        this(context, R.style.GlobalDialog);
    }

    public CointypeMenuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private void init() {
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_cointype_menu, null);
        mCreateQrcode = layoutView.findViewById(R.id.cointype_menu_create_qrcode);
        mScanQrcode = layoutView.findViewById(R.id.cointype_menu_scan_qrcode);
        mCancel = layoutView.findViewById(R.id.cointype_menu_cancel);
        mCreateQrcode.setOnClickListener(this);
        mScanQrcode.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        this.setContentView(layoutView);
        Window window = getWindow();
        //设置弹出窗口大小
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //设置显示位置
        window.setGravity(Gravity.BOTTOM);
        //设置动画效果
        //window.setWindowAnimations(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cointype_menu_create_qrcode) {

            if (mOnCreateQRListener != null) {
                mOnCreateQRListener.onCreateQR();
            }

            this.dismiss();
        } else if (v.getId() == R.id.cointype_menu_scan_qrcode) {

            if (mOnScanQRListener != null) {
                mOnScanQRListener.onScanQR();
            }

            this.dismiss();
        }else if (v.getId() == R.id.cointype_menu_cancel) {
            this.dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
    }

    public void setOnCreateQRListener(OnCreateQRListener mOnCreateQRListener) {
        this.mOnCreateQRListener = mOnCreateQRListener;
    }

    public void setOnScanQRListener(OnScanQRListener mScanQRListener) {
        this.mOnScanQRListener = mScanQRListener;
    }

    public interface OnCreateQRListener {
        void onCreateQR();
    }

    public interface OnScanQRListener {
        void onScanQR();
    }
}