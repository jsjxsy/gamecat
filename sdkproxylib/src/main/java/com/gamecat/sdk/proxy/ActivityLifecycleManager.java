package com.gamecat.sdk.proxy;

/**
 * Created by admin on 2017/3/21.
 */

public class ActivityLifecycleManager {
    private ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private static ActivityLifecycleManager instance;

    private ActivityLifecycleManager() {
    }

    public static ActivityLifecycleManager getInstance() {
        if (null == instance) {
            instance = new ActivityLifecycleManager();
        }
        return instance;
    }

    public void setActivityLifecycleCallbacks(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        this.activityLifecycleCallbacks = activityLifecycleCallbacks;
    }

    public ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return this.activityLifecycleCallbacks;
    }
}
