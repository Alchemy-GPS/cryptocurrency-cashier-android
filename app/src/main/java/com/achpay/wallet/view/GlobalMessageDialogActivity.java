package com.achpay.wallet.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.achpay.wallet.database.dbmodel.RecentOrder;
import com.achpay.wallet.database.dbmodel.RecentOrderDaoManager;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.event.OrderReadEvent;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.order.OrderDetailActivity;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;

import java.io.Serializable;

public class GlobalMessageDialogActivity extends Activity {

    private GlobalMessageDialog dialog;
    private Serializable message;
    private String orderId;
    private RecentOrder recentOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = getIntent().getSerializableExtra(TransParams.GLOBAL_DIALOG_DATA);
        orderId = getIntent().getStringExtra(TransParams.GLOBAL_DIALOG_LSUCCESS);
        if (!TextUtils.isEmpty(orderId)) {
            Log.i("RecentOrder == " + orderId);
            recentOrder = RecentOrderDaoManager.getInstance().queryRecentOrderById(orderId);
        }
        showMessageDialog();
    }

    private void showMessageDialog() {
        if (dialog != null && dialog.isShowing()) {
            dimissMessageDialog();
        }

        newDialog();

        dialog.show();
    }

    private void newDialog() {
        if (dialog == null) {
            dialog = new GlobalMessageDialog(this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            if (message != null) {
                dialog.setTransferMessage(message);
                if (message instanceof WSClientMessage) {
                    dialog.setContent(((WSClientMessage) message).getMessage());
                }
            }

            dialog.setOnConfirmClickListener(new GlobalMessageDialog.OnConfirmClickListener() {
                @Override
                public void onConfirmClick(Serializable message) {
                    GlobalMessageDialogActivity.this.dimissMessageDialog();
                }

                @Override
                public void onConfirmClick() {
                    Intent intent = new Intent(GlobalMessageDialogActivity.this, OrderDetailActivity.class);
                    if (!TextUtils.isEmpty(orderId)) {
                        if (recentOrder != null) {
                            recentOrder.setIsDialogShown(true);
                            RecentOrderDaoManager.getInstance().updateRecentOrder(recentOrder);
                            EventBusUtil.post(new OrderReadEvent(true, recentOrder.getOrderId()));
                        }

                        intent.putExtra(User.ORDER_ID, orderId);
                        GlobalMessageDialogActivity.this.startActivity(intent);
                    }
                    GlobalMessageDialogActivity.this.dimissMessageDialog();
                }
            });

            dialog.setOnCancelClickListener(new GlobalMessageDialog.OnCancelClickListener() {
                @Override
                public void onCancelClick() {
                    if (recentOrder != null) {
                        recentOrder.setIsDialogShown(true);
                        RecentOrderDaoManager.getInstance().updateRecentOrder(recentOrder);
                    }
                    GlobalMessageDialogActivity.this.dimissMessageDialog();
                }
            });
        }
    }

    private void dimissMessageDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dimissMessageDialog();
    }
}
