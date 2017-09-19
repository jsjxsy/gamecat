package com.youximao.sdk.lib.common.common.callback;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by davy on 16/8/19.
 * 用于注销登录
 */
public class CancelUtil {

    public static void cancelLogin(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            String openId = AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_OPEN_ID);
            String token = Config.getToken();
            jsonObject.put("openId", openId);
            jsonObject.put("token", token);
            jsonObject.put("code", "000");

            //发送广播
            if (context != null) {
                Intent intent = new Intent();
                intent.setAction("com.youximao.demo.app.main.logout");
                intent.putExtra("result", "success");
                intent.putExtra("message", jsonObject.toString());
                LocalBroadcastManager mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
