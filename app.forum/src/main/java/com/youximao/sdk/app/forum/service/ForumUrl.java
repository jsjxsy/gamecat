package com.youximao.sdk.app.forum.service;


import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * 论坛请求地址类
 * Created by admin on 2016/12/6.
 */

public class ForumUrl {
    public static String FORUM_DEV_URL = "";
    public static String FORUM_TEST_URL = "http://testbbs.youximao.cn/forum.php";
    public static String FORUM_LIANTIAO_URL = "http://testbbs-lt.youximao.cn/forum.php";
    public static String FORUM_PRE_URL = "http://bbs.youximao.tv/forum.php";
    public static String FORUM_RELEASE_URL = "http://bbs.youximao.tv/forum.php";

    public static String GAME_CAT_FORUM_URL = "?mod=forumdisplay&fid=";

    //域名
    public static final String USER_CENTER_DOMAIN_URL_DEBUG = "http://testucenter-lt.youximao.cn";
    public static final String USER_CENTER_DOMAIN_URL_PRE = "http://ucenter-pre.youximao.tv";


    public static final String FORUM_ACCREDIT_URL = "/sdk/login/getDiscuzToken";

    //域名
    private static String CREATE_PAY_LIANTIAO = "http://testgamecatsdk-lt.youximao.cn";
    private static String CREATE_PAY_PRE = "http://sdk-pre.youximao.tv";


    public static String FORUM_GAME_ID_URL = "/app/getGameInfo";

    /**
     * 论坛地址
     */
    public static String getForumUrl() {
        StringBuilder url = new StringBuilder();
        int e = Config.getEnvironment();
        switch (e) {
            //联调环境
            case 0:
                url.append(FORUM_LIANTIAO_URL);
                break;
            //正式环境
            case 1:
                url.append(AppCacheSharedPreferences.getCacheString("bbsUrl"));
                break;
            //测试环境
            case 2:
                url.append(AppCacheSharedPreferences.getCacheString("bbsUrl"));
                break;
            //预发布环境
            case 3:
                url.append(FORUM_PRE_URL);
                break;
            //开发环境
            case 4:
                url.append(AppCacheSharedPreferences.getCacheString("bbsUrl"));
                break;
        }

        url.append(GAME_CAT_FORUM_URL);
        url.append(AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_FROUM_GAME_ID));
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
