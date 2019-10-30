package com.achpay.wallet.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.achpay.wallet.R;
import com.achpay.wallet.model.event.RedownloadEvent;
import com.achpay.wallet.model.params.User;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.EventBusUtil;
import com.achpay.wallet.utils.Log;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

public class AppDownloadFinishDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView mConfirm;
    private TextView mInfo;
    private TextView mTitle;
    private boolean redownload;

    public AppDownloadFinishDialog(@NonNull Context context) {
        this(context, R.style.GlobalDialog);
    }

    public AppDownloadFinishDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private void init() {
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_app_downloaded, null);
        mConfirm = layoutView.findViewById(R.id.dialog_downloaded_confirm);
        mInfo = layoutView.findViewById(R.id.dialog_downloaded_info);
        mTitle = layoutView.findViewById(R.id.dialog_downloaded_title);
        redownload = false;
        mConfirm.setOnClickListener(this);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.setContentView(layoutView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_downloaded_confirm) {

            if (redownload) {
                EventBusUtil.post(new RedownloadEvent(true));
                return;
            }

            SharedPreferenceUtil.getPref(mContext).putLongValue(User.DOWNLOAD_ID, 0);

            String currentApkMD5 = SharedPreferenceUtil.getPref(mContext).getStringValue(User.APK_MD5_VALUE);

            final File apk = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ACHPAY.apk");

            String downloadApkMD5 = CommonUtil.MD5File(apk);


            if (currentApkMD5.equals(downloadApkMD5)) {

                if (Build.VERSION.SDK_INT >= 26) {
                    boolean canInstall = mContext.getPackageManager().canRequestPackageInstalls();
                    if (canInstall) {
                        installAPK(apk);
                    } else {
                        //请求安装未知应用来源的权限
                        AndPermission.with(mContext)
                                .runtime()
                                .permission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                                .onGranted(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        installAPK(apk);
                                    }
                                })
                                .onDenied(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        installAPK(apk);
                                    }
                                })
                                .start();
                    }
                } else {
                    installAPK(apk);
                }
            } else {
                redownload = true;
                mTitle.setText(mContext.getString(R.string.dialog_downloaded_title_error));
                mInfo.setText(mContext.getString(R.string.dialog_downloaded_info_error));
                Toast.makeText(mContext, mContext.getString(R.string.dialog_downloaded_info_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void installAPK(File apk) {
        SharedPreferenceUtil.getPref(mContext).putBooleanValue(User.FIRST_TIME, true);

        Intent install = new Intent(Intent.ACTION_VIEW);

        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= 24) {

            Uri apkUri = FileProvider.getUriForFile(mContext, User.FILE_AUTHORITY, apk);

            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            install.setDataAndType(apkUri, "application/vnd.android.package-archive");

        } else {
            install.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        }

        mContext.startActivity(install);
    }
}
