package com.youximao.sdk.app.usercenter.service;

import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * Created by admin on 2016/11/3.
 */

public class UserUrl {

    //域名
    public static final String USER_CENTER_DOMAIN_URL_DEBUG = "http://testucenter-lt.youximao.cn";
    public static final String USER_CENTER_DOMAIN_URL_TEST = "http://testucenter.youximao.cn";
    public static final String USER_CENTER_DOMAIN_URL_PRE = "http://ucenter-pre.youximao.tv";
    public static final String USER_CENTER_DOMAIN_URL_RELEASE = "http://ucenter.youximao.tv";
    public static final String USER_CENTER_DOMAIN_URL_DEVELOPMENT = "http://uc.dm.com";
    //接口url
    public static final String USER_NORMAL_LOGIN = "/sdk/login/nomalLogin";
    public static final String USER_QUICK_REGISTER = "/sdk/register/oneKeyRegister";
    public static final String USER_NORMAL_REGISTER = "/sdk/register/nomalRegister";
    public static final String USER_OTHER_LOGIN = "/sdk/login/otherLogin";
    public static final String USER_AUTO_LOGIN = "/sdk/login/autoLogin";
    public static final String USER_INFO_BY_TOKEN = "/sdk/login/getUserInfoByToken";
    public static final String USER_MOBILE_CODE = "/sdk/register/getMobileCode";
    public static final String USER_MOBILE_SID = "/sdk/login/getUniqueToken";
    public static final String USER_VALID_MOBILE = "/sdk/editInfo/validMobileBind";
    public static final String USER_VALID_MOBILE_CODE = "/sdk/editInfo/validMobileCode";
    public static final String USER_RESET_PASSWORD = "/sdk/editInfo/resetPassword";
    public static final String FORUM_ACCREDIT_URL = "/sdk/login/getDiscuzToken";
    //QQ
    public static final String USER_QQ_LOGIN = "https://graph.qq.com/oauth2.0/authorize";
    public static final String YOUR_APP_ID = "101361045";

    private static final String YOUR_REDIRECT_URI = "http://static.youximao.tv/sdkh5/autoLogin.html";


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
     * https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=101361045&redirect_uri=http://www.youximao.tv&scope= get_user_info
     * http://wiki.open.qq.com/wiki/website/使用Implicit_Grant方式获取Access_Token
     * ?response_type=token&client_id=[YOUR_APPID]&redirect_uri=[YOUR_REDIRECT_URI]&scope=[THE_SCOPE]
     */

    public static String getQQLoginUrl() {
        String url = USER_QQ_LOGIN +
                "?" +
                "response_type=token" +
                "&" +
                "client_id=" +
                YOUR_APP_ID +
                "&" +
                "redirect_uri=" +
                YOUR_REDIRECT_URI +
                "&" +
                "scope=get_user_info" +
                "&" +
                "display=mobile";
        return url;
    }


    /**
     * https://graph.qq.com/oauth2.0/me？access_token=B2ACFAF95AA2C168991A4967570C00BE&expires_in=7776000
     *https://graph.qq.com/oauth2.0/me?access_token=ACCESSTOKEN&unionid=1
     * @return
     */
    public static String getOpenIdUrl(String accessToken) {
        String url = "https://graph.qq.com/oauth2.0/me" +
                "?" +
                "access_token=" +
                accessToken +
                "&unionid=1";
        return url;
    }

    /**
     * https://graph.qq.com/user/get_user_info?access_token=B2ACFAF95AA2C168991A4967570C00BE&oauth_consumer_key=101361045&openid=326FF459ADFD364635B14B94F918908B
     */
    public static String getQQUserInfoUrl(String accessToken, String oauth_consumer_key, String openid) {
        String url = "https://graph.qq.com/user/get_user_info" +
                "?" +
                "access_token=" +
                accessToken +
                "&" +
                "oauth_consumer_key=" +
                oauth_consumer_key +
                "&" +
                "openid=" +
                openid;
        return url;
    }
}
