package com.youximao.sdk.app.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.constant.IntentConstant;
import com.youximao.sdk.lib.common.common.network.UpdateApi;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.sdk.GameCatSDK;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import net.wequick.small.Small;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity {
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKManager.init(this);
        AppCacheSharedPreferences.init(this);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        action();
    }


    private void action() {
        Uri uri = Small.getUri(this);
        if (uri != null) {
            String method = uri.getQueryParameter("method");
            switch (method) {
                case "init":
                    init(uri);
                    break;
                case "login":
                    login(uri);
                    break;
                case "logout":
                    logout();
                    break;
                case "order":
                    order(uri);
                    break;
                case "splash":
                    splash();
                    break;
                case "update":
                    update();
                    break;
                case "download":
                    Intent intent = getIntent();
                    int type = intent.getIntExtra("type", 1);
                    HashMap<String, String> map = (HashMap<String, String>) intent.getSerializableExtra("data");
                    download(type, map);
                    break;
                case "database":
                    createDatabase();
                    break;

            }
        }

    }

    /**
     * 埋点
     */
    private void download(int type, HashMap<String, String> map) {
        if (type == 1) {//开始
            Collect.getInstance().update(CustomId.id_770000, map);
        } else if (type == 2) {//成功
            Collect.getInstance().update(CustomId.id_770001, map);
        } else if (type == 3) {//失败
            Collect.getInstance().update(CustomId.id_770002, map);
        }
        finish();
    }

    private void init(Uri uri) {
        String gameId = uri.getQueryParameter("gameId");
        int evn = Integer.parseInt(uri.getQueryParameter("environment"));
        String appAESKey = uri.getQueryParameter("aeskey");
        boolean appIsLandscape = uri.getBooleanQueryParameter(IntentConstant.IS_LANDSCAPE, true);
        GameCatSDK.setEnvironment(this, gameId, evn, appAESKey, appIsLandscape);//0为联调环境，1为正式环境,2为测试，3为预发布,4开发环境
        Log.e("init", "gameId=" + gameId + "appAESKey=" + appAESKey);
        finish();
    }

    private void logout() {
        GameCatSDK.Logout(this);
        finish();
    }

    private void login(Uri uri) {
        boolean switchAccount = Boolean.parseBoolean(uri.getQueryParameter("switchAccount"));
        GameCatSDK.Login(this, switchAccount, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                Intent intent = new Intent();
                intent.setAction("com.youximao.demo.app.main.login");
                intent.putExtra("result", "success");
                intent.putExtra("message", message.toString());
                //发送广播
                mLocalBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFail(String message) {
                Intent intent = new Intent();
                intent.setAction("com.youximao.demo.app.main.login");
                intent.putExtra("result", "fail");
                intent.putExtra("message", message.toString());
                //发送广播
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });
        finish();

    }


    private void order(Uri uri) {
        double amount = Double.parseDouble(uri.getQueryParameter("amount"));
        String description = uri.getQueryParameter("description");
        String codeNo = uri.getQueryParameter("codeNo");
        String notifyUrl = uri.getQueryParameter("notifyUrl");
        String extend = uri.getQueryParameter("extend");
        GameCatSDK.Order(this, amount, description, codeNo, notifyUrl, extend);
        finish();
    }

    private void splash() {
        GameCatSDK.splashPage(this);
        finish();
    }


    private void update() {
        UpdateApi.update(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                Intent intent = new Intent();
                intent.setAction("com.youximao.demo.app.main.update");
                intent.putExtra("result", "success");
                intent.putExtra("message", message.toString());
                //发送广播
                mLocalBroadcastManager.sendBroadcast(intent);
            }

            @Override
            public void onFail(String message) {
                Intent intent = new Intent();
                intent.setAction("com.youximao.demo.app.main.update");
                intent.putExtra("result", "fail");
                intent.putExtra("message", message.toString());
                //发送广播
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        }, MainActivity.this);
        finish();
    }

    private void createDatabase() {
        Small.openUri("usercenter?method=database", this);
        finish();
    }

}
