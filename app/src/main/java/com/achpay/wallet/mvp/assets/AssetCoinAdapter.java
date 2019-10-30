package com.achpay.wallet.mvp.assets;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.bumptech.glide.Glide;
import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.database.dbmodel.CryptocurrencyManger;
import com.achpay.wallet.model.UserInfoResponse;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.history.HistoryActivity;
import com.achpay.wallet.utils.CommonUtil;

import java.util.List;

public class AssetCoinAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserInfoResponse.AccountInfos> mList;

    public AssetCoinAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setDatas(List<UserInfoResponse.AccountInfos> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mine_coin, parent, false);
        return new CoinTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        final UserInfoResponse.AccountInfos account = mList.get(adapterPosition);
        if (holder instanceof CoinTypeViewHolder) {
            CoinTypeViewHolder itemHolder = (CoinTypeViewHolder) holder;

            if (position == mList.size() - 1) {
                itemHolder.bottomLine.setVisibility(View.GONE);
            }
            String coinId = String.valueOf(account.getCryptocurrencyId());

            Cryptocurrency cryptocurrency = CryptocurrencyManger.getInstance().queryCryptocurrencyById(coinId);

            String coinName = account.getCryptocurrency();

            String logoUrl = cryptocurrency.getListlogoUrl();

            Glide.with(mContext).load(Constants.APP_HOST + logoUrl).into(itemHolder.coinIcon);

            itemHolder.coinName.setText(coinName);

            String coinAmount = account.getAvailableBalance()
                    + mContext.getString(R.string.empty_space_one)
                    + account.getCryptocurrency();

            itemHolder.coinAmount.setText(coinAmount);

            String currencyAmount = mContext.getString(R.string.empty_space)
                    + account.getCurrencyAmount()
                    + mContext.getString(R.string.empty_space_one)
                    + CommonUtil.getCurrencyType(mContext);

            itemHolder.currencyAmount.setText(currencyAmount);

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

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    private static class CoinTypeViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        ImageView coinIcon;
        TextView coinName;
        TextView coinAmount;
        TextView currencyAmount;
        View bottomLine;

        CoinTypeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            coinIcon = itemView.findViewById(R.id.item_coinamount_icon);
            coinName = itemView.findViewById(R.id.item_coinamount_name);
            coinAmount = itemView.findViewById(R.id.item_coinamount);
            currencyAmount = itemView.findViewById(R.id.item_coinamount_money);
            bottomLine = itemView.findViewById(R.id.item_coinamount_bottomline);
        }
    }
}
