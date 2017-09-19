package com.youximao.sdk.lib.common.common.network;

import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;//import com.gamecat.common.config.Config;
//import com.gamecat.common.util.AppCacheSharedPreferences;
//import com.gamecat.network.NetworkRequestsUtil;
//import com.gamecat.pay.service.GameCatPayListener;
//import com.gamecat.sdk.GameCatSDKListener;

/**
 * Created by admin on 2016/10/18.
 */

public class DomainUrl {

    private static final String GAME_CAT_URL_RELEASE = "http://a.youximao.tv";
    private static final String GAME_CAT_URL_DEBUG = "http://testsite.youximao.cn";
    private static final String GAME_CAT_URL_DEVELOPMENT = "http://site.dm.com";


    private static final String GAME_CAT_DOMAIN_URL = "/apppinteracerpc/appmainuc/web.app";


    //域名  ucenter
    public static final String USER_CENTER_DOMAIN_URL_DEBUG = "http://testucenter-lt.youximao.cn";
    public static final String USER_CENTER_DOMAIN_URL_TEST = "http://testucenter.youximao.cn";
    public static final String USER_CENTER_DOMAIN_URL_PRE = "http://ucenter-pre.youximao.tv";
    public static final String USER_CENTER_DOMAIN_URL_RELEASE = "http://ucenter.youximao.tv";
    public static final String USER_CENTER_DOMAIN_URL_DEVELOPMENT = "http://uc.dm.com";

    //域名　gamecatsdk
    public static final String GAME_SDK_DOMAIN_URL_DEVELOPMENT = "http://gamecatsdk.dm.com";
    public static final String GAME_SDK_DOMAIN_URL_TEST = "http://testgamecatsdk.youximao.cn";
    public static final String GAME_SDK_DOMAIN_URL_DEBUG = "http://testgamecatsdk-lt.youximao.cn";
    public static final String GAME_SDK_DOMAIN_URL_PRE = "http://sdk-pre.youximao.tv";
    public static final String GAME_SDK_DOMAIN_URL_LINE = "http://sdk.youximao.tv";

    //域名　paysdk
    public static final String PAY_SDK_DOMAIN_URL_DEVELOPMENT = "https://pay.dm.com";
    public static final String PAY_SDK_DOMAIN_URL_TEST = "https://testpay.youximao.cn";
    public static final String PAY_SDK_DOMAIN_URL_DEBUG = "https://testpay-lt.youximao.cn";
    public static final String PAY_SDK_DOMAIN_URL_PRE = "https://pay-pre.youximao.tv";
    public static final String PAY_SDK_DOMAIN_URL_LINE = "https://pay.youximao.tv";

    //域名　staticURL
    public static final String STATIC_SDK_DOMAIN_URL_DEVELOPMENT = "http://sdkstatic.dm.com";
    public static final String STATIC_SDK_DOMAIN_URL_TEST = "http://testimg.youximao.cn/sdkh5";
    public static final String STATIC_SDK_DOMAIN_URL_DEBUG = "http://teststatic-lt.youximao.cn";
    public static final String STATIC_SDK_DOMAIN_URL_PRE = "http://static-pre.youximao.tv";
    public static final String STATIC_SDK_DOMAIN_URL_LINE = "http://static.youximao.tv/sdkh5";

    //域名　wechat
    public static final String WECHAT_DOMAIN_URL_DEVELOPMENT = "http://wechat.dm.com";
    public static final String WECHAT_DOMAIN_URL_TEST = "http://testwechat.youximao.cn";
    public static final String WECHAT_DOMAIN_URL_DEBUG = "http://testwechat-lt.youximao.cn";
    public static final String WECHAT_DOMAIN_URL_PRE = "http://wechat-pre.youximao.tv";
    public static final String WECHAT_DOMAIN_URL_LINE = "http://wechat.youximao.tv";

    //域名　giftPackageAll
    public static final String GIFT_PACKAGE_DOMAIN_URL_DEVELOPMENT = "http://sdkstatic.dm.com/#/package/list";
    public static final String GIFT_PACKAGE_DOMAIN_URL_TEST = "http://testimg.youximao.cn/sdkh5/#/package/list";
    public static final String GIFT_PACKAGE_DOMAIN_URL_DEBUG = "http://teststatic-lt.youximao.cn/#/package";
    public static final String GIFT_PACKAGE_DOMAIN_URL_PRE = "http://static-pre.youximao.tv/#/package";
    public static final String GIFT_PACKAGE_DOMAIN_URL_LINE = "http://static.youximao.tv/sdkh5/#/package";

    //域名　bbs
    public static final String BBS_SDK_DOMAIN_URL_DEVELOPMENT = "";
    public static final String BBS_SDK_DOMAIN_URL_TEST = "http://testbbs.youximao.cn/forum.php";
    public static final String BBS_SDK_DOMAIN_URL_DEBUG = "http://testbbs-lt.youximao.cn/forum.php";
    public static final String BBS_SDK_DOMAIN_URL_PRE = "http://bbs-pre.youximao.tv/forum.php";
    public static final String BBS_SDK_DOMAIN_URL_LINE = "http://bbs.youximao.tv/forum.php";


    public static final String USER_MOBILE_SID = "/sdk/login/getUniqueToken";

    //域名
    private static String CREATE_PAY_LIANTIAO = "http://testgamecatsdk-lt.youximao.cn";
    private static String CREATE_PAY_PRE = "http://sdk-pre.youximao.tv";

    public static String SWITCH_LIST_URL = "/sdk/switch/list";

    /**
     * 域名下发url
     * http://site.dm.com/apppinteracerpc/appmainuc/web.app
     * http://testsite.youximao.cn/apppinteracerpc/appmainuc/web.app
     * ?methodName=web_getDomainList&
     * packageName=com.youximao.fusionsdk&
     * appVersion=1.1
     * 获取不到取默认的	http://a-pre.youximao.tv	http://a.youximao.tv
     *
     * 联调，预发都没有域名下发的接口
     *
     * @return
     */
    public static String getDomainListUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            case 0:
                //联调环境
                url.append(GAME_CAT_URL_RELEASE);
                break;
            case 1:
                //正式环境
                url.append(GAME_CAT_URL_RELEASE);
                break;
            case 2:
                //测试环境
                url.append(GAME_CAT_URL_DEBUG);
                break;
            case 3:
                //预发布
                url.append(GAME_CAT_URL_RELEASE);
                break;
            case 4:
                url.append(GAME_CAT_URL_DEVELOPMENT);
                break;
        }
        url.append(GAME_CAT_DOMAIN_URL);
        return url.toString();
    }



    /**
     * 获取用户中心数据的url，0联调服，1正式服，2测试服，3开发服
     *
     * @return
     */
    public static String getUserDomainUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                url.append(USER_CENTER_DOMAIN_URL_DEBUG);
                break;
            //正式环境
            case 1:
                url.append(AppCacheSharedPreferences.getCacheString("ucenter"));
                break;
            //测试环境
            case 2:
                url.append(AppCacheSharedPreferences.getCacheString("ucenter"));
                break;
            //预发布
            case 3:
                url.append(USER_CENTER_DOMAIN_URL_PRE);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("ucenter"));
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
