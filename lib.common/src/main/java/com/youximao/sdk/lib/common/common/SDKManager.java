package com.youximao.sdk.lib.common.common;

import android.app.Activity;

/**
 * Created by davy on 16/6/14.
 */
public class SDKManager {
    /**
     * GameCatActivity的上下文
     */
    private static Activity activity;

    public static void init(Activity context) {
        activity = context;
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static Activity getContext() {
        return activity;
    }

    /**
     * 传值上下文
     *
     * @return
     */
    public static void setContext(Activity context) {
        activity = context;
    }

}
