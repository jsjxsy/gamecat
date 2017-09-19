package com.youximao.sdk.lib.common.alianalytics;

import android.app.Activity;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.common.ChannelUtil;
import com.youximao.sdk.lib.common.common.config.Config;

/**
 * 阿里分析的数据统计
 * https://help.aliyun.com/document_detail/30037.html?spm=5176.product30019.6.554.fGjd70
 * Created by davy on 16/8/27.
 */
public class GameCatAnalytics {
    private static GameCatAnalytics gamecatAnalytics;

    public static synchronized GameCatAnalytics getInstance() {
        if (gamecatAnalytics == null) {
            gamecatAnalytics = new GameCatAnalytics();
        }
        return gamecatAnalytics;
    }

    public void init(Activity activity) {

        /**
         * 初始化Mobile Analytics服务
         */

        // 获取MAN服务
        MANService manService = MANServiceProvider.getService();

        // 打开调试日志
        //manService.getMANAnalytics().turnOnDebug();

        // MAN初始化方法之一，从AndroidManifest.xml中获取appKey和appSecret初始化
        manService.getMANAnalytics().init(activity.getApplication(), activity.getApplicationContext());

        // MAN另一初始化方法，手动指定appKey和appSecret
        // String appKey = "******";
        // String appSecret = "******";
        // manService.getMANAnalytics().init(this, getApplicationContext(), appKey, appSecret);

        // 若需要关闭 SDK 的自动异常捕获功能可进行如下操作,详见文档5.4
        // manService.getMANAnalytics().turnOffCrashHandler();

        // 通过此接口关闭页面自动打点功能，详见文档4.2
        manService.getMANAnalytics().turnOffAutoPageTrack();

        // 设置渠道（用以标记该app的分发渠道名称），如果不关心可以不设置即不调用该接口，渠道设置将影响控制台【渠道分析】栏目的报表展现。如果文档3.3章节更能满足您渠道配置的需求，就不要调用此方法，按照3.3进行配置即可
        manService.getMANAnalytics().setChannel(ChannelUtil.getChannel(activity));

        // 若AndroidManifest.xml 中的 android:versionName 不能满足需求，可在此指定；
        // 若既没有设置AndroidManifest.xml 中的 android:versionName，也没有调用setAppVersion，appVersion则为null
        manService.getMANAnalytics().setAppVersion(Config.VERSION);

        Collect.getInstance().custom(CustomId.id_800000);
//        /**
//         * 进行自定义事件（一般上报事件）埋点
//         */
//        // 事件名称：play_music
//        MANHitBuilders.MANCustomHitBuilder hitBuilder = new MANHitBuilders.MANCustomHitBuilder("gameCatSdk");
//        // 可使用如下接口设置时长：3分钟
//        hitBuilder.setDurationOnEvent(3 * 60);
//        hitBuilder.setProperty("time", String.valueOf(System.currentTimeMillis()));
//        hitBuilder.setProperty("gameId", Config.getGameId());
//        hitBuilder.setProperty("phone", Config.getPhone());
//        hitBuilder.setProperty("guildChlId", ChannelUtil.getChannel(activity));
//        // 设置关联的页面名称：聆听
//        hitBuilder.setEventPage("初始化sdk手机分析");
//        // 上报自定义事件打点数据
//        manService.getMANAnalytics().getDefaultTracker().send(hitBuilder.build());

    }
}
