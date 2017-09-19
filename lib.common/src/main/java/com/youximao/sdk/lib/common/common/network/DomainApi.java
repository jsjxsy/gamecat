package com.youximao.sdk.lib.common.common.network;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.DeviceNetworkUtil;
import com.youximao.sdk.lib.common.network.NetworkRequestsUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/2/6.
 */

public class DomainApi {
    private final static String PACKAGE_NAME = "com.youximao.fusionsdk";
    private final static String METHOD_NAME = "web_getDomainList";

    /**
     * 获取设备sid
     *
     * @param
     */
    public static void getUniqueToken(String channelId, String sdId, String sharedId, String systemId, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("channelId", channelId);
        paramsMap.put("deviceId", DeviceNetworkUtil.getDeviceId(SDKManager.getContext()));
        paramsMap.put("terminalName", Build.MANUFACTURER);
        paramsMap.put("terminalType", "2");
        paramsMap.put("sdId", sdId);
        paramsMap.put("sharedId", sharedId);
        paramsMap.put("systemId", systemId);
        Map<String, String> headersMap = new HashMap<>();
        String mobileSid = DomainUrl.getUserDomainUrl() + DomainUrl.USER_MOBILE_SID;
        NetworkRequestsUtil.getInstance().startAsynchronousRequests(mobileSid, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },context);
    }

    /**
     * 开关查询接口
     *
     * @param
     */
    public static void switchList(final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();
        String swtch = DomainUrl.initSDKPayUrl() + DomainUrl.SWITCH_LIST_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(swtch, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },context);
    }

    /**
     * 域名下发
     *
     * @param
     */
    public static void initDomainList(final Activity activity) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("appVersion", Config.getVersion());
        paramsMap.put("packageName", PACKAGE_NAME);
        paramsMap.put("methodName", METHOD_NAME);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("methodName", METHOD_NAME);
        headersMap.put("appVersion", Config.getVersion());
        headersMap.put("packageName", PACKAGE_NAME);
        useDefaultURL();
        NetworkRequestsUtil.getInstance().startAsynchronousRequestNotEncryptAES(DomainUrl.getDomainListUrl(), headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {

                        try {
                            if (message.getString("code").equals("000")) {
                                JSONObject json = message.getJSONObject("data");
                                //保存拉取请求用户中心的地址
                                AppCacheSharedPreferences.putCacheString("ucenter", json.getString("ucenter"));
                                //保存拉取web加载的url
                                AppCacheSharedPreferences.putCacheString("sdk", json.getString("sdk"));
                                //保存拉取请求支付的地址
                                AppCacheSharedPreferences.putCacheString("pay", json.getString("pay"));
                                //保存个人中心地址
                                AppCacheSharedPreferences.putCacheString("staticUrl", json.getString("staticUrl"));
                                //保存拉取充值喵点地址
                                AppCacheSharedPreferences.putCacheString("wechat", json.getString("wechat"));
                                //保存拉取礼包地址
                                AppCacheSharedPreferences.putCacheString("giftPackageUrl", json.getString("giftPackageAll"));
                                //保存拉取论坛地址
                                AppCacheSharedPreferences.putCacheString("bbsUrl", json.getString("bbs"));

                                getUniqueId(activity);
                                getSwitch(activity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(String message) {
                        Log.e("", message);
                    }
                },activity);

    }
    /**
     * 域名下发失败
     */
    private static void useDefaultURL(){
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                saveURL(DomainUrl.USER_CENTER_DOMAIN_URL_DEBUG,DomainUrl.GAME_SDK_DOMAIN_URL_DEBUG,DomainUrl.PAY_SDK_DOMAIN_URL_DEBUG,
                        DomainUrl.STATIC_SDK_DOMAIN_URL_DEBUG,DomainUrl.WECHAT_DOMAIN_URL_DEBUG,DomainUrl.GIFT_PACKAGE_DOMAIN_URL_DEBUG,
                        DomainUrl.BBS_SDK_DOMAIN_URL_DEBUG);
                break;
            //正式环境
            case 1:
                saveURL(DomainUrl.USER_CENTER_DOMAIN_URL_RELEASE,DomainUrl.GAME_SDK_DOMAIN_URL_LINE,DomainUrl.PAY_SDK_DOMAIN_URL_LINE,
                        DomainUrl.STATIC_SDK_DOMAIN_URL_LINE,DomainUrl.WECHAT_DOMAIN_URL_LINE,DomainUrl.GIFT_PACKAGE_DOMAIN_URL_LINE,
                        DomainUrl.BBS_SDK_DOMAIN_URL_LINE);
                break;
            //测试环境
            case 2:
                saveURL(DomainUrl.USER_CENTER_DOMAIN_URL_TEST,DomainUrl.GAME_SDK_DOMAIN_URL_TEST,DomainUrl.PAY_SDK_DOMAIN_URL_TEST,
                        DomainUrl.STATIC_SDK_DOMAIN_URL_TEST,DomainUrl.WECHAT_DOMAIN_URL_TEST,DomainUrl.GIFT_PACKAGE_DOMAIN_URL_TEST,
                        DomainUrl.BBS_SDK_DOMAIN_URL_TEST);
                break;
            //预发布
            case 3:
                saveURL(DomainUrl.USER_CENTER_DOMAIN_URL_PRE,DomainUrl.GAME_SDK_DOMAIN_URL_PRE,DomainUrl.PAY_SDK_DOMAIN_URL_PRE,
                        DomainUrl.STATIC_SDK_DOMAIN_URL_PRE,DomainUrl.WECHAT_DOMAIN_URL_PRE,DomainUrl.GIFT_PACKAGE_DOMAIN_URL_PRE,
                        DomainUrl.BBS_SDK_DOMAIN_URL_PRE);
                break;
            //开发环境
            case 4:
                saveURL(DomainUrl.USER_CENTER_DOMAIN_URL_DEVELOPMENT,DomainUrl.GAME_SDK_DOMAIN_URL_DEVELOPMENT,DomainUrl.PAY_SDK_DOMAIN_URL_DEVELOPMENT,
                        DomainUrl.STATIC_SDK_DOMAIN_URL_DEVELOPMENT,DomainUrl.WECHAT_DOMAIN_URL_DEVELOPMENT,DomainUrl.GIFT_PACKAGE_DOMAIN_URL_DEVELOPMENT,
                        DomainUrl.BBS_SDK_DOMAIN_URL_DEVELOPMENT);
                break;
        }
    }

    private static void saveURL(String ucenter,String sdk,String pay,String staticUrl,String wechat,String giftPackageAll,String bbs){
        //保存拉取请求用户中心的地址
        AppCacheSharedPreferences.putCacheString("ucenter", ucenter);
        //保存拉取web加载的url
        AppCacheSharedPreferences.putCacheString("sdk", sdk);
        //保存拉取请求支付的地址
        AppCacheSharedPreferences.putCacheString("pay", pay);
        //保存个人中心地址
        AppCacheSharedPreferences.putCacheString("staticUrl", staticUrl);
        //保存拉取充值喵点地址
        AppCacheSharedPreferences.putCacheString("wechat", wechat);
        //保存拉取礼包地址
        AppCacheSharedPreferences.putCacheString("giftPackageUrl", giftPackageAll);
        //保存拉取论坛地址
        AppCacheSharedPreferences.putCacheString("bbsUrl", bbs);
    }
    public static String initialNozzleInfo;
    public static void getUniqueId(Activity activity) {
        AppConfig.getAppConfig(activity).mobileSid(new GameCatSDKListener() {

            @Override
            public void onSuccess(JSONObject message) {
                initialNozzleInfo = "UniqueId:" + message;
            }

            @Override
            public void onFail(String message) {
                initialNozzleInfo = "UniqueId:" + message;
            }
        },activity);
    }


    public static void getSwitch(Context context) {
        SwitchOff.getSwitchOff().switchList(new GameCatSDKListener() {

            @Override
            public void onSuccess(JSONObject message) {
                Log.e("初始化数据：",initialNozzleInfo + "　Switch:" + message);
            }

            @Override
            public void onFail(String message) {
                Log.e("初始化数据：",initialNozzleInfo + "　Switch:" + message);
            }
        },context);
    }



}
