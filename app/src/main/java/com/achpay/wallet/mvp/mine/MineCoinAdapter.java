package com.achpay.wallet.mvp.mine;

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

import com.bumptech.glide.Glide;
import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.model.UserInfoResponse;
import com.achpay.wallet.mvp.assets.AssetDetailActivity;
import com.achpay.wallet.mvp.history.HistoryActivity;

import java.util.List;

public class MineCoinAdapter extends RecyclerView.Adapter<MineCoinAdapter.CoinTypeViewHolder> {
    private Context mContext;
    private List<UserInfoResponse.AccountInfos> mList;

    public MineCoinAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<UserInfoResponse.AccountInfos> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mine_assetdetail, parent, false);
        return new CoinTypeViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CoinTypeViewHolder itemHolder, int position) {
        int adapterPosition = itemHolder.getAdapterPosition();
        final UserInfoResponse.AccountInfos account = mList.get(adapterPosition);
        if (position == 10) {
            itemHolder.mMore.setVisibility(View.VISIBLE);
            itemHolder.mCommon.setVisibility(View.GONE);
            itemHolder.mMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AssetDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {
            itemHolder.mMore.setVisibility(View.GONE);

            itemHolder.mCommon.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(Constants.APP_HOST + account.getSettlelogoUrl()).into(itemHolder.coinIcon);

            itemHolder.coinName.setText(account.getCryptocurrency());

            itemHolder.coinAmount.setText(account.getAvailableBalance().substring(0, 10));

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, HistoryActivity.class);
                    intent.putExtra(TransParams.COIN_NAME, account.getCryptocurrency());
                    intent.putExtra(TransParams.COIN_ID, account.getCryptocurrencyId());
                    intent.putExtra(User.TOTAL_COIN, account.getAvailableBalance());
                    intent.putExtra(User.TOTAL_CURRENCY, account.getCurrencyAmount());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    static class CoinTypeViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        ImageView coinIcon;
        TextView coinName;
        TextView coinAmount;
        RelativeLayout mCommon;
        RelativeLayout mMore;

        CoinTypeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            coinIcon = itemView.findViewById(R.id.item_asset_coinicon);
            coinName = itemView.findViewById(R.id.item_asset_cointype);
            coinAmount = itemView.findViewById(R.id.item_asset_coinamount);
            mCommon = itemView.findViewById(R.id.item_mine_common);
            mMore = itemView.findViewById(R.id.item_mine_more);
        }
    }
}
