package com.achpay.wallet.mvp.history;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.model.OrderResponse;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.order.OrderDetailActivity;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter {
    private Context mContext;

    List<OrderResponse.OrderDetail> mDetails;

    public HistoryAdapter(Context mContext) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history_coin, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HistoryViewHolder) {
            HistoryViewHolder itemHolder = (HistoryViewHolder) holder;

            final OrderResponse.OrderDetail detail = mDetails.get(position);

            itemHolder.coinName.setText(detail.getCryptoCurrency());

            itemHolder.coinAmount.setText(detail.getReceiveQuantity());

            itemHolder.orderTime.setText(detail.getReceiveTime());

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

    private static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView coinName;
        private TextView coinAmount;
        private TextView orderTime;
        private TextView orderStatus;

        HistoryViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            coinName = itemView.findViewById(R.id.item_history_cointype);
            coinAmount = itemView.findViewById(R.id.item_history_amount);
            orderTime = itemView.findViewById(R.id.item_history_ordertime);
            orderStatus = itemView.findViewById(R.id.item_history_status);
        }
    }
}
