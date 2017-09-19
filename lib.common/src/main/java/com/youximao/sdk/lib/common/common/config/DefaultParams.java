package com.youximao.sdk.lib.common.common.config;

import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSONObject;
import com.youximao.sdk.lib.common.common.ChannelUtil;
import com.youximao.sdk.lib.common.common.network.AppConfig;
import com.youximao.sdk.lib.common.common.util.DeviceNetworkUtil;
//import com.gamecat.alianalytics.util.ChannelUtil;
//import com.gamecat.common.network.AppConfig;
//import com.gamecat.common.util.DeviceNetworkUtil;

/**
 * Created by admin on 2016/11/2.
 */

public class DefaultParams {

    private static DefaultParams mInstance;
    private JSONObject mDeviceStatusJSONObject;
    private JSONObject mDataJSONObject;
    private Context mContext;

    private DefaultParams(Context context) {
        mDeviceStatusJSONObject = new JSONObject();
        mDataJSONObject = new JSONObject();
        mContext = context;
    }

    public static DefaultParams getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DefaultParams(context);
        }
        return mInstance;
    }

    /**
     * apiVersion	调用的api接口版本号需要和服务器定的版本号一致(暂时传空)	string
     * cpu	手机cpu型号	string
     * devicesId	手机设备Id	string
     * dt	机型	string
     * imei	手机唯一码	string
     * imsi	手机卡识别码	string
     * mac	mac地址	string
     * nt	网络类型	string
     * packageName	包名	string
     * sid	平台内部设备号1	string
     * sid2	平台内部设备号2	string
     * sid3	平台内部设备号3	string
     * sv	系统版本	string
     * terminalType	终端类型1-ios，2-安卓	number
     * vc	apk版本号	string
     * vn	apk版本名	string
     */
    public JSONObject getDeviceStatusParams() {
        mDeviceStatusJSONObject.put("apiVersion", "");
        mDeviceStatusJSONObject.put("cpu", DeviceNetworkUtil.getCPUName());
        mDeviceStatusJSONObject.put("devicesId", DeviceNetworkUtil.getDeviceId(mContext));
        mDeviceStatusJSONObject.put("dt", Build.DEVICE);
        mDeviceStatusJSONObject.put("imei", DeviceNetworkUtil.getIMEI(mContext));
        mDeviceStatusJSONObject.put("imsi", DeviceNetworkUtil.getIMSI(mContext));
        mDeviceStatusJSONObject.put("mac", DeviceNetworkUtil.getMacAddress(mContext));
        mDeviceStatusJSONObject.put("nt", DeviceNetworkUtil.getNetworkType(mContext));
        mDeviceStatusJSONObject.put("packageName", DeviceNetworkUtil.getPackageName(mContext));
        mDeviceStatusJSONObject.put("appName", DeviceNetworkUtil.getAppName(mContext));
        if (Build.VERSION.SDK_INT < 23) {
            mDeviceStatusJSONObject.put("sid", AppConfig.getAppConfig(mContext).getDeviceCode1());
        } else {
            mDeviceStatusJSONObject.put("sid", AppConfig.getAppConfig(mContext).getDeviceCode2());
        }
        mDeviceStatusJSONObject.put("sid2", AppConfig.getAppConfig(mContext).getDeviceCode2());
        if (Build.VERSION.SDK_INT < 23) {
            mDeviceStatusJSONObject.put("sid3", AppConfig.getAppConfig(mContext).getDeviceCode3());
        } else {
            mDeviceStatusJSONObject.put("sid3", AppConfig.getAppConfig(mContext).getDeviceCode3());
        }
        mDeviceStatusJSONObject.put("sv", Build.VERSION.RELEASE);
        mDeviceStatusJSONObject.put("terminalType", "2");
        mDeviceStatusJSONObject.put("vc", DeviceNetworkUtil.getVersionCode(mContext));
        mDeviceStatusJSONObject.put("vn", DeviceNetworkUtil.getVersionName(mContext));
        return mDeviceStatusJSONObject;
    }

    /**
     * chlId	渠道id	string
     * gameId	游戏id	string
     * guildChlId	游戏包id	string
     *
     * @return
     */

    public JSONObject getDataParams() {
        mDataJSONObject.put("chlId", Config.getChannelId());
        mDataJSONObject.put("gameId", Config.getGameId());
        mDataJSONObject.put("guildChlId", ChannelUtil.getChannel(mContext));
        return mDataJSONObject;
    }

}
