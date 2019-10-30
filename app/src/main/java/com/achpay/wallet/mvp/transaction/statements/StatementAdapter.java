package com.achpay.wallet.mvp.transaction.statements;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.model.StatementResponse;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.order.OrderDetailActivity;
import com.achpay.wallet.utils.CommonUtil;

import java.util.List;

public class StatementAdapter extends RecyclerView.Adapter<StatementAdapter.StatementViewHolder> {
    private Context mContext;

    List<StatementResponse.Detail> mDetails;

    public StatementAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<StatementResponse.Detail> mDetails) {
        this.mDetails = mDetails;
        notifyDataSetChanged();
    }

    public void addDatas(List<StatementResponse.Detail> mDetails) {
        this.mDetails.addAll(mDetails);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_transaction_statement, parent, false);
        return new StatementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatementViewHolder holder, int position) {
        final StatementResponse.Detail detail = mDetails.get(position);

        holder.coinAmount.setText(detail.getReceiveQuantity());
        holder.coinName.setText(detail.getCryptoCurrency());

        holder.currencyName.setText(detail.getCurrency());
        holder.currencyAmount.setText(detail.getReceiveQuantityFait());

        holder.statementTime.setText(detail.getReceiveTime());

        holder.statementType.setText(CommonUtil.transStatementStatus(mContext, detail.getAccountType()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(detail.getOrderId())) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);

                    intent.putExtra(User.ORDER_ID, detail.getOrderId());

                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetails == null ? 0 : mDetails.size();
    }

    static class StatementViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView coinName;
        private TextView coinAmount;

        private TextView currencyName;
        private TextView currencyAmount;

        private TextView statementTime;
        private TextView statementType;

        StatementViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            coinName = itemView.findViewById(R.id.item_transaction_statement_cointype);
            coinAmount = itemView.findViewById(R.id.item_transaction_statement_coin_amount);
            statementTime = itemView.findViewById(R.id.item_transaction_statement_time);
            statementType = itemView.findViewById(R.id.item_transaction_statement_type);
            currencyName = itemView.findViewById(R.id.item_transaction_statement_currencytype);
            currencyAmount = itemView.findViewById(R.id.item_transaction_statement_currency_amount);
        }
    }
}
