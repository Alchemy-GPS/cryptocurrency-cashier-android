package com.achpay.wallet.mvp.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.achpay.wallet.R;
import com.achpay.wallet.WelcomeActivity;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.mvp.main.MainActivity;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingLanguageActivity extends Activity implements View.OnClickListener {

    private SettingLanguageAdapter mSettingLanguageAdapter;
    private List<String> languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_language);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.language_setting_title));
        mTitleBack.setOnClickListener(this);

        RecyclerView mRecyclerView = findViewById(R.id.language_setting_rv);
        Button mConfirm = findViewById(R.id.language_setting_confirm);
        mConfirm.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] language = getResources().getStringArray(R.array.language);

        languages = Arrays.asList(language);

        mSettingLanguageAdapter = new SettingLanguageAdapter(languages);

        int selected = SharedPreferenceUtil.getPref(this).getInt(User.SETTING_LANGUAGE_SELECTION, 0);

        mSettingLanguageAdapter.setSelection(selected);

        mSettingLanguageAdapter.setOnItemClickLitener(new SettingLanguageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mSettingLanguageAdapter.setSelection(position);
            }
        });

        mRecyclerView.setAdapter(mSettingLanguageAdapter);
    }

    private void settingLanguage() {

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
            case R.id.language_setting_confirm:
                int selected = mSettingLanguageAdapter.getSelection();
                String language = languages.get(selected);
                SharedPreferenceUtil.getPref(this).putStringValue(User.SETTING_LANGUAGE, language);
                SharedPreferenceUtil.getPref(this).putIntValue(User.SETTING_LANGUAGE_SELECTION, selected);
                settingLanguage();
                Log.i("App语言为" + language);
                this.finish();
                break;
        }
    }
}
