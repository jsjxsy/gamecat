package com.youximao.sdk.lib.common.common.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.lib.common.common.ChannelUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.model.UniqueId;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by yulinsheng on 16-8-15.
 * UniqueId分环境保存，获取
 */
public class AppConfig {

    private static AppConfig appConfig;
    private String catDeviceCode1, catDeviceCode2, catDeviceCode3;
    private Context mContext;

    private AppConfig(Context ctx) {
        this.mContext = ctx;
    }

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig(context);
        }
        return appConfig;
    }

    private File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }

    private void setSDProps(Properties p) {
        FileOutputStream fos = null;
        try {
            File dirConf = createFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "GameCat/sid", "sid");
            fos = new FileOutputStream(dirConf);
            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public String getSDByKey(String key) {
        Properties props = getSD();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties getSD() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            File dirConf = createFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "GameCat/sid", "sid");
            fis = new FileInputStream(dirConf.getPath());
            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    public void setSDByKey(String key, String value) {
        Properties props = getSD();
        props.setProperty(key, value);
        setSDProps(props);
    }

    /**
     * 获取手机sid
     */

    public void getMobileSid(final GameCatSDKListener listener, Context context) {
        if (!isLocalMobileSId()) {
            mobileSid(listener, context);
        }
    }


    /**
     * 是否本地存储SID
     *
     * @return
     */
    public boolean isLocalMobileSId() {
        if (TextUtils.isEmpty(getDeviceCode1()) ||
                TextUtils.isEmpty(getDeviceCode2()) ||
                TextUtils.isEmpty(getDeviceCode3())) {
            return false;
        } else {
            return true;
        }
    }

    public void mobileSid(final GameCatSDKListener listener, final Context context) {
        DomainApi.getUniqueToken(ChannelUtil.getChannel(mContext), getDeviceCode3(), getDeviceCode2(), getDeviceCode1(), new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (code.equals("000")) {
                        String data = message.getString("data");
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
                        UniqueId uniqueId = JSON.parseObject(content, UniqueId.class);
                        saveEquid(uniqueId.getUniqueId(), mContext);
                        listener.onSuccess(message);
                    } else {
                        String content = message.getString("message");
                        listener.onFail(content);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFail("接口初始化信息失败！");
                }
            }

            @Override
            public void onFail(String message) {
                listener.onFail(message);
            }
        }, context);
    }


    //保存sid
    public void saveEquid(String equid, Context context) {
        switch (Config.getEnvironment()) {
            //联调环境
            case 0:
                setCatDeviceCode1("COMBINED_SDK_DEVICE_CODE", equid);
                setCatDeviceCode2("combinedSdkUniqueId", equid);
                setCatDeviceCode3(context, "combinedSdkUniqueId", equid);
                break;
            //正式环境
            case 1:
                setCatDeviceCode1("RELEASE_SDK_DEVICE_CODE", equid);
                setCatDeviceCode2("releaseSdkUniqueId", equid);
                setCatDeviceCode3(context, "releaseSdkUniqueId", equid);
                break;
            //测试环境
            case 2:
                setCatDeviceCode1("DEBUG_SDK_DEVICE_CODE", equid);
                setCatDeviceCode2("debugSdkUniqueId", equid);
                setCatDeviceCode3(context, "debugSdkUniqueId", equid);
                break;
            //预发布
            case 3:
                setCatDeviceCode1("PRE_SDK_DEVICE_CODE", equid);
                setCatDeviceCode2("preSdkUniqueId", equid);
                setCatDeviceCode3(context, "preSdkUniqueId", equid);
                break;
            //开发环境
            case 4:
                setCatDeviceCode1("DEV_SDK_DEVICE_CODE", equid);
                setCatDeviceCode2("devSdkUniqueId", equid);
                setCatDeviceCode3(context, "devSdkUniqueId", equid);
                break;
        }
    }


    private void setCatDeviceCode1(String key, String value) {
        if (Build.VERSION.SDK_INT < 23) {
            Settings.System.putString(mContext.getContentResolver(), key, value);
        }
    }

    private void setCatDeviceCode2(String key, String value) {
        AppCacheSharedPreferences.putCacheString(key, value);
    }

    private void setCatDeviceCode3(Context context, String key, String value) {
        if (checkPermission(context)) {
            setSDByKey(key, value);
        }
    }

    public String getDeviceCode1() {
        if (TextUtils.isEmpty(catDeviceCode1) && Build.VERSION.SDK_INT < 23) {
            switch (Config.getEnvironment()) {
                //联调环境
                case 0:
                    catDeviceCode1 = Settings.System.getString(mContext.getContentResolver(), "COMBINED_SDK_DEVICE_CODE");
                    break;
                //正式环境
                case 1:
                    catDeviceCode1 = Settings.System.getString(mContext.getContentResolver(), "RELEASE_SDK_DEVICE_CODE");
                    break;
                //测试环境
                case 2:
                    catDeviceCode1 = Settings.System.getString(mContext.getContentResolver(), "DEBUG_SDK_DEVICE_CODE");
                    break;
                //预发布
                case 3:
                    catDeviceCode1 = Settings.System.getString(mContext.getContentResolver(), "PRE_SDK_DEVICE_CODE");
                    break;
                //开发环境
                case 4:
                    catDeviceCode1 = Settings.System.getString(mContext.getContentResolver(), "DEV_SDK_DEVICE_CODE");
                    break;
            }
        }
        if (catDeviceCode1 == null) {
            catDeviceCode1 = "";
        }
        return catDeviceCode1;
    }

    public String getDeviceCode2() {
        if (TextUtils.isEmpty(catDeviceCode2)) {
            switch (Config.getEnvironment()) {
                //联调环境
                case 0:
                    catDeviceCode2 = AppCacheSharedPreferences.getCacheString("combinedSdkUniqueId");
                    break;
                //正式环境
                case 1:
                    catDeviceCode2 = AppCacheSharedPreferences.getCacheString("releaseSdkUniqueId");
                    break;
                //测试环境
                case 2:
                    catDeviceCode2 = AppCacheSharedPreferences.getCacheString("debugSdkUniqueId");
                    break;
                //预发布
                case 3:
                    catDeviceCode2 = AppCacheSharedPreferences.getCacheString("preSdkUniqueId");
                    break;
                //开发环境
                case 4:
                    catDeviceCode2 = AppCacheSharedPreferences.getCacheString("devSdkUniqueId");
                    break;
            }
        }
        if (catDeviceCode2 == null) {
            catDeviceCode2 = "";
        }
        return catDeviceCode2;
    }

    public String getDeviceCode3() {
        if (TextUtils.isEmpty(catDeviceCode3)) {
            switch (Config.getEnvironment()) {
                //联调环境
                case 0:
                    catDeviceCode3 = getSDByKey("combinedSdkUniqueId");
                    break;
                //正式环境
                case 1:
                    catDeviceCode3 = getSDByKey("releaseSdkUniqueId");
                    break;
                //测试环境
                case 2:
                    catDeviceCode3 = getSDByKey("debugSdkUniqueId");
                    break;
                //预发布
                case 3:
                    catDeviceCode3 = getSDByKey("preSdkUniqueId");
                    break;
                //开发环境
                case 4:
                    catDeviceCode3 = getSDByKey("devSdkUniqueId");
                    break;
            }
        }
        if (catDeviceCode3 == null) {
            catDeviceCode3 = "";
        }
        return catDeviceCode3;
    }


    public static boolean checkPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }
}
