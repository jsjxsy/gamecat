package com.youximao.sdk.app.usercenter.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.usercenter.database.UserCenterDatabaseConstant;
import com.youximao.sdk.app.usercenter.model.OpenId;
import com.youximao.sdk.app.usercenter.model.QQUserInfo;
import com.youximao.sdk.lib.common.network.NetworkRequestsUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;
import com.youximao.sdk.lib.database.ApiTableClazzDeclare;
import com.youximao.sdk.lib.database.DatabaseCache;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/11/3.
 */

public class UserApi implements ApiTableClazzDeclare {

    private static UserApi INSTANCE;

    public static synchronized UserApi getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserApi();
        }
        return INSTANCE;
    }

    private UserApi() {
        DatabaseCache.getInstance().declareColumnsClass(this);
    }

    /**
     * 登录
     *
     * @param
     */
    public void normalLogin(final String userName, final String password, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", userName);
        paramsMap.put("password", password);
        Map<String, String> headersMap = new HashMap<>();
        String login = UserUrl.getUserDomainUrl() + UserUrl.USER_NORMAL_LOGIN;
        NetworkRequestsUtil.getInstance().startNetworkRequests(login, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);

    }

    /**
     * 注册
     *
     * @param
     */
    public void normalRegister(final String mobile, final String password, String vcode, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("vcode", vcode);
        paramsMap.put("password", password);
        Map<String, String> headersMap = new HashMap<>();
        String login = UserUrl.getUserDomainUrl() + UserUrl.USER_NORMAL_REGISTER;
        NetworkRequestsUtil.getInstance().startNetworkRequests(login, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);

    }

    /**
     * 一键登录
     *
     * @param
     */
    public void quickRegister(final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();
        String oneKeyRegister = UserUrl.getUserDomainUrl() + UserUrl.USER_QUICK_REGISTER;
        NetworkRequestsUtil.getInstance().startNetworkRequests(oneKeyRegister, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);

    }


    /**
     * QQ登录
     * sex 0 代表男 1代表女
     * 类型(1-QQ,2-微信,3-新浪)
     *
     * @param
     */
    public void otherLogin(String iconUrl, String openId, String unionId, String sex, String userName, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("iconURL", iconUrl);
        paramsMap.put("openId", openId);
        paramsMap.put("unionId", unionId);
        paramsMap.put("sex", sex);
        paramsMap.put("type", "1");
        paramsMap.put("userName", userName);
        Map<String, String> headersMap = new HashMap<>();
        String otherLogin = UserUrl.getUserDomainUrl() + UserUrl.USER_OTHER_LOGIN;
        NetworkRequestsUtil.getInstance().startNetworkRequests(otherLogin, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);

    }


    /**
     * 自动登录
     *
     * @param listener
     */
    public void autoLogin(final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();
        String autoLogin = UserUrl.getUserDomainUrl() + UserUrl.USER_AUTO_LOGIN;
        NetworkRequestsUtil.getInstance().startNetworkRequests(autoLogin, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);

    }

    /**
     * 获取手机验证码
     *
     * @param status:1-注册，2-忘记密码，99-其他
     * @param listener
     */
    public void getVerificationCode(String status, String phone, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", phone);
        paramsMap.put("status", status);
        Map<String, String> headersMap = new HashMap<>();
        String verificationCode = UserUrl.getUserDomainUrl() + UserUrl.USER_MOBILE_CODE;
        NetworkRequestsUtil.getInstance().startAsynchronousRequests(verificationCode, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);
    }

    /**
     * 根据token获取用户信息
     *
     * @param listener
     */
    public void getUserInfoByToken(final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();

        String userInfoByTokenUrl = UserUrl.getUserDomainUrl() + UserUrl.USER_INFO_BY_TOKEN;
        NetworkRequestsUtil.getInstance().startNetworkRequests(userInfoByTokenUrl, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);
    }


    /**
     * 校验是否绑定手机
     *
     * @param listener
     */
    public void validMobile(String account, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("account", account);
        Map<String, String> headersMap = new HashMap<>();
        String validMobile = UserUrl.getUserDomainUrl() + UserUrl.USER_VALID_MOBILE;
        NetworkRequestsUtil.getInstance().startNetworkRequests(validMobile, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);
    }

    /**
     * 校验验证码
     *
     * @param listener
     */
    public void validMobileCode(String mobile, String vcode, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("vcode", vcode);

        Map<String, String> headersMap = new HashMap<>();
        String validMobileCode = UserUrl.getUserDomainUrl() + UserUrl.USER_VALID_MOBILE_CODE;
        NetworkRequestsUtil.getInstance().startNetworkRequests(validMobileCode, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);
    }


    /**
     * 重设密码
     *
     * @param listener
     */
    public void resetPassword(String mobile, String vcode, String newPassword, String aginPassword, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", mobile);
        paramsMap.put("vcode", vcode);
        paramsMap.put("newPassword", newPassword);
        paramsMap.put("aginPassword", aginPassword);
        Map<String, String> headersMap = new HashMap<>();
        String resetPassword = UserUrl.getUserDomainUrl() + UserUrl.USER_RESET_PASSWORD;
        NetworkRequestsUtil.getInstance().startNetworkRequests(resetPassword, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);
    }


    /**
     * callback(
     * {
     * client_id: "101361045",
     * openid: "326FF459ADFD364635B14B94F918908B"
     * }
     * )
     *
     * @param accessToken
     * @param listener
     */
    public void getQQOpenId(final String accessToken, final GameCatSDKListener listener, final Context context) {
        final String openIdUrl = UserUrl.getOpenIdUrl(accessToken);
        NetworkRequestsUtil.getInstance().startNetworkRequests(openIdUrl,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        String callback = response.toString().replace("callback(", "").replace(");", "");
                        Log.e("xsy", "callback" + callback);
                        final OpenId openId = JSON.parseObject(callback, OpenId.class);
                        if (openId == null) {
                            listener.onFail(callback);
                            return;
                        }
                        Log.e("xsy", "openId:" + openId.getOpenid() + "getClient_id:" + openId.getClient_id());
                        getQQUserInfo(accessToken, openId, new GameCatSDKListener() {

                            @Override
                            public void onSuccess(JSONObject response) {
                                QQUserInfo userInfo = JSON.parseObject(response.toString(), QQUserInfo.class);
                                String sex = "0";
                                String gender = userInfo.getGender();
                                if (TextUtils.equals(gender, "男")) {
                                    sex = "0";
                                } else {
                                    sex = "1";
                                }
                                otherLogin(userInfo.getFigureurl(), openId.getOpenid(), openId.getUnionid(), sex, userInfo.getNickname(), new GameCatSDKListener() {
                                    @Override
                                    public void onSuccess(JSONObject message) {
                                        listener.onSuccess(message);
                                    }

                                    @Override
                                    public void onFail(String message) {
                                        listener.onFail(message);
                                    }
                                }, context);
                            }

                            @Override
                            public void onFail(String message) {
                                listener.onFail(message);
                            }
                        }, context);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);
    }

    /**
     * @param accessToken
     * @param openId
     * @param listener
     */
    public void getQQUserInfo(String accessToken, OpenId openId, final GameCatSDKListener listener, Context context) {
        String qqUserInfoUrl = UserUrl.getQQUserInfoUrl(accessToken, openId.getClient_id(), openId.getOpenid());
        NetworkRequestsUtil.getInstance().startNetworkRequests(qqUserInfoUrl,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        listener.onSuccess(response);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                }, context);
    }


    @Override
    public ArrayList<Class<?>> getTableClazzDeclare() {
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(UserCenterDatabaseConstant.UserInformationColumns.class);
        return list;
    }
}
