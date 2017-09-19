package com.youximao.sdk.app.forum.service;


import android.content.Context;

import com.youximao.sdk.lib.common.network.NetworkRequestsUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 论坛接口请求类
 * Created by yulinsheng on 16-12-13.
 */
public class ForumApi {

    /**
     * 拿到论坛的授权信息
     */
    public static void getDiscuzToken(final GameCatSDKListener listener, final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();

        String forumUrl = ForumUrl.getUserDomainUrl() + ForumUrl.FORUM_ACCREDIT_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(forumUrl, headersMap, paramsMap,
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
     * 根据sdk游戏id获取平台内部gameId
     */
    public static void getGameInfo(final GameCatSDKListener listener, final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();

        String gameInfo = ForumUrl.initSDKPayUrl() + ForumUrl.FORUM_GAME_ID_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(gameInfo, headersMap, paramsMap,
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

}
