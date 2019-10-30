package com.achpay.wallet.mvp.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.Constants;
import com.achpay.wallet.R;
import com.achpay.wallet.WelcomeActivity;
import com.achpay.wallet.model.WSClientMessage;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.network.RetrofitUtil;
import com.achpay.wallet.service.MessageService;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import java.util.Arrays;
import java.util.List;

public class SettingNetworkActivity extends Activity implements View.OnClickListener{
    private SettingNetworkAdapter mSettingNetworkAdapter;
    private List<String> networks;
    private EditText mNetWorkAddress;
    String[] network = new String[]{
            "192.168.0.102:9091/foundation",
            "54.255.232.245:9090/foundation",
            "13.250.21.97:9096/foundation-web"
    };
    private TextView mConnect;
    private TextView mDisonnect;
    private CheckBox mInputTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_network);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.setting_net_config));
        mTitleBack.setOnClickListener(this);

        RecyclerView mRecyclerView = findViewById(R.id.network_setting_rv);
        mConnect = findViewById(R.id.connect_websocket);
        mDisonnect = findViewById(R.id.disconnect_websocket);
        Button mConfirm = findViewById(R.id.network_setting_confirm);
        mNetWorkAddress = findViewById(R.id.network_setting_address);

        RelativeLayout mInputTipsLayout = findViewById(R.id.setting_network_showtips_layout);
        mInputTips = findViewById(R.id.setting_network_showtips);

        mConfirm.setOnClickListener(this);
        mConnect.setOnClickListener(this);
        mDisonnect.setOnClickListener(this);
        mInputTipsLayout.setOnClickListener(this);

        //设置网络
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        networks = Arrays.asList(network);
        mSettingNetworkAdapter = new SettingNetworkAdapter(networks);
        int selected = SharedPreferenceUtil.getPref(this).getInt(User.SETTING_NETWORK_SELECTION, 0);
        String net = SharedPreferenceUtil.getPref(this).getString(User.SETTING_NETWORK, networks.get(selected));
        mNetWorkAddress.setText(net);
        mSettingNetworkAdapter.setSelection(selected);
        mSettingNetworkAdapter.setOnItemClickLitener(new SettingNetworkAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mSettingNetworkAdapter.setSelection(position);
                String net = networks.get(position);
                mNetWorkAddress.setText(net);
            }
        });
        mRecyclerView.setAdapter(mSettingNetworkAdapter);

        //设置手否显示输入也弹窗
        boolean showTestNetTips = SharedPreferenceUtil.getPref(this).getBoolean("SHOW_TESTNET_TIPS", true);
        if (showTestNetTips) {
            mInputTips.setChecked(true);
        } else {
            mInputTips.setChecked(false);
        }
    }

    private void settingNetwork() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_titlebar_back:
                this.finish();
                break;
            case R.id.network_setting_confirm:
                int selected = mSettingNetworkAdapter.getSelection();
                String net = mNetWorkAddress.getText().toString().trim();

                Constants.IP = net;

                Constants.APP_HOST = "http://" + net + "/";

                Constants.WS_HOST = "ws://" + net + "/websocket/socketServer.do";

                RetrofitUtil.recreate();

                Intent intent = new Intent(this, MessageService.class);
                stopService(intent);

                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_NETWORK, net);
                SharedPreferenceUtil.getPref(this).putIntValue(User.SETTING_NETWORK_SELECTION, selected);
                settingNetwork();
                Log.i("App网络为 === " + net);
                this.finish();
                break;
            case R.id.connect_websocket:
                WSClientMessage connectMessage = new WSClientMessage();
                connectMessage.setMessage(TransParams.CHECK_WEBSOCKET);
                EventBusUtil.post(connectMessage);
                break;
            case R.id.disconnect_websocket:
                WSClientMessage disConnectMessage = new WSClientMessage();
                disConnectMessage.setMessage(TransParams.CLOSE_WEBSOCKET);
                EventBusUtil.post(disConnectMessage);
                break;
            case R.id.setting_network_showtips_layout:
                if (mInputTips.isChecked()) {
                    mInputTips.setChecked(false);
                    SharedPreferenceUtil.getPref(this).putBooleanValue("SHOW_TESTNET_TIPS", false);
                } else {
                    mInputTips.setChecked(true);
                    SharedPreferenceUtil.getPref(this).putBooleanValue("SHOW_TESTNET_TIPS", true);
                }
                break;
        }
    }
}
