package com.lzy.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.utils.MD5Util;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.listener.DownloadListener;

import net.wequick.small.Small;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by zhan on 17-2-24.
 */
public class MyDownloadListener extends DownloadListener {
    private DownloadEntity downloadEntity;
    private DownloadTask downloadTask;

    public MyDownloadListener(DownloadEntity downloadEntity, DownloadTask downloadTask) {
        this.downloadEntity = downloadEntity;
        this.downloadTask = downloadTask;
    }

    @Override
    public void onProgress(DownloadInfo downloadInfo) {
        Log.e("zyy", "DownloadLength = " + downloadInfo.getDownloadLength());
        Log.e("zyy", "DownloadProgress = " + downloadInfo.getProgress());
    }

    @Override
    public void onFinish(DownloadInfo downloadInfo) {
        Log.e("zyy", "onFinish");
        String folder = downloadInfo.getTargetFolder();
        File file = new File(folder + downloadInfo.getFileName());
        try {
            String md5 = MD5Util.getFileMD5String(file);
            Log.e("zyy", "md5:" + md5);
            if (md5 != null && md5.equalsIgnoreCase(downloadEntity.getMd5())) {//文件校验成功
                Log.e("zyy", "校验成功");
                DownloadTask.map.put(downloadEntity.getUrl(),true);
                AppCacheSharedPreferences.putCacheBoolean(downloadEntity.getUrl(), true);
                downloadEntity.getToUpdateInter().toUpdate(downloadEntity.getBundleNam());
            } else {
                Log.e("zyy", "校验失败" + downloadEntity.getRetrycount());
                sendErrormsg(downloadEntity,"文件校验失败");
                DownloadTask.map.put(downloadEntity.getUrl(),false);
                if (downloadEntity.getRetrycount() < 2) {
                    retryDownload(downloadEntity, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("zyy", "校验失败" + downloadEntity.getRetrycount());
            sendErrormsg(downloadEntity,"文件校验失败"+e.toString());
            DownloadTask.map.put(downloadEntity.getUrl(),false);
            Log.e("zyy", e.toString());
            if (downloadEntity.getRetrycount() < 2) {
                retryDownload(downloadEntity, true);
            }
        }
    }

    @Override
    public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
        Log.e("zyy", "onError" + errorMsg);
        if ("网络异常".equals(errorMsg)) {
            //网络异常　下载返回错误状态　不用重置下载（也即不用移除下载任务，可以断点续传）
            sendErrormsg(downloadEntity,"网络异常");
        } else if ("文件读写异常".equals(errorMsg)) {
            if (null != OkGo.getContext() && NetWorkUtils.getNetWorkType(OkGo.getContext()) == NetWorkUtils.NETWORKTYPE_INVALID) {
                //因为网络断开造成文件读写异常　不用重置下载，网络恢复可以继续断点续传
                sendErrormsg(downloadEntity,"网络异常造成文件读写异常");
            } else {
                sendErrormsg(downloadEntity,"文件读写异常重新下载");
                //其他情况引起文件读写异常，需要重新开启下载任务
                retryDownload(downloadEntity, false);
            }
        } else {
            //其他异常引起下载任务错误，移除本次下载任务，重新开启下载
            sendErrormsg(downloadEntity,"异常导致重新下载");
            retryDownload(downloadEntity, false);
        }
    }

    private void retryDownload(DownloadEntity downloadEntity, boolean isCheck) {
        DownloadTask.list.remove(downloadTask);
        AppCacheSharedPreferences.putCacheBoolean(downloadEntity.getUrl(), false);
        if (isCheck) {
            downloadEntity.setRetrycount(downloadEntity.getRetrycount() + 1);
        }
        DownloadTask downloadTask = new DownloadTask(downloadEntity,this.downloadTask.getmContext());
        downloadTask.downloadFile(true);
    }

    private void sendErrormsg(DownloadEntity downloadEntity,String msg){
        Intent intent = Small.getIntentOfUri("main?method=download", downloadTask.getmContext());
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("errorMsg",msg);
        map.put("time",""+System.currentTimeMillis());
        map.put("currentVersion",downloadEntity.getOldVersion());
        map.put("version",downloadEntity.getVersion());
        intent.putExtra("data",map);
        intent.putExtra("type",3);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        downloadTask.getmContext().startActivity(intent);
    }
}
