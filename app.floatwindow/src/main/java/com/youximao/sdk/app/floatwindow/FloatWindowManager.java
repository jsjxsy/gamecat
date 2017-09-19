package com.youximao.sdk.app.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by admin on 2016/10/14.
 */

public class FloatWindowManager {
    public static final int BIND_SECERE_PHONE = 1;//绑定安全手机提醒
    public static final int NEW_GIFT_TIPS = 2;//新礼包提醒

    private static FloatWindowManager mInstance;
    private int mScreenWidth;
    public boolean mIsLeftDisplay = true;
    public int mWindowHeightPosition;
    public int mWindowType;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mBigLayoutParams, mSmallLayoutParams;
    private FloatWindowBigView mBigWindow;
    private FlowWindowSmallView mSmallWindow;
    private Context mContext;
    private int mScreenHeight;
    private int mNavigationBarHeight = 0;

    private FloatWindowManager(Context context) {
        if (context == null) {
            return;
        }

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mWindowHeightPosition = mScreenHeight / 2;
        mWindowType = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
        this.mContext = context;
    }


    public static FloatWindowManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FloatWindowManager(context);
        }
        return mInstance;
    }

    private void init() {
        if (FloatWindowService.getInstance() != null && FloatWindowService.getInstance().isNavigationBarShow()) {
            int resIdNavigationBar = mContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resIdNavigationBar > 0) {
                mNavigationBarHeight = mContext.getResources().getDimensionPixelSize(resIdNavigationBar);//navigationBar高度
            }
        }
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth() - mNavigationBarHeight;
    }


    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    public void createSmallWindow(boolean isHalfWindow,boolean isOPenPage,int type) {
        if (mContext == null) {
            return;
        }
        if (mSmallWindow == null) {
            init();
            mSmallWindow = new FlowWindowSmallView(mContext,isOPenPage,type);
            if (mSmallLayoutParams == null) {
                mSmallLayoutParams = new WindowManager.LayoutParams();
                //设置window type
                mSmallLayoutParams.type = mWindowType;
                //设置图片格式，效果为背景透明
                mSmallLayoutParams.format = PixelFormat.RGBA_8888;
                //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
                mSmallLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

                //调整悬浮窗显示的停靠位置为左侧置顶
                mSmallLayoutParams.gravity = Gravity.START | Gravity.TOP;
                // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
                if (mIsLeftDisplay) {
                    mSmallLayoutParams.x = 0;
                    if (isHalfWindow) {
                        mSmallWindow.setImageViewEnable(false);
                        mSmallWindow.scrollTo(mSmallWindow.getViewWidth() / 2, 0);
                        mSmallWindow.setImageViewRotation(0);
                    } else {
                        mSmallWindow.scrollTo(0, 0);
                        mSmallWindow.setImageViewEnable(true);
                    }
                } else {
                    mSmallLayoutParams.x = mScreenWidth;
                    Log.e("xsy", "small mScreeWidth:" + mScreenWidth);
                    if (isHalfWindow) {
                        mSmallWindow.setImageViewEnable(false);
                        mSmallWindow.scrollTo(-mSmallWindow.getViewWidth() / 2, 0);
                        mSmallWindow.setImageViewRotation(180);
                    } else {
                        mSmallWindow.scrollTo(0, 0);
                        mSmallWindow.setImageViewEnable(true);
                    }
                }
                mSmallLayoutParams.y = mWindowHeightPosition;
                //设置悬浮窗口长宽数据
                Log.e("xsy", "width " + mSmallWindow.getViewWidth() + " height " + mSmallWindow.getViewHeight());
                mSmallLayoutParams.width = mSmallWindow.getViewWidth();
                mSmallLayoutParams.height = mSmallWindow.getViewHeight();

            }

//            mSmallWindow
            mSmallWindow.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSmallWindow != null && mWindowManager != null) {
                        if (mIsLeftDisplay) {
                            mSmallLayoutParams.x = 0;
                        } else {
                            mSmallLayoutParams.x = mScreenWidth;
                        }
                        mWindowManager.updateViewLayout(mSmallWindow, mSmallLayoutParams);
                    }
                }
            }, 100);

            mSmallWindow.setLayoutParams(mSmallLayoutParams);
            mSmallWindow.setParams(mSmallLayoutParams);
            mWindowManager.addView(mSmallWindow, mSmallLayoutParams);
        }
    }


    public void createBigWindow(int type,String message) {
        if (mContext == null) {
            return;
        }

        if (mBigWindow == null) {
            init();
            mBigWindow = new FloatWindowBigView(mContext,type,message);
            if (mBigLayoutParams == null) {

                mBigLayoutParams = new WindowManager.LayoutParams();
                //设置window type
                mBigLayoutParams.type = mWindowType;
                //设置图片格式，效果为背景透明
                mBigLayoutParams.format = PixelFormat.RGBA_8888;
                //调整悬浮窗显示的停靠位置为左侧置顶
                mBigLayoutParams.gravity = Gravity.START | Gravity.TOP;
                mBigLayoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
                // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
                if (mIsLeftDisplay) {
                    mBigLayoutParams.x = 0;
                } else {
                    mBigLayoutParams.x = mScreenWidth;
                    Log.e("xsy", "big mScreeWidth:" + mScreenWidth);
                }
                mBigLayoutParams.y = mWindowHeightPosition;
                mBigLayoutParams.width = mBigWindow.getViewWidth();
                mBigLayoutParams.height = mBigWindow.getViewHeight();
                Log.e("xsy", "big viewWidth:" + mBigWindow.getViewWidth() + " ViewHeight" + mBigWindow.getViewHeight());
            }
            mBigWindow.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBigWindow != null && mWindowManager != null) {
                        if (mIsLeftDisplay) {
                            mBigLayoutParams.x = 0;
                        } else {
                            mBigLayoutParams.x = mScreenWidth;
                        }
                        Log.e("xsy", "big window  ");
                        mWindowManager.updateViewLayout(mBigWindow, mBigLayoutParams);
                    }
                }
            }, 1000);
            mBigWindow.setLayoutParams(mBigLayoutParams);
            mWindowManager.addView(mBigWindow, mBigLayoutParams);
            openBigWindowAnimation();
        }

    }

    public void openBigWindowAnimation() {
        removeSmallWindow();
    }

    public void closeBigWindowAnimation(int type) {
        if (mBigWindow != null) {
            if (type == -1){
                createSmallWindow(false,false,type);
            }else {
                createSmallWindow(false,true,type);
            }
            removeBigWindow();
        }
    }


    public void removeBigWindow() {
        if (mBigWindow != null) {
            mWindowManager.removeView(mBigWindow);
            mBigWindow = null;
            mBigLayoutParams = null;
        }
    }

    public void removeSmallWindow() {
        if (mSmallWindow != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mSmallWindow);
            mSmallWindow = null;
            mSmallLayoutParams = null;
        }
    }

    public void clear() {
        mContext = null;
        mInstance = null;
    }


}
