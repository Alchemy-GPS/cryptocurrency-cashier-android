package com.achpay.wallet.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.wallet.R;

public class ToastUtil {

    public static void show(Context context,String message,int duration) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_msg, null);
        TextView mTextView = view.findViewById(R.id.toast_msg);
        mTextView.setText(message);

        toast.setView(view);

        toast.setGravity(Gravity.BOTTOM, 0, 200);

        toast.setDuration(duration);

        toast.show();
    }

    public static void showInCenter(Context context,String message,int duration) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_msg, null);
        TextView mTextView = view.findViewById(R.id.toast_msg);
        mTextView.setText(message);

        toast.setView(view);

        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.setDuration(duration);

        toast.show();
    }
}
