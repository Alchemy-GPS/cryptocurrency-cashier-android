package com.achpay.wallet.mvp.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.achpay.wallet.R;

import java.util.List;

public class SettingLanguageAdapter extends RecyclerView.Adapter {

    private List<String> languages;

    private int selected;

    public SettingLanguageAdapter(List<String> languages) {
        this.languages = languages;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof LanguageViewHolder){
            final LanguageViewHolder viewHolder = (LanguageViewHolder) holder;

            if (position == languages.size() - 1) {
                viewHolder.mGap.setVisibility(View.GONE);
            }

            String name = languages.get(position);
            viewHolder.mLanguage.setText(name);

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
        return languages.size();
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder{
        TextView mLanguage;
        CheckBox mSelected;
        View mGap;

        public LanguageViewHolder(View itemView) {
            super(itemView);
            mLanguage = itemView.findViewById(R.id.item_setting_language_tv);
            mSelected = itemView.findViewById(R.id.item_setting_language_cb);
            mGap = itemView.findViewById(R.id.item_setting_language_bottom);
        }
    }
}
