package com.youximao.sdk.lib.common.alianalytics;


import android.text.TextUtils;

import com.alibaba.sdk.android.man.MANHitBuilders;
import com.alibaba.sdk.android.man.MANPageHitBuilder;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.youximao.sdk.lib.common.common.ChannelUtil;
import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
//import com.youximao.sdk.lib.common.common.widget.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于收集埋点
 * Created by davy on 16/8/28.
 */
public class Collect {
    private static Collect collect;

    public static Collect getInstance() {

        if (collect == null) {
            collect = new Collect();
        }
        return collect;
    }

    //注册埋点
    public void register(String openId, String phone) {
        MANService manService = MANServiceProvider.getService();

        manService.getMANAnalytics().userRegister(openId);
        /**
         * 使用页面基础打点类进行页面事件埋点，见文档4.2.3
         */
        // 获取基础页面打点对象
        MANPageHitBuilder pageHitBuilder = new MANPageHitBuilder("register");
        // 设置来源页面名称
        pageHitBuilder.setReferPage("register");
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("openId", openId);
        properties.put("phone", phone);
        properties.put("time", String.valueOf(System.currentTimeMillis()));
        properties.put("gameId", Config.getGameId());
        pageHitBuilder.setProperties(properties);
        manService.getMANAnalytics().getDefaultTracker().send(pageHitBuilder.build());
    }


    //登录埋点
    public void login(String phone, String openId) {
        MANService manService = MANServiceProvider.getService();
        manService.getMANAnalytics().updateUserAccount(phone, openId);
        /**
         * 使用页面基础打点类进行页面事件埋点，见文档4.2.3
         */
        // 获取基础页面打点对象
        MANPageHitBuilder pageHitBuilder = new MANPageHitBuilder("login");
        // 设置来源页面名称
        pageHitBuilder.setReferPage("login");
        Map<String, String> properties = new HashMap<String, String>();
//        properties.put("openId", openId);
        properties.put("phone", phone);
        properties.put("time", String.valueOf(System.currentTimeMillis()));
        properties.put("gameId", Config.getGameId());
        String userId = AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_USER_ID);
        if (!TextUtils.isEmpty(userId)) {
            properties.put("userId", userId);
        }
        properties.put("userGameBindingChannelId", Config.getGameBindingChannelId());
        properties.put("appChannelId", ChannelUtil.getChannel(SDKManager.getContext()));
        properties.put("guildChlId", ChannelUtil.getChannel(SDKManager.getContext()));
        pageHitBuilder.setProperties(properties);
        manService.getMANAnalytics().getDefaultTracker().send(pageHitBuilder.build());

    }

    //自定义埋点
    public void custom(String key) {
        /**
         * 进行自定义事件（一般上报事件）埋点
         */
        MANService manService = MANServiceProvider.getService();
        // 事件名称：play_music
        MANHitBuilders.MANCustomHitBuilder hitBuilder = new MANHitBuilders.MANCustomHitBuilder(key);
        hitBuilder.setEventPage("自定义埋点");
        // 可使用如下接口设置时长：3分钟
        hitBuilder.setDurationOnEvent(3 * 60);
//        hitBuilder.setProperty("openId", Config.getOpenId());
        hitBuilder.setProperty("time", String.valueOf(System.currentTimeMillis()));
        hitBuilder.setProperty("gameId", Config.getGameId());
        hitBuilder.setProperty("phone", Config.getPhone());
        hitBuilder.setProperty("userId", Config.getUserId());
        hitBuilder.setProperty("userGameBindingChannelId", Config.getGameBindingChannelId());
        hitBuilder.setProperty("appChannelId", ChannelUtil.getChannel(SDKManager.getContext()));
        hitBuilder.setProperty("guildChlId", ChannelUtil.getChannel(SDKManager.getContext()));
        // 上报自定义事件打点数据
        manService.getMANAnalytics().getDefaultTracker().send(hitBuilder.build());
//        ToastUtil.makeText(SDKManager.getContext(), "customId:" + key, false);

    }

    //升级埋点
    public void update(String key, Map<String, String> properties) {
        MANService manService = MANServiceProvider.getService();
        // 事件名称：play_music
        MANHitBuilders.MANCustomHitBuilder hitBuilder = new MANHitBuilders.MANCustomHitBuilder(key);
        hitBuilder.setEventPage("升级埋点");

        // 可使用如下接口设置时长：3分钟
        hitBuilder.setDurationOnEvent(3 * 60);

        if (null == properties){
            properties = new HashMap<String, String>();
        }
//        properties.put("openId", openId);
        properties.put("phone", Config.getPhone());
        properties.put("time", String.valueOf(System.currentTimeMillis()));
        properties.put("gameId", Config.getGameId());
        String userId = AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_USER_ID);
        if (!TextUtils.isEmpty(userId)) {
            properties.put("userId", userId);
        }
        properties.put("userGameBindingChannelId", Config.getGameBindingChannelId());
        properties.put("appChannelId", ChannelUtil.getChannel(SDKManager.getContext()));
        properties.put("guildChlId", ChannelUtil.getChannel(SDKManager.getContext()));
        hitBuilder.setProperties(properties);
        manService.getMANAnalytics().getDefaultTracker().send(hitBuilder.build());
//        ToastUtil.makeText(SDKManager.getContext(), "update customId:" + key, false);
    }

}
