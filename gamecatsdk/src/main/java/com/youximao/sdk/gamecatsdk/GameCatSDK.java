package com.youximao.sdk.gamecatsdk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.gamecat.sdk.proxy.HostInterface;
import com.gamecat.sdk.proxy.HostInterfaceManager;
import com.gamecat.sdk.proxy.ServiceInterface;
import com.gamecat.sdk.proxy.ServiceInterfaceManager;
import com.lzy.utils.DownloadTask;
import com.lzy.utils.NETReceiver;
import com.lzy.utils.NetWorkUtils;

import net.wequick.small.Small;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by davy on 16/6/14.
 */
public class GameCatSDK {

    private static GameCatSDKListener mListener;
    private static GameCatSDKListener mLogoutListener;
    private static GameCatSDKListener mUpdateListener;
    private static GameCatSDKListener mGameLogoutListener;

    private static LoginBroadcastReceiver loginBroadcastReceiver;
    private static LogoutBroadcastReceiver logoutBroadcastReceiver;
    private static UpdateBroadcastReceiver updateBroadcastReceiver;
    private static NETReceiver.NetWorkListener netWorkListener;
    private static int mNetWorkType;
    private static GameLogoutBroadcastReceiver gameLogoutBroadcastReceiver;

    /**
     * 设置切换环境
     *
     * @param environment
     */
    public static void setEnvironment(final Activity activity, String gameId, int environment, String aesKey, boolean is_landscape) {
        HostInterfaceManager.getInstance().setHostInterface(new HostInterface() {
            @Override
            public Activity getActivity() {
                return activity;
            }
        });

        if (havePermission(activity)) {
            grantedPermission(activity);
        } else {
            applyPermission(activity);
        }

        //去掉空格
        if (!TextUtils.isEmpty(aesKey)) {
            aesKey = aesKey.trim();
        }
        if (!TextUtils.isEmpty(gameId)) {
            gameId = gameId.trim();
        }

        StringBuilder params = new StringBuilder("main?method=init");
        if (!TextUtils.isEmpty(gameId)) {
            params.append("&gameId=");
            params.append(Uri.encode(gameId));
        }

        if (!TextUtils.isEmpty(aesKey)) {
            params.append("&aeskey=");
            params.append(Uri.encode(aesKey));
        }

        params.append("&environment=");
        params.append(environment);

        params.append("&screenLandscape=");
        params.append(is_landscape);

        Small.openUri(params.toString(), activity);
        mNetWorkType = NetWorkUtils.getNetWorkType(activity);

        registerLogin(activity);
        registerLogout(activity);
        registerUpdate(activity);
        registerGameLogout(activity);
        registerNetWork();
    }

    private static void registerNetWork() {
        Log.e("zyy", "registerNetWork");
        if (netWorkListener == null) {
            netWorkListener = new NETReceiver.NetWorkListener() {
                @Override
                public void onNetWorkChanged(int netType) {
                    Log.e("zyy", "netType = " + netType);
                    if (mNetWorkType == 0 && netType != 0) {
                        for (DownloadTask downloadTask : DownloadTask.list) {
                            downloadTask.downloadFile(false);
                            Log.e("zyy", "断网后重新开启下载" + DownloadTask.list.size());
                        }
                    }
                    mNetWorkType = netType;
                }
            };
        }
        NETReceiver.setNetWorkListener(netWorkListener);
    }

    private static void registerLogin(Activity activity) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.youximao.demo.app.main.login");
        //注册接收器以及过滤规则
        if (null == loginBroadcastReceiver) {
            loginBroadcastReceiver = new LoginBroadcastReceiver();
        }
        lbm.registerReceiver(loginBroadcastReceiver, filter);
    }

    private static void registerLogout(Activity activity) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.youximao.demo.app.main.logout");
        //注册接收器以及过滤规则
        if (null == logoutBroadcastReceiver) {
            logoutBroadcastReceiver = new LogoutBroadcastReceiver();
        }
        lbm.registerReceiver(logoutBroadcastReceiver, filter);
    }

    private static void registerUpdate(Activity activity) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.youximao.demo.app.main.update");
        //注册接收器以及过滤规则
        if (null == updateBroadcastReceiver) {
            updateBroadcastReceiver = new UpdateBroadcastReceiver();
        }
        lbm.registerReceiver(updateBroadcastReceiver, filter);
    }

    private static void registerGameLogout(Activity activity) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.youximao.sdk.app.gamelogout");
        //注册接收器以及过滤规则
        if (null == gameLogoutBroadcastReceiver) {
            gameLogoutBroadcastReceiver = new GameLogoutBroadcastReceiver();
        }
        lbm.registerReceiver(gameLogoutBroadcastReceiver, filter);
    }

    public static void unregisterReceiver(Activity activity) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
        if (null != loginBroadcastReceiver) {
            lbm.unregisterReceiver(loginBroadcastReceiver);
        }
        if (null != logoutBroadcastReceiver) {
            lbm.unregisterReceiver(logoutBroadcastReceiver);
        }

        if (null != updateBroadcastReceiver) {
            lbm.unregisterReceiver(updateBroadcastReceiver);
        }
        if (null != netWorkListener) {
            NETReceiver.removeNetWorkListener(netWorkListener);
        }
        if (null != gameLogoutBroadcastReceiver) {
            lbm.unregisterReceiver(gameLogoutBroadcastReceiver);
        }
    }


    /**
     * 登录
     *
     * @param listener 登录监听器，用作登录成功或者失败的回调
     */
    public static void Login(Activity activity, boolean switchAccount, final GameCatSDKListener listener) {
        Small.openUri("main?method=login&switchAccount=" + switchAccount, activity);
        mListener = listener;
    }

    public static void update(Context context, final GameCatSDKListener listener) {
        Intent intent = Small.getIntentOfUri("main?method=update", context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        mUpdateListener = listener;
    }

    /**
     * 注销登录接口
     */
    public static void Logout(Activity activity) {
        Small.openUri("main?method=logout", activity);
    }


    /**
     * 调用订单方法
     *
     * @param amount      订单金额
     * @param description 产品介绍
     * @param codeNo      订单编号
     * @param notifyUrl   发货回调地址
     * @param extend      扩展参数
     */
    public static void Order(Activity activity, double amount, String description, String codeNo,
                             String notifyUrl, String extend) {
        StringBuilder params = new StringBuilder("main?method=order&amount=");
        params.append(amount);
        if (!TextUtils.isEmpty(description)) {
            params.append("&description=");
            params.append(Uri.encode(description));
        }

        if (!TextUtils.isEmpty(codeNo)) {
            params.append("&codeNo=");
            params.append(Uri.encode(codeNo));
        }

        if (!TextUtils.isEmpty(notifyUrl)) {
            params.append("&notifyUrl=");
            params.append(Uri.encode(notifyUrl));
        }

        if (!TextUtils.isEmpty(extend)) {
            params.append("&extend=");
            params.append(Uri.encode(extend));
        }

        Small.openUri(params.toString(), activity);

    }


    /**
     * 闪屏
     */
    public static void splashPage(Activity activity) {
        Small.openUri("main?method=splash", activity);
    }

    /**
     * 退出游戏提示框
     */
    public static void gameLogout(Activity activity) {
        Small.openUri("logout", activity);
    }

    public static void startFloatWindow(Activity activity, boolean isShowNavigationBar) {
        Small.openUri("floatwindow?method=start&isShowNavigationBar=" + isShowNavigationBar, activity);
    }

    public static void showFloatWindow(Activity activity) {
        ServiceInterface serviceInterface = ServiceInterfaceManager.getServiceInterface();
        if (serviceInterface != null) {
            serviceInterface.showFloatWindow();
        }
    }

    public static void hideFloatWindow(Activity activity) {
        ServiceInterface serviceInterface = ServiceInterfaceManager.getServiceInterface();
        if (serviceInterface != null) {
            serviceInterface.hideFloatWindow();
        }
    }


    public static void stopFloatWindow(Activity activity) {
        ServiceInterface serviceInterface = ServiceInterfaceManager.getServiceInterface();
        if (serviceInterface != null) {
            serviceInterface.stopFloatWindow();
        }
    }


    /**
     * pay系统注销登录监听
     */
    public static void sdkCancelListener(final GameCatSDKListener listener) {
        mLogoutListener = listener;
    }

    public static void setGameLogoutListener(GameCatSDKListener listener) {
        mGameLogoutListener = listener;
    }

    static class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            String message = intent.getStringExtra("message");
            if (TextUtils.equals(result, "success")) {
                try {
                    mListener.onSuccess(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (TextUtils.equals(result, "fail")) {
                mListener.onFail(message);
            }
        }

    }

    static class LogoutBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            String message = intent.getStringExtra("message");
            if (TextUtils.equals(result, "success")) {
                try {
                    mLogoutListener.onSuccess(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (TextUtils.equals(result, "fail")) {
                mLogoutListener.onFail(message);
            }
        }

    }


    static class UpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            String message = intent.getStringExtra("message");
            if (TextUtils.equals(result, "success")) {
                try {
                    mUpdateListener.onSuccess(new JSONObject(message));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (TextUtils.equals(result, "fail")) {
                mUpdateListener.onFail(message);
            }
        }

    }

    static class GameLogoutBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            String message = intent.getStringExtra("message");
            if (TextUtils.equals(result, "success")) {
                mGameLogoutListener.onSuccess(new JSONObject());
            } else if (TextUtils.equals(result, "fail")) {
                mGameLogoutListener.onFail(message);
            }
        }

    }

    public static boolean havePermission(Activity context) {
        int permission1 = ActivityCompat.checkSelfPermission(context, PermissionConstant.READ_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(context, PermissionConstant.WRITE_EXTERNAL_STORAGE);
        if (permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static void applyPermission(Activity context) {
        String[] PERMISSIONS_STORAGE = new String[]{PermissionConstant.READ_EXTERNAL_STORAGE, PermissionConstant.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(context, PERMISSIONS_STORAGE, PermissionConstant.REQUEST_EXTERNAL_STORAGE);
    }

    public static void grantedPermission(Activity activity) {
        Small.openUri("main?method=database", activity);
    }

}
