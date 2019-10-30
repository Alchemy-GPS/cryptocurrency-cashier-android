package com.achpay.wallet.receiver;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.achpay.wallet.model.params.User;
import com.achpay.wallet.utils.CommonUtil;
import com.achpay.wallet.utils.SharedPreferenceUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.List;

/**
 * Created by dawnton on 2017/1/16.
 */

public class DownloadCompleteReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long downloadId = SharedPreferenceUtil.getPref(context).getLong(User.DOWNLOAD_ID, 0);
        String currentApkMD5 = SharedPreferenceUtil.getPref(context).getStringValue(User.APK_MD5_VALUE);

        if (downloadId == id) {
            SharedPreferenceUtil.getPref(context).putLongValue(User.DOWNLOAD_ID, 0);

            final File apk = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ACHPAY.apk");

            String downloadApkMD5 = CommonUtil.MD5File(apk);

            if (currentApkMD5.equals(downloadApkMD5)) {
                if (Build.VERSION.SDK_INT >= 26) {
                    boolean canInstall = context.getPackageManager().canRequestPackageInstalls();
                    if (canInstall) {
                        installAPK(mContext,apk);
                    } else {
                        //请求安装未知应用来源的权限
                        AndPermission.with(context)
                                .runtime()
                                .permission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                                .onGranted(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        installAPK(mContext,apk);
                                    }
                                })
                                .onDenied(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        installAPK(mContext,apk);
                                    }
                                })
                                .start();
                    }
                } else {
                    installAPK(mContext,apk);
                }
            }
        }
    }

    private void installAPK(Context mContext,File apk) {
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
