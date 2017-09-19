package com.youximao.sdk.gamecatsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.utils.AppCacheSharedPreferences;
import com.lzy.utils.DownloadEntity;
import com.lzy.utils.DownloadTask;
import com.lzy.utils.ToUpdateInter;

import net.wequick.small.Small;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Manco on 2016/8/25.
 */
public class UpgradeManager {

    private static class UpdateInfo {
        public String packageName;
        public String downloadUrl;
        public String md5;
    }

    private static class UpgradeInfo {
        public JSONObject manifest;
        public List<UpdateInfo> updates;
    }

    private interface OnResponseListener {
        void onResponse(UpgradeInfo info);
    }

    private interface OnUpgradeListener {
        void onUpgrade(boolean succeed);
    }

    private static class ResponseHandler extends Handler {
        private OnResponseListener mListener;

        public ResponseHandler(OnResponseListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mListener.onResponse((UpgradeInfo) msg.obj);
                    break;
            }
        }
    }

    private ResponseHandler mResponseHandler;

    private Context mContext;
    private static String mVersion;

    public UpgradeManager(Context context) {
        mContext = context;
    }

    public void checkUpgrade() {
        requestUpgradeInfo(Small.getBundleVersions(), new OnResponseListener() {
            @Override
            public void onResponse(UpgradeInfo info) {
                upgradeBundles(info,
                        new OnUpgradeListener() {
                            @Override
                            public void onUpgrade(boolean succeed) {
                                String text = succeed ?
                                        "Upgrade Success! Switch to background and back to foreground to see changes."
                                        : "Upgrade Failed!";
                                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    /**
     * @param versions
     * @param listener
     */
    private void requestUpgradeInfo(Map versions, OnResponseListener listener) {
        System.out.println(versions); // this should be passed as HTTP parameters
        mResponseHandler = new ResponseHandler(listener);
        GameCatSDK.update(mContext, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (!code.equals("000")) {
                        return;
                    }
                    JSONObject data = message.getJSONObject("data");
                    JSONObject manifest = null;
                    JSONArray updates = null;
                    if (data != null) {
                        mVersion = data.getString("version");
                        String oldVersion = AppCacheSharedPreferences.getCacheString("Version");
                        String currentTaskVersion = AppCacheSharedPreferences.getCacheString("currentTaskVersion");
                        if (!TextUtils.equals(mVersion,currentTaskVersion)){
                            DownloadManager downloadManager = DownloadService.getDownloadManager();
                            downloadManager.removeAllTask();
                        }
                        manifest = data.getJSONObject("manifest");
                        updates = data.getJSONArray("updates");

                    }
                    if (null == updates) return;
                    int N = updates.length();
                    List<UpdateInfo> infos = new ArrayList<>(N);
                    for (int i = 0; i < N; i++) {
                        JSONObject o = updates.getJSONObject(i);
                        UpdateInfo info = new UpdateInfo();
                        info.packageName = o.getString("pkg");
                        info.downloadUrl = o.getString("url");
                        info.md5 = o.getString("md5");
                        infos.add(info);
                    }

                    // Post message
                    UpgradeInfo ui = new UpgradeInfo();
                    ui.manifest = manifest;
                    ui.updates = infos;
                    Message.obtain(mResponseHandler, 1, ui).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
            }
        });
    }

    private static class DownloadHandler extends Handler {
        private OnUpgradeListener mListener;

        public DownloadHandler(OnUpgradeListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mListener.onUpgrade((Boolean) msg.obj);
                    break;
            }
        }
    }


    private DownloadHandler mHandler;
    private boolean isUpdata;

    private void upgradeBundles(final UpgradeInfo info,
                                final OnUpgradeListener listener) {
        // Just for example, you can do this by OkHttp or something.
        mHandler = new DownloadHandler(listener);


        // Update manifest
        if (info.manifest != null) {
            if (!Small.updateManifest(info.manifest, false)) {
                Message.obtain(mHandler, 1, false).sendToTarget();
                return;
            }
        }
        // Download bundles
        List<UpdateInfo> updates = info.updates;
        for (UpdateInfo u : updates) {
            DownloadTask.map.put(u.downloadUrl, false);
        }
        for (UpdateInfo u : updates) {
            // Get the patch file for downloading
            net.wequick.small.Bundle bundle = Small.getBundle(u.packageName);
            if (u != null){
                isUpdata = true;
            }
            if (bundle == null) {
                continue;

            }
            File file = bundle.getPatchFile();
            downloadFile(u.downloadUrl, file.getName(), file.getParent(), u.packageName, u.md5);
        }
        if (isUpdata){
            Intent intent = Small.getIntentOfUri("main?method=download", mContext);
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("time",""+System.currentTimeMillis());
            String oldVersion = AppCacheSharedPreferences.getCacheString("Version");
            if (TextUtils.isEmpty(oldVersion)){
                oldVersion = "2.0.0";
            }
            map.put("currentVersion",oldVersion);
            map.put("version",mVersion);
            intent.putExtra("data",map);
            intent.putExtra("type",1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

        Message.obtain(mHandler, 1, true).sendToTarget();
    }

    private void downloadFile(String url, String fileName, String filePath, String bundleNam, String md5) {
        if (!TextUtils.isEmpty(mVersion)){
            AppCacheSharedPreferences.putCacheString("currentTaskVersion",mVersion);
        }
        DownloadEntity downloadEntity = new DownloadEntity();
        downloadEntity.setUrl(url);
        downloadEntity.setFileName(fileName);
        downloadEntity.setFilePath(filePath + File.separator);
        downloadEntity.setBundleNam(bundleNam);
        downloadEntity.setMd5(md5);
        downloadEntity.setVersion(mVersion);
        String oldVersion = AppCacheSharedPreferences.getCacheString("Version");
        if (TextUtils.isEmpty(oldVersion)){
            oldVersion = "2.0.0";
        }
        downloadEntity.setOldVersion(oldVersion);
        downloadEntity.setToUpdateInter(new ToUpdateInter() {
            @Override
            public void toUpdate(String bundleNam) {

                for (Boolean b : DownloadTask.map.values()) {
                    if (!b) {
                        return;
                    }
                }
                for (DownloadTask downloadTask : DownloadTask.list) {
                    net.wequick.small.Bundle bundle = Small.getBundle(downloadTask.getDownloadEntity().getBundleNam());
                    if (bundle != null) {
                        bundle.upgrade();
                    }
                }

                Intent intent = Small.getIntentOfUri("main?method=download", mContext);
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("time",""+System.currentTimeMillis());
                String oldVersion = AppCacheSharedPreferences.getCacheString("Version");
                if (TextUtils.isEmpty(oldVersion)){
                    oldVersion = "2.0.0";
                }
                map.put("currentVersion",oldVersion);
                map.put("version",mVersion);
                intent.putExtra("data",map);
                intent.putExtra("type",2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

                AppCacheSharedPreferences.putCacheString("Version", mVersion);
            }
        });
        DownloadTask downloadTask = new DownloadTask(downloadEntity,mContext);
        downloadTask.downloadFile(false);
    }
}
