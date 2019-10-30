package com.achpay.wallet.mvp.cointype.pages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.database.dbmodel.Cryptocurrency;
import com.achpay.wallet.model.params.TransParams;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinTypeViewHolder> {
    private Context mContext;
    private List<Cryptocurrency> mList;
    private String title;

    public CoinAdapter(Context mContext, String title) {
        this.mContext = mContext;
        this.title = title;
    }

    public void setDatas(List<Cryptocurrency> mList) {

        List<Cryptocurrency> mCryptocurrencies = new ArrayList<>();

        if (title.equals(TransParams.COINTYPE_TAB_TITLES[0])) {

            for (Cryptocurrency cryptocurrency : mList) {
                if (!cryptocurrency.getCoinType().equals(TransParams.LIGHTNING)) {
                    mCryptocurrencies.add(cryptocurrency);
                }
            }
        } else if (title.equals(TransParams.COINTYPE_TAB_TITLES[1])) {
            for (Cryptocurrency cryptocurrency : mList) {
                if (cryptocurrency.getCoinType().equals(TransParams.LIGHTNING)) {
                    mCryptocurrencies.add(cryptocurrency);
                }
            }
        }
        this.mList = mCryptocurrencies;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinAdapter.CoinTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_checkout_cointype, parent, false);
        return new CoinTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CoinAdapter.CoinTypeViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();

        final Cryptocurrency cryptocurrency = mList.get(adapterPosition);

        holder.coinName.setText(cryptocurrency.getCryptoCurrency());
        holder.coinFullName.setText(cryptocurrency.getFullCoinName());

        Glide.with(mContext).load(Constants.APP_HOST + cryptocurrency.getListlogoUrl()).into(holder.coinIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(holder.itemView, holder.getAdapterPosition(), cryptocurrency);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private CoinAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(CoinAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, Cryptocurrency cryptocurrency);
    }

    static class CoinTypeViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView coinIcon;
        private TextView coinName;
        private TextView coinFullName;

        CoinTypeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            coinIcon = itemView.findViewById(R.id.item_cointype_icon);
            coinName = itemView.findViewById(R.id.item_cointype_name);
            coinFullName = itemView.findViewById(R.id.item_cointype_full_name);
        }
    }

}
