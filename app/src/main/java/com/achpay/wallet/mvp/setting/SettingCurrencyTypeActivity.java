package com.achpay.wallet.mvp.setting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.model.params.ResponseParam;
import com.achpay.wallet.model.event.SettingCurrencyEvent;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingCurrencyTypeActivity extends Activity implements View.OnClickListener {
    private SettingCurrencyAdapter mSettingCurrencyAdapter;
    private List<String> currencys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_currency);
        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.currency_setting_title));
        mTitleBack.setOnClickListener(this);

        RecyclerView mRecyclerView = findViewById(R.id.setting_currency_rv);
        Button mConfirm = findViewById(R.id.currency_setting_confirm);
        mConfirm.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] currency = getResources().getStringArray(R.array.currency);

        currencys = Arrays.asList(currency);

        mSettingCurrencyAdapter = new SettingCurrencyAdapter(currencys);


        int selected = getSelectedPosition(CommonUtil.getCurrencyId());

        mSettingCurrencyAdapter.setSelection(selected);

        mSettingCurrencyAdapter.setOnItemClickLitener(new SettingCurrencyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mSettingCurrencyAdapter.setSelection(position);
            }
        });

        mRecyclerView.setAdapter(mSettingCurrencyAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_titlebar_back:
                this.finish();
                break;
            case R.id.currency_setting_confirm:
                int selected = mSettingCurrencyAdapter.getSelection();
                String[] currencyName = getResources().getStringArray(R.array.currency_name);
                String[] currencyUnit = getResources().getStringArray(R.array.currency_unit);
                String[] currencyId = getResources().getStringArray(R.array.currency_id);
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_CURRENCY, currencyName[selected]);
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_CURRENCY_UNIT, currencyUnit[selected]);
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_CURRENCY_ID, currencyId[selected]);
                EventBusUtil.post(new SettingCurrencyEvent(ResponseParam.SUCCESS));
                this.finish();
                break;
        }
    }

    private int getSelectedPosition(String currencyId) {
        String[] currencyIds = getResources().getStringArray(R.array.currency_id);
        for (int i = 0; i < currencyIds.length; i++) {
            if (currencyId.equals(currencyIds[i])) {
                return i;
            }
        }
        return 0;
    }
}
