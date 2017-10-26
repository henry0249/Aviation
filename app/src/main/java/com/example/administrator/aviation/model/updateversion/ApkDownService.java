package com.example.administrator.aviation.model.updateversion;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.administrator.aviation.model.homemessge.HomeMessage;

import java.io.File;

/**
 * 安装下载的service
 */
public class ApkDownService extends Service {

    private DownloadManager dm;
    private long downloadId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HomeMessage homeMessage = new HomeMessage();
        String apkUrl = homeMessage.getAppconfig().getAPPURL();

        // 注册广播通知
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload(apkUrl);
        return super.onStartCommand(intent, flags, startId);
    }

    // 开始下载Apk
    public void startDownload(String apkUrl) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (!com.example.administrator.aviation.model.updateversion.StringUtils.isNetUrl(apkUrl)) {
            Toast.makeText(ApkDownService.this, "不是一个正确的网址", Toast.LENGTH_SHORT).show();
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setMimeType("application/vnd.android.package-archive");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFileName(apkUrl));
        request.setDestinationInExternalPublicDir("feijichang", getFileName(apkUrl));
        downloadId = dm.enqueue(request);
    }

    // 广播
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {
                File file = new File(dm.getUriForDownloadedFile(downloadId).toString());
                installApk(file, context);
            }
            stopSelf();
        }
    };

    // 得到下载地址
    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    // 自动安装已下载Apk
    private void installApk(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
//        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(file));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 取消注册广播通知
        unregisterReceiver(receiver);
    }

}

