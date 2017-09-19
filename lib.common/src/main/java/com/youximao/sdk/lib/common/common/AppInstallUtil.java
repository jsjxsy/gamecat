package com.youximao.sdk.lib.common.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by admin on 2017/1/11.
 */

public class AppInstallUtil {
    private static final String WEIXIN_PACKAGE = "com.tencent.mm";

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(WEIXIN_PACKAGE)) {
                    return true;
                }
            }
        }

        return false;
    }


//    public static boolean isWeiXinRunning(Context context) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
//        boolean isAppRunning = false;
//        for (ActivityManager.RunningTaskInfo info : list) {
//            if (info.topActivity.getPackageName().equals(WEIXIN_PACKAGE) ||
//                    info.baseActivity.getPackageName().equals(WEIXIN_PACKAGE)) {
//                isAppRunning = true;
//                Log.i("xsy", info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
//                break;
//            }
//        }
//        return isAppRunning;
    // 获取activity管理对象
//        ActivityManager activityManager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        // 获取所有正在运行的app
//        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
//                .getRunningAppProcesses();
//        boolean isAppRunning = false;
//        // 遍历app，获取应用名称或者包名
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.processName.equals(WEIXIN_PACKAGE)) {
//                isAppRunning = true;
//                Log.i("app", appProcess.processName + "在运行");
//            }
//        }


//        return  isAppRunning;
//    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }


}
