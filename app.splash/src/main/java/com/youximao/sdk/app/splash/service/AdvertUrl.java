package com.youximao.sdk.app.splash.service;

/**
 * Created by Administrator on 2017/2/9.
 */


import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * 活动域名
 * http://gamecatsdk.dm.com/sdk/activeIndex
 * http://testgamecatsdk.youximao.cn/sdk/activeIndex
 * http://testgamecatsdk-lt.youximao.cn/sdk/activeIndex
 * http://sdk-pre.youximao.tv/sdk/activeIndex
 * http://sdk.youximao.tv/sdk/activeIndex
 * Created by admin on 2016/11/17.
 */

public class AdvertUrl {

    public static String ADVERT_DEV_URL = "http://sdkstatic.dm.com";
    public static String ADVERT_TEST_URL = "http://testimg.youximao.cn/sdkh5";
    public static String ADVERT_LIANTIAO_URL = "http://teststatic-lt.youximao.cn";
    public static String ADVERT_PRE_URL = "http://static-pre.youximao.tv";
    public static String ADVERT_RELEASE_URL = "http://static.youximao.tv/sdkh5";

    public static final String ADVERT_URL = "/sdkh5/activeIndex.html";

    public static String getAdvertUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                url.append(ADVERT_LIANTIAO_URL);
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
                url.append(ADVERT_PRE_URL);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("staticUrl"));
                break;
        }
        url.append(ADVERT_URL);
        return url.toString();
    }

}

