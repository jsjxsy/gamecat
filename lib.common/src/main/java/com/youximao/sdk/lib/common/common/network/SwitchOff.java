package com.youximao.sdk.lib.common.common.network;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.model.SwitchItem;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

//import com.gamecat.common.config.Config;
//import com.gamecat.common.constant.SharePreferenceConstant;
//import com.gamecat.common.util.AES.AESUtil;
//import com.gamecat.common.util.AppCacheSharedPreferences;
//import com.gamecat.pay.service.GameCatPayApi;
//import com.gamecat.sdk.GameCatSDKListener;
//import com.gamecat.usercenter.model.SwitchItem;

/**
 * Created by yulinsheng on 16-12-9.
 * 获取开关信息
 */
public class SwitchOff {

    private static SwitchOff switchOff;

    private SwitchOff() {
    }

    public static SwitchOff getSwitchOff() {
        if (switchOff == null) {
            switchOff = new SwitchOff();
        }
        return switchOff;
    }

    public void switchList(final GameCatSDKListener listener, Context context) {
        DomainApi.switchList(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (code.equals("000")) {
                        String data = message.getString("data");
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
                        SwitchItem switchItem = JSON.parseObject(content, SwitchItem.class);
                        saveSwitch(switchItem);
                        listener.onFail(content);
                    } else {
                        String content = message.getString("message");
                        listener.onFail(content);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFail("接口初始化信息失败！");
                }
            }

            @Override
            public void onFail(String message) {
                Log.e("返回数据", "初始化获取开关信息失败！");
            }
        },context);
    }

    public void saveSwitch(SwitchItem switchItem) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_ACTIVE, switchItem.getActive());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_BBS, switchItem.getBbs());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_GIFT_PACKAGE, switchItem.getGiftPackage());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_QQ_LOGIN, switchItem.getQqLogin());
    }
}
