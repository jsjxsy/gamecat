package com.youximao.sdk.app.personal.service;

import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * Created by admin on 2016/10/13.
 */

public class GameCatPayUrl {

    //接口url
    public static String CREATE_ORDER_URL = "/app/createOrder";
    public static String QUERY_AVAILABLE_COUPON_URL = "/app/queryAvailableCoupon";
    public static String QUERY_PAY_WAY_URL = "/sdk/queryPayWay";
    public static String SDK_PAY_URL = "/app/sdkPay";
    public static String SWITCH_LIST_URL = "/sdk/switch/list";
    public static String FORUM_GAME_ID_URL = "/app/getGameInfo";

    //域名
    private static String QUERY_COUPON_LIANTIAO = "http://testpay-lt.youximao.cn";
    private static String QUERY_COUPON_DEBUG = "https://pay.dm.com";
    private static String QUERY_COUPON_TEST = "https://testpay.youximao.cn";
    private static String QUERY_COUPON_PRE = "https://pay-pre.youximao.tv";
    private static String QUERY_COUPON_RELEASE = "https://pay.youximao.tv";
    private static String QUERY_COUPON_MOCK = "http://mock.youximao.cn/mockjsdata/37/";
    //域名
    private static String CREATE_PAY_LIANTIAO = "http://testgamecatsdk-lt.youximao.cn";
    private static String CREATE_PAY_DEBUG = "http://gamecatsdk.dm.com";
    private static String CREATE_PAY_TEST = "http://testgamecatsdk.youximao.cn";
    private static String CREATE_PAY_PRE = "http://sdk-pre.youximao.tv";
    private static String CREATE_PAY_RELEASE = "http://sdk.youximao.tv";

    /**
     * 获取支付的url
     */
    public static String initEvnUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                url.append(QUERY_COUPON_LIANTIAO);
                break;
            //正式环境
            case 1:
                url.append(AppCacheSharedPreferences.getCacheString("pay"));
                break;
            //测试环境
            case 2:
                url.append(AppCacheSharedPreferences.getCacheString("pay"));
                break;
            //预发布
            case 3:
                url.append(QUERY_COUPON_PRE);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("pay"));
                break;
            //mock环境
            case 5:
                url.append(AppCacheSharedPreferences.getCacheString("pay"));
                break;
        }
        return url.toString();
    }

    /**
     * 获取创建订单的url
     */
    public static String initSDKPayUrl() {

        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                url.append(CREATE_PAY_LIANTIAO);
                break;
            //正式环境
            case 1:
                url.append(AppCacheSharedPreferences.getCacheString("sdk"));
                break;
            //测试环境
            case 2:
                url.append(AppCacheSharedPreferences.getCacheString("sdk"));
                break;
            //预发布环境
            case 3:
                url.append(CREATE_PAY_PRE);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("sdk"));
                break;
        }
        return url.toString();
    }

}
