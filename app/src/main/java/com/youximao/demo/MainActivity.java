package com.youximao.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gamecat.sdk.proxy.ActivityLifecycleManager;
import com.youximao.sdk.gamecatsdk.GameCatSDK;
import com.youximao.sdk.gamecatsdk.GameCatSDKListener;
import com.youximao.sdk.gamecatsdk.PermissionConstant;
import com.youximao.sdk.gamecatsdk.utils.NavigationBarUtil;
import com.youximao.sdk.gamecatsdk.utils.SimpleTextWatcher;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends Activity implements View.OnClickListener {
    TextView listener;
    EditText payEdit;

    String gameId;
    /**
     * 调用订单方法
     *
     * @param amount 订单金额
     * @param description 产品介绍
     * @param codeNo 订单编号
     * @param notifyUrl 发货回调地址
     * @param extend 扩展参数
     * @param listener 订单回调
     */
    double amount = 0.1;
    int mAmount;
    String description, codeNo, notifyUrl, extend;
    boolean mIsDisplayFloatWindow = false;
    private boolean mIsSwitch;

    private class PayTextWatcher extends SimpleTextWatcher {
        private EditText editText;

        private PayTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s != null && s.length() > 0) {
                //比如：.123 删除前面的.开始的数字
                String value = s.toString();
                int len = value.length();
                if (len == 1 && value.equals(".")) {
                    Toast.makeText(MainActivity.this, "输入数字错误", Toast.LENGTH_SHORT).show();
                    s.clear();
                    return;
                }
                //判断金额是否大于0
                if (TextUtils.equals(value, "0.0")) {
                    Toast.makeText(MainActivity.this, "输入金额必须大于0", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                    return;
                }
                //保留小数点的位数，一位
                if (value.contains(".")) {
                    if (value.length() - 1 - value.indexOf(".") > 1) {
                        Toast.makeText(MainActivity.this, "输入金额只能保留一位小数", Toast.LENGTH_SHORT).show();
                        value = value.substring(0, value.indexOf(".") + 1 + 1);
                        editText.setText(value);
                        return;
                    }
                }

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        NavigationBarUtil.hideNavigationBar(this);
        setContentView(R.layout.main);
        init();

        if (savedInstanceState != null && savedInstanceState.containsKey("isDisplayFloatWindow") &&
                savedInstanceState.getBoolean("isDisplayFloatWindow")) {
            GameCatSDK.startFloatWindow(MainActivity.this, false);
            mIsDisplayFloatWindow = true;
        }

        GameCatSDK.startFloatWindow(MainActivity.this, false);
        mIsDisplayFloatWindow = true;
    }

    private void init() {
        boolean isLandscape = true;    // 是否横屏
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
        }
        GameCatSDK.setEnvironment(this, "360_baozoumoshoutuan_2", 2, "eW91eGltYW82MDAwMDFsdA==", isLandscape);//0为联调环境，1为正式环境,2为测试，3为预发布,4开发环境
        initCallBack();
        initView();
    }

    private void initCallBack() {
        //用户注销接口
        GameCatSDK.sdkCancelListener(new GameCatSDKListener() {
            @Override
            public void onSuccess(final JSONObject message) {
                //注销
                listener.setText("登录注销,回调参数" + message + "");
                //重新登录
                login(true);
                closeFloatWindow();
            }

            @Override
            public void onFail(String message) {
                Log.e("返回数据", message + "");
            }
        });

        GameCatSDK.setGameLogoutListener(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                // TODO: 17-3-17  同步退出游戏
                finish();
            }

            @Override
            public void onFail(String message) {
                // TODO: 17-3-17  同步退出游戏
                finish();
            }
        });

    }


    private void initView() {
        TextView channelName = (TextView) findViewById(R.id.channel_name);
        channelName.setText("(渠道名：");
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.id_button_login_cancel).setOnClickListener(this);
        findViewById(R.id.order).setOnClickListener(this);
        findViewById(R.id.flash_screen).setOnClickListener(this);
        payEdit = (EditText) findViewById(R.id.pay_edit);
        payEdit.addTextChangedListener(new PayTextWatcher(payEdit));
        listener = (TextView) findViewById(R.id.listener_text);
        listener.setText("内部环境");
        findViewById(R.id.id_button_test).setOnClickListener(this);
        findViewById(R.id.id_button_floatwindow).setOnClickListener(this);
        findViewById(R.id.gamelogout).setOnClickListener(this);
    }


    private void initOrderData() {
        description = "倚天剑";
        codeNo = String.valueOf(System.currentTimeMillis());
        extend = "一区张无忌下的订单";
        String amountValue = payEdit.getEditableText().toString();
        if (!TextUtils.isEmpty(amountValue)) {
            try {
                mAmount = Integer.valueOf(amountValue).intValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            payEdit.setText(amountValue);
            payEdit.setSelection(amountValue.length());
            try {
                notifyUrl = URLEncoder.encode("http://mock.youximao.cn/mockjsdata/11/sdk/notify", "UTF-8");
                amount = Double.parseDouble(amountValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.login:
                listener.setText("上送游戏Id" + gameId);
                login(false);
                break;
            case R.id.id_button_login_cancel:
                logout();
                break;
            case R.id.order:
                initOrderData();
                listener.setText("上送游戏订单" + "金额" + amount + ";产品介绍" + description + ";订单编号" + codeNo + ";发货回调地址" + notifyUrl + ";扩展参数" + extend);
                if (amount <= 0) {
                    Toast.makeText(MainActivity.this, "输入金额必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAmount > 5000000) {
                    Toast.makeText(MainActivity.this, "超过金额限制", Toast.LENGTH_SHORT).show();
                    return;
                }
                GameCatSDK.Order(MainActivity.this, amount, description, codeNo, notifyUrl, extend);
                break;
            case R.id.flash_screen:
                listener.setText("闪屏");
                GameCatSDK.splashPage(MainActivity.this);
                break;
            case R.id.id_button_test:
                listener.setText("配置测试");
                break;
            case R.id.id_button_floatwindow:
                break;
            case R.id.gamelogout:
                GameCatSDK.gameLogout(MainActivity.this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsDisplayFloatWindow) {
            GameCatSDK.showFloatWindow(MainActivity.this);
        }
        if (ActivityLifecycleManager.getInstance().getActivityLifecycleCallbacks() != null) {
            ActivityLifecycleManager.getInstance().getActivityLifecycleCallbacks().onStart();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            NavigationBarUtil.hideNavigationBar(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIsDisplayFloatWindow) {
            GameCatSDK.hideFloatWindow(MainActivity.this);
        }

        if (ActivityLifecycleManager.getInstance().getActivityLifecycleCallbacks() != null) {
            ActivityLifecycleManager.getInstance().getActivityLifecycleCallbacks().onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeFloatWindow();
        GameCatSDK.unregisterReceiver(this);
    }

    private void closeFloatWindow() {
        if (mIsDisplayFloatWindow) {
            GameCatSDK.stopFloatWindow(MainActivity.this);
            mIsDisplayFloatWindow = false;
        }
    }

    private void login(boolean switchAccount) {
        //第二参数 true：不自动登录，跳到登录页面
        // false：当用户登录过且token没有失效，会跳到自动登录界面，没有登录或者token失效，则跳到登录界面
        GameCatSDK.Login(MainActivity.this, switchAccount, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                listener.setText("登录成功回调参数" + message + "");
                Log.e("返回数据", message + "");
                //第二参数：是否有虚拟系统导航栏 NavigationBar,计算浮窗的width
                //true:有系统NavigationBar
                //false:没有系统NavigationBar
                GameCatSDK.startFloatWindow(MainActivity.this, false);
                mIsDisplayFloatWindow = true;
            }

            @Override
            public void onFail(String message) {
                listener.setText(message + "");
            }
        });

    }

    private void logout() {
        GameCatSDK.Logout(MainActivity.this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDisplayFloatWindow", mIsDisplayFloatWindow);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionConstant.REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    GameCatSDK.grantedPermission(this);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    GameCatSDK.applyPermission(this);
                }
                return;
            }
        }
    }
}
