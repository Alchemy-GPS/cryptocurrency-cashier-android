package com.achpay.wallet.mvp.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.achpay.progresshub.KProgressHUD;
import com.achpay.wallet.R;
import com.achpay.wallet.base.view.BaseFragmentActivity;
import com.achpay.wallet.model.UpdateAppResponse;
import com.achpay.wallet.model.event.RedownloadEvent;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.utils.AppUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.achpay.wallet.view.AppDownloadProgressDialog;
import com.achpay.wallet.view.AppUpdateDialog;
import com.achpay.wallet.widget.CtrlScrollViewPager;
import com.achpay.wallet.widget.tabs.AlphaTabsIndicator;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends BaseFragmentActivity<MainPresenter> implements MainView {
    private AlphaTabsIndicator alphaTabsIndicator;
    private CtrlScrollViewPager mViewPger;
    private KProgressHUD progressHUD;
    private String downloadUrl;
    private AppUpdateDialog mUpdateDialog;
    private AppDownloadProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBusUtil.register(this);

        mViewPger = findViewById(R.id.main_viewpager);
        alphaTabsIndicator = findViewById(R.id.main_alphaIndicator);
//        直接跳转到币种选择界面,不在使用此方法
//        MvpPre.checkLoginInfo(this);
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MvpPre.checkService(this);
    }

    @Override
    protected MainPresenter bindPresenter() {
        return new MainPresenter(this);
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
                MvpPre.download(downloadUrl, MainActivity.this);
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

    @Override
    public void showPoint(boolean show) {
        if (show) {
            alphaTabsIndicator.getTabView(0).showPoint();
        } else {
            alphaTabsIndicator.getTabView(0).removeShow();
        }
    }

    @Override
    public void initViewPager() {
        //登录后再检查更新
        MvpPre.checkAppUpdate(this);

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPger.setAdapter(mainAdapter);
        mViewPger.setScroll(false);
        mViewPger.setOffscreenPageLimit(3);
        mViewPger.addOnPageChangeListener(mainAdapter);
        alphaTabsIndicator.setViewPager(mViewPger);

        AndPermission.with(getSelfActivity())
                .runtime()
                .permission(Permission.READ_PHONE_STATE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasPermissions(getSelfActivity(), Permission.READ_PHONE_STATE)) {
                            String deviceId = AppUtil.getDeviceId(MainActivity.this);
                            SharedPreferenceUtil.getPref(MainActivity.this).putStringValue(User.DEVICE_ID, deviceId);
                        } else {
                            //无权限
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //无权限
                    }
                })
                .start();
    }

    @Override
    public void showLoading() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setSize(125, 125)
                .setLabel(getString(R.string.progress_logining))
                .setCancellable(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressHUD.dismiss();
                        MainActivity.this.finish();
                    }
                });
        progressHUD.show();
    }

    @Override
    public void dismiss() {
        if (progressHUD != null && progressHUD.isShowing()) {
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

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void needRedownload(RedownloadEvent event) {
        if (event != null && event.isRedownload()) {
            MvpPre.download(downloadUrl, MainActivity.this);
            showDownloadProgress();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
