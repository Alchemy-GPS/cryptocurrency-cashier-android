package com.achpay.wallet.observer;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.achpay.wallet.model.event.DownloadEvent;
import com.achpay.wallet.utils.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dawnton on 2017/5/3.
 */

public class DownloadChangeObserver extends ContentObserver {
    private long downloadID;
    private Context mContext;
    private DownloadEvent downloadEvent;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public DownloadChangeObserver(Handler handler, Context mContext) {
        super(handler);
        this.mContext = mContext;
        downloadEvent = new DownloadEvent();
    }

    public void setDownloadId(long downloadID) {
        this.downloadID = downloadID;
    }

    @Override
    public void onChange(boolean selfChange) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);
        DownloadManager dManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        Cursor cursor = dManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int totalSize = cursor.getInt(totalColumn);
            int currentSize = cursor.getInt(currentColumn);
            float percent = (float) currentSize / (float) totalSize;
            int progress = Math.round(percent * 100);
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            //下载时的状态
            downloadEvent.setProgress(progress);
            downloadEvent.setStatus(status);

            Log.i("下载进度 === " + progress);
            EventBus.getDefault().post(downloadEvent);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    public int queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);
        DownloadManager dManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor = dManager.query(query);
        int status = 0;
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        }
        return status;
    }
}
