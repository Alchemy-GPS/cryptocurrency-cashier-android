package com.achpay.wallet.mvp.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.achpay.wallet.R;

import java.util.List;

public class SettingCurrencyAdapter extends RecyclerView.Adapter {
    private List<String> currencys;

    private int selected = 0;

    public SettingCurrencyAdapter(List<String> currencys) {
        this.currencys = currencys;
    }

    public void setSelection(int position){
        this.selected = position;
        notifyDataSetChanged();
    }

    public int getSelection() {
        return selected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_currency, parent, false);

        return new SettingCurrencyAdapter.CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SettingCurrencyAdapter.CurrencyViewHolder){
            final SettingCurrencyAdapter.CurrencyViewHolder viewHolder = (SettingCurrencyAdapter.CurrencyViewHolder) holder;

            if (position == currencys.size() - 1) {
                viewHolder.mGap.setVisibility(View.GONE);
            }

            String name = currencys.get(position);
            viewHolder.mCurrency.setText(name);

            if(selected == position){
                viewHolder.mSelected.setChecked(true);
            } else {
                viewHolder.mSelected.setChecked(false);
            }

            if (mOnItemClickLitener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return currencys.size();
    }

    private SettingCurrencyAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(SettingCurrencyAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder{
        TextView mCurrency;
        CheckBox mSelected;
        View mGap;

        public CurrencyViewHolder(View itemView) {
            super(itemView);
            mCurrency = itemView.findViewById(R.id.item_setting_currency_tv);
            mSelected = itemView.findViewById(R.id.item_setting_currency_cb);
            mGap = itemView.findViewById(R.id.item_setting_currency_bottom);
        }
    }
}
