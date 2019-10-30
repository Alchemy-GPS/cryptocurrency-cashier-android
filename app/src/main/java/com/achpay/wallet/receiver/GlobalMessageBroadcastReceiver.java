package com.achpay.wallet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.view.GlobalMessageDialogActivity;

import java.io.Serializable;

public class GlobalMessageBroadcastReceiver extends BroadcastReceiver {
    private static String action = GlobalMessageBroadcastReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction) && intentAction.equals(action)) {
            Intent activity = new Intent(context, GlobalMessageDialogActivity.class);
            if (intent.getSerializableExtra(TransParams.GLOBAL_DIALOG_DATA) != null) {
                activity.putExtra(TransParams.GLOBAL_DIALOG_DATA, intent.getSerializableExtra(TransParams.GLOBAL_DIALOG_DATA));
            } else if (intent.getStringExtra(TransParams.GLOBAL_DIALOG_LSUCCESS) != null) {
                activity.putExtra(TransParams.GLOBAL_DIALOG_LSUCCESS, intent.getStringExtra(TransParams.GLOBAL_DIALOG_LSUCCESS));
            }
            activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activity);
        }
    }

    public void register(Context context) {
        context.registerReceiver(this, new IntentFilter(action));
    }

    public void unRegister(Context context) {
        context.unregisterReceiver(this);
    }

    public static void sendBroadcast(Context context) {
        context.sendBroadcast(new Intent(action));
    }

    public static void sendBroadcast(Context context, Serializable message) {
        Intent intent = new Intent(action);
        intent.putExtra(TransParams.GLOBAL_DIALOG_DATA, message);
        context.sendBroadcast(intent);
    }

    public static void sendLSuccess(Context context, String message) {
        Intent intent = new Intent(action);
        intent.putExtra(TransParams.GLOBAL_DIALOG_LSUCCESS, message);
        context.sendBroadcast(intent);
    }
}
