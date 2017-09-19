package com.youximao.sdk.app.floatwindow;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import com.gamecat.sdk.proxy.ActivityLifecycleCallbacks;
import com.gamecat.sdk.proxy.ActivityLifecycleManager;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

import java.lang.ref.WeakReference;


public class FloatWindowService extends Service implements SensorEventListener {

    private static FloatWindowService mInstance;
    private static Activity mContext;    //Activity mContext;
    private static boolean mIsNavigationBarShow;

    private static final int START_SHAKE = 0x1;
    private static final int END_SHAKE = 0x2;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效

    //记录摇动状态
    private boolean isShake = false;
    private MyHandler mHandler;
    private int mWeiChatAudio;
    private boolean mIsShow = false;

    private class MyHandler extends Handler {
        private WeakReference<FloatWindowService> mReference;
        private FloatWindowService mActivity;

        public MyHandler(FloatWindowService activity) {
            mReference = new WeakReference<>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    mActivity.mVibrator.vibrate(300);
                    //发出提示音
                    mActivity.mSoundPool.play(mActivity.mWeiChatAudio, 1, 1, 0, 0, 1);
                    shakeShowFloatWindow();
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
                    mActivity.isShake = false;
                    break;
            }
        }
    }

    /**
     * 摇一摇显示浮窗
     */
    public void shakeShowFloatWindow() {
        if (!mIsShow) {
            showSmallWindow();
        } else {
            hideSmallWindow();
        }
    }

    public void openShakeDialog() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, DialogActivity.class);
        startActivity(intent);
    }


    public void resetShake() {
        isShake = false;
    }

    public static void startService(Activity context, boolean isNavigationBarShow) {
        mContext = context;
        mIsNavigationBarShow = isNavigationBarShow;
        Intent intent = new Intent(context, FloatWindowService.class);
        context.startService(intent);
    }


    public static void stopService(Context context) {
        Intent intent = new Intent(context, FloatWindowService.class);
        context.stopService(intent);
        Log.e("xsy", "FloatWindowService stopService");
    }

    public static FloatWindowService getInstance() {
        return mInstance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (mContext != null) {
            AppCacheSharedPreferences.init(mContext);
            FloatWindowManager.getInstance(mContext).createSmallWindow(false, false, 0);
        }

        mHandler = new MyHandler(this);

        //初始化SoundPool
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(this, R.raw.weichat_audio, 1);

        //获取Vibrator震动服务
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        ActivityLifecycleManager.getInstance().setActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onStart() {
                //获取 SensorManager 负责管理传感器
                mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
                if (mSensorManager != null) {
                    //获取加速度传感器
                    mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    if (mAccelerometerSensor != null) {
                        mSensorManager.registerListener(FloatWindowService.this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
                    }
                }
            }

            @Override
            public void onPause() {
                // 务必要在pause中注销 mSensorManager
                // 否则会造成界面退出后摇一摇依旧生效的bug
                if (mSensorManager != null) {
                    mSensorManager.unregisterListener(FloatWindowService.this);
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(FloatWindowService.this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SensorEventListener回调方法
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                    .abs(z) > 17) && !isShake) {
                isShake = true;
                // TODO: 2016/10/19 实现摇动逻辑, 摇动后进行震动
                Thread thread = new Thread() {
                    @Override
                    public void run() {


                        super.run();
                        try {
                            //开始震动 发出提示音 展示动画效果
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void hideSmallWindow() {
        if (mContext != null) {
            FloatWindowManager.getInstance(mContext).removeBigWindow();
            FloatWindowManager.getInstance(mContext).removeSmallWindow();
            mIsShow = false;
        }

    }

    public void showSmallWindow() {
        if (mContext != null) {
            FloatWindowManager.getInstance(mContext).createSmallWindow(false, false, 0);
            mIsShow = true;
        }
    }

    public boolean isNavigationBarShow() {
        return this.mIsNavigationBarShow;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FloatWindowManager.getInstance(mContext).removeBigWindow();
        FloatWindowManager.getInstance(mContext).removeSmallWindow();
        FloatWindowManager.getInstance(mContext).clear();
        mContext = null;
        mInstance = null;
        Log.e("xsy", "FloatWindowService onDestroy");

    }

}
