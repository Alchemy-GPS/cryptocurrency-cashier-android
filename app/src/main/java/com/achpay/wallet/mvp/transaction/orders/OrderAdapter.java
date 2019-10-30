package com.achpay.wallet.mvp.transaction.orders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.order.OrderDetailActivity;
import com.achpay.wallet.utils.CommonUtil;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter {
    private Context mContext;

    List<OrderResponse.OrderDetail> mDetails;

    public OrderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<OrderResponse.OrderDetail> mDetails) {
        this.mDetails = mDetails;
        notifyDataSetChanged();
    }

    public void addDatas(List<OrderResponse.OrderDetail> mDetails) {
        this.mDetails.addAll(mDetails);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_transaction_order, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final OrderResponse.OrderDetail detail = mDetails.get(position);

        if (holder instanceof TransactionViewHolder) {
            TransactionViewHolder itemHolder = (TransactionViewHolder) holder;

            itemHolder.coinName.setText(detail.getCryptoCurrency());

            itemHolder.coinAmount.setText(detail.getQuantity());

            itemHolder.currencyName.setText(detail.getCurrency());

            itemHolder.currencyAmount.setText(detail.getQuantityFait());

            itemHolder.orderTime.setText(detail.getReceiveTime());

            itemHolder.orderStatus.setText(CommonUtil.transOrderStatus(mContext, detail.getResult()));

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);

                    intent.putExtra(User.ORDER_ID, detail.getOrderId());

                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDetails == null ? 0 : mDetails.size();
    }

    private static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView coinName;
        private TextView coinAmount;
        private TextView currencyName;
        private TextView currencyAmount;
        private TextView orderTime;
        private TextView orderStatus;

        TransactionViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            coinName = itemView.findViewById(R.id.item_transaction_order_cointype);
            coinAmount = itemView.findViewById(R.id.item_transaction_order_coin_amount);
            orderTime = itemView.findViewById(R.id.item_transaction_order_time);
            orderStatus = itemView.findViewById(R.id.item_transaction_order_status);
            currencyName = itemView.findViewById(R.id.item_transaction_order_currencytype);
            currencyAmount = itemView.findViewById(R.id.item_transaction_order_currency_amount);
        }
    }
}