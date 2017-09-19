package com.youximao.sdk.app.personal.service;


import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * Created by admin on 2016/11/8.
 * 个人中心地址
 */

public class PersonalUrl {
    public static String WECHAT_CREATE_URL = "/sdk/androidCreateGoodsOrder";
    public static String WECHAT_POINT_URL = "/sdk/catPointRechargeInit";
    private static String WECHAT_COMBINED = "http://testwechat-lt.youximao.cn";
    private static String WECHAT_TEST = "http://testwechat.youximao.cn";
    private static String WECHAT_PRE = "http://wechat-pre.youximao.tv";
    private static String WECHAT_RELEASE = "http://wechat.youximao.tv";
    private static String WECHAT_DEBUG = "http://wechat.dm.com";


    public static String USER_CENTER_DEV_URL = "http://sdkstatic.dm.com";
    public static String USER_CENTER_TEST_URL = "http://testimg.youximao.cn/sdkh5";
    public static String USER_CENTER_LIANTIAO_URL = "http://teststatic-lt.youximao.cn";
    public static String USER_CENTER_PRE_URL = "http://static-pre.youximao.tv";
    public static String USER_CENTER_RELEASE_URL = "http://static.youximao.tv/sdkh5";

    public static String getWebUserCenterUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //单独支行业务模块
            case -1:
                url.append("http://m.baidu.com");
                break;
            //联调环境
            case 0:
                url.append(USER_CENTER_LIANTIAO_URL);
                break;
            //正式环境
            case 1:
                url.append(AppCacheSharedPreferences.getCacheString("staticUrl"));
                break;
            //测试环境
            case 2:
                url.append(AppCacheSharedPreferences.getCacheString("staticUrl"));
                break;
            //预发布
            case 3:
                url.append(USER_CENTER_PRE_URL);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("staticUrl"));
                break;
        }
        return url.toString();
    }

    /**
     * 获取喵点充值的url
     *
     * @return
     */
    public static String initWechat() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                url.append(WECHAT_COMBINED);
                break;
            //正式环境
            case 1:
                url.append(AppCacheSharedPreferences.getCacheString("wechat"));
                break;
            //测试环境
            case 2:
                url.append(AppCacheSharedPreferences.getCacheString("wechat"));
                break;
            //预发布
            case 3:
                url.append(WECHAT_PRE);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("wechat"));
                break;
        }
        return url.toString();
    }
}
