package com.achpay.wallet.mvp.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.achpay.wallet.R;

import java.util.List;

public class SettingNetworkAdapter extends RecyclerView.Adapter {

    private List<String> networks;

    private int selected;

    public SettingNetworkAdapter(List<String> networks) {
        this.networks = networks;
    }

    public void setSelection(int position) {
        this.selected = position;
        notifyDataSetChanged();
    }

    public int getSelection() {
        return selected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_network, parent, false);
        return new SettingNetworkAdapter.NetworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SettingNetworkAdapter.NetworkViewHolder) {
            final SettingNetworkAdapter.NetworkViewHolder viewHolder = (SettingNetworkAdapter.NetworkViewHolder) holder;

            if (position == networks.size() - 1) {
                viewHolder.mGap.setVisibility(View.GONE);
            }

            String name = networks.get(position);

            viewHolder.mNetwork.setText(name);

            if (selected == position) {
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
        return networks.size();
    }

    private SettingNetworkAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(SettingNetworkAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    class NetworkViewHolder extends RecyclerView.ViewHolder {
        TextView mNetwork;
        CheckBox mSelected;
        View mGap;

        public NetworkViewHolder(View itemView) {
            super(itemView);
            mNetwork = itemView.findViewById(R.id.item_setting_network_tv);
            mSelected = itemView.findViewById(R.id.item_setting_network_cb);
            mGap = itemView.findViewById(R.id.item_setting_network_bottom);
        }
    }
}