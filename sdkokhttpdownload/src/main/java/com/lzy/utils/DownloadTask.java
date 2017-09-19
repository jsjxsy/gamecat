package com.lzy.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.utils.MD5Util;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import net.wequick.small.Small;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhan on 17-2-27.
 *
 * 下载任务
 */
public class DownloadTask {
    /**记录下载任务，为网络恢复时继续任务准备 */
    public static List<DownloadTask> list = new ArrayList<DownloadTask>();
    /**记录本次任务每一个包的更新状态 */
    public static Map<String,Boolean> map = new HashMap<String,Boolean>();

    public DownloadEntity getDownloadEntity() {
        return downloadEntity;
    }
    private Context mContext;

    private DownloadEntity downloadEntity;

    public DownloadTask(DownloadEntity downloadEntity,Context context) {
        this.downloadEntity = downloadEntity;
        this.mContext = context;
        list.add(this);
    }

    public Context getmContext() {
        return mContext;
    }

    /**
     * @param isRetry 是否是重新下载
     */
    public void downloadFile(boolean isRetry) {
        String url = downloadEntity.getUrl();
        String fileName = downloadEntity.getFileName();
        String filePath = downloadEntity.getFilePath();

        DownloadManager downloadManager = DownloadService.getDownloadManager();
        if (isRetry) {
            downloadManager.removeTask(url, true);
        }
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(url);
        GetRequest request;
        if (downloadInfo == null) {
            request = new GetRequest(url);
            downloadManager.addTask(fileName, url, request, filePath, new MyDownloadListener(downloadEntity, this));
        } else {
            switch (downloadInfo.getState()) {
                case DownloadManager.PAUSE:
                case DownloadManager.NONE:
                case DownloadManager.ERROR:
                    Log.e("zyy", "restart");
                    request = new GetRequest(url);
                    downloadManager.addTask(fileName, url, request, filePath, new MyDownloadListener(downloadEntity, this));
                    break;
                case DownloadManager.DOWNLOADING:
                    Log.e("zyy", "DOWNLOADING");
                    break;
                case DownloadManager.FINISH:
                    Log.e("zyy", "FINISH");
                    String folder = downloadInfo.getTargetFolder();
                    File file = new File(folder + downloadInfo.getFileName());
                    try {
                        String md5 = MD5Util.getFileMD5String(file);
                        Log.e("zyy", "md5:" + md5);
                        if (md5 != null && md5.equalsIgnoreCase(downloadEntity.getMd5())) {//文件校验成功
                            Log.e("zyy", "校验成功" + downloadEntity.getRetrycount());
                            map.put(downloadEntity.getUrl(),true);
                            AppCacheSharedPreferences.putCacheBoolean(downloadEntity.getUrl(), true);
                            downloadEntity.getToUpdateInter().toUpdate(downloadEntity.getBundleNam());
                        } else {
                            Log.e("zyy", "校验失败" + downloadEntity.getRetrycount());
                            sendErrormsg(downloadEntity,"文件校验失败");
                            map.put(downloadEntity.getUrl(),false);
                            if (downloadEntity.getRetrycount() < 2) {
                                retryDownload(downloadEntity);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("zyy", "校验失败" + downloadEntity.getRetrycount());
                        sendErrormsg(downloadEntity,"文件校验失败  "+e.toString());
                        map.put(downloadEntity.getUrl(),false);
                        if (downloadEntity.getRetrycount() < 2) {
                            retryDownload(downloadEntity);
                        }
                    }
                    break;
            }
        }
    }

    private void retryDownload(DownloadEntity downloadEntity) {
        list.remove(this);
        AppCacheSharedPreferences.putCacheBoolean(downloadEntity.getUrl(), false);
        downloadEntity.setRetrycount(downloadEntity.getRetrycount() + 1);
        DownloadTask downloadTask = new DownloadTask(downloadEntity,mContext);
        downloadTask.downloadFile(true);
    }

    private void sendErrormsg(DownloadEntity downloadEntity,String msg){
        Intent intent = Small.getIntentOfUri("main?method=download", mContext);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("errorMsg",msg);
        map.put("time",""+System.currentTimeMillis());
        map.put("currentVersion",downloadEntity.getOldVersion());
        map.put("version",downloadEntity.getVersion());
        intent.putExtra("data",map);
        intent.putExtra("type",3);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
