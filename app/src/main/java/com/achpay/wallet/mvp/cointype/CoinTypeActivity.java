package com.achpay.wallet.mvp.cointype;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseFragmentActivity;
import com.achpay.wallet.model.UpdateAppResponse;
import com.achpay.wallet.model.event.RedownloadEvent;
import com.achpay.wallet.model.params.TransParams;
import com.achpay.wallet.mvp.cointype.pages.CoinFragmentPresenter;
import com.achpay.wallet.mvp.cointype.pages.CoinFragmentView;
import com.achpay.wallet.mvp.main.MainActivity;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.view.AppDownloadProgressDialog;
import com.achpay.wallet.view.AppUpdateDialog;
import com.achpay.wallet.widget.tabs.SlidingTabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CoinTypeActivity extends BaseFragmentActivity<CoinTypePresenter> implements CoinTypeView, View.OnClickListener {

    private String amount;
    private String currencyId;
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private KProgressHUD progressHUD;
    private String downloadUrl;
    private AppDownloadProgressDialog mProgressDialog;
    private AppUpdateDialog mUpdateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_type);

        RelativeLayout mTitleBack = findViewById(R.id.left_titlebar_back);
        TextView mTitle = findViewById(R.id.left_titlebar_text);
        mTitle.setText(getString(R.string.checkout_select_coin));
        mTitleBack.setOnClickListener(this);

        mTabLayout = findViewById(R.id.cointype_tablayout);

        mViewPager = findViewById(R.id.cointype_viewpager);

        EventBusUtil.register(this);

        MvpPre.checkLoginInfo(this);
    }

    @Override
    protected CoinTypePresenter bindPresenter() {
        return new CoinTypePresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MvpPre.checkService(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left_titlebar_back) {
            this.finish();
        }
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab_layout, null);
        TextView textView = view.findViewById(R.id.tab_item_title);
        textView.setText(TransParams.COINTYPE_TAB_TITLES[position]);
        if (position == 0) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textView.setTextColor(getResources().getColor(R.color.white));
        }
        return view;
    }

    @Override
    public void initViewPager() {
        mTabLayout.setVisibility(View.VISIBLE);

        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), amount, currencyId);

        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabLayout.redrawIndicator(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view) {
                    TextView textView = view.findViewById(R.id.tab_item_title);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    textView.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view) {
                    TextView textView = view.findViewById(R.id.tab_item_title);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    textView.setTextColor(getResources().getColor(R.color.grey_aa));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
    }

    @Override
    public void showLoading() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setSize(100, 100)
                    .setCancellable(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            progressHUD.dismiss();
                            CoinTypeActivity.this.finish();
                        }
                    });
        }

        if (!progressHUD.isShowing()) {
            progressHUD.show();
        }
    }

    @Override
    public void dismiss() {
        if (progressHUD != null && progressHUD.isShowing()) {
            Log.i("dismiss");

            progressHUD.dismiss();
        }
    }

    @Override
    public void loginSuccess() {
        dismiss();
        initViewPager();
    }

    @Override
    public void loginFail() {
        dismiss();
        Toast.makeText(this, getString(R.string.login_login_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCurrency(String currencyId){
        this.currencyId = currencyId;
    }

    @Override
    public void setAmount(String amount){
        this.amount = amount;
    }

    @Override
    public void showUpdate(UpdateAppResponse response) {
        downloadUrl = response.getUrl();
        if (mUpdateDialog == null) {
            mUpdateDialog = new AppUpdateDialog(this);
        }
        mUpdateDialog.setUpdateInfo(response);
        mUpdateDialog.setUpdatekListener(new AppUpdateDialog.OnUpdatekClickListener() {
            @Override
            public void onUpdatekClick() {
                MvpPre.download(downloadUrl, CoinTypeActivity.this);
                showDownloadProgress();
            }
        });
        mUpdateDialog.show();
    }

    @Override
    public void showDownloadProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new AppDownloadProgressDialog(this);
        }
        mProgressDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void needRedownload(RedownloadEvent event) {
        if (event != null && event.isRedownload()) {
            MvpPre.download(downloadUrl, CoinTypeActivity.this);
            showDownloadProgress();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
