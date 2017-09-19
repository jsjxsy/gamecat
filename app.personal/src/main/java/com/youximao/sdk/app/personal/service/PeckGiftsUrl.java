package com.youximao.sdk.app.personal.service;

import android.util.Log;

import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * Created by admin on 2016/11/17.
 * 礼包地址
 */

public class PeckGiftsUrl {
    public static String GIFT_DEV_URL = "http://sdkstatic.dm.com/#/package/list";
    public static String GIFT_TEST_URL = "http://testimg.youximao.cn/sdkh5/#/package/list";
    public static String GIFT_LIANTIAO_URL = "http://teststatic-lt.youximao.cn/#/package/list";
    public static String GIFT_PRE_URL = "http://static-pre.youximao.tv/#/package/list";
    public static String GIFT_RELEASE_URL = "http://static.youximao.tv/sdkh5/#/package/list";

    public static String getPeckGiftsUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                url.append(GIFT_LIANTIAO_URL);
                break;
            //正式环境
            case 1:
                url.append(AppCacheSharedPreferences.getCacheString("giftPackageUrl"));
                break;
            //测试环境
            case 2:
                url.append(AppCacheSharedPreferences.getCacheString("giftPackageUrl"));
                break;
            //预发布
            case 3:
                url.append(GIFT_PRE_URL);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("giftPackageUrl"));
                break;
        }
        Log.e("gitpack", url.toString());
        return url.toString();
    }
}
