package com.youximao.sdk.app.floatwindow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;

import net.wequick.small.Small;


/**
 * Created by admin on 16/9/28.
 */

public class FlowWindowSmallView extends LinearLayout {
    private final int DISPLAY_HALF_WINDOW_MESSAGE = 0x01;
    private final int OPEN_PERSONAL_MESSAGE = 0x03;
    private final int OPEN_TIPS_PAGE = 0x04;
    float firstRawX = 0, firstRawY = 0, lastRawX = 0, lastRawY = 0;
    private View mFlowLayout;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private Context mContext;
    private int viewWidth;
    private int viewHeight;
    private ImageView mImageView;
    private int mScreenWidth;
    private ValueAnimator mValueAnimator;
    private boolean mIsAddView = false;
    private Vibrator mVibrator;//手机震动
    private boolean mIsVibrator = false;
    //是否有新消息
    private boolean isHavenNewMessage = false;
    private ImageView mLeftNewTips;
    private ImageView mRightNewTips;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DISPLAY_HALF_WINDOW_MESSAGE:
                    rotateEye(false,0);
                    break;
                case OPEN_PERSONAL_MESSAGE:
                    rotateEye(true,0);
                    break;
                case OPEN_TIPS_PAGE:
                    int type = msg.arg1;
                    rotateEye(true,type);
                    break;
            }

        }
    };

    private void rotateEye(boolean isOpenPersonal, final int type) {
        Log.e("xsy", "mHandler handleMessage");
        mImageView.setImageResource(R.drawable.gamecat_float_window_selector);
        Collect.getInstance().custom(CustomId.id_152000);
        setImageViewEnable(false);
        if (FloatWindowManager.getInstance(mContext).mIsLeftDisplay) {
            mLayoutParams.x = 0;
            scrollTo(viewWidth / 2, 0);
            mImageView.setRotation(0);
        } else {
            mLayoutParams.x = FloatWindowManager.getInstance(mContext).getScreenWidth();
            scrollTo(-viewWidth / 2, 0);
            mImageView.setRotation(180);
        }
        if (isOpenPersonal) {
            mImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (type != 0){
                        openPersonalAction(mContext,type);
                    }else {
                        openPersonalAction(mContext);
                    }
                }
            }, 100);
        }
    }

    private FloatWindowBottom mFloatWindowBottom;

    public FlowWindowSmallView(Context context,boolean isOPenPage,int type) {
        super(context);
        this.mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = FloatWindowManager.getInstance(mContext).getScreenWidth();
        init(isOPenPage,type);
        mFloatWindowBottom = new FloatWindowBottom(mContext);
    }


    /**
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHandler.removeMessages(DISPLAY_HALF_WINDOW_MESSAGE);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstRawX = event.getRawX();
                firstRawY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                lastRawX = event.getRawX();
                lastRawY = event.getRawY();
                mLayoutParams.x = (int) event.getRawX() - viewWidth / 2;
                mLayoutParams.y = (int) event.getRawY() - viewHeight / 2;
                if (FloatWindowManager.getInstance(mContext).mIsLeftDisplay) {
                    int dx = getScrollX() - (int) (lastRawX - firstRawX);
                    if (dx >= 0 && dx < viewWidth / 2) {
                        scrollTo(dx, 0);
                    } else {
                        scrollTo(0, 0);
                        //刷新
                        mWindowManager.updateViewLayout(mFlowLayout, mLayoutParams);
                        addViewForWindow(mFloatWindowBottom, mWindowManager);
                        moveViewVibrator();
                    }
                } else {
                    int dx = getScrollX() - (int) (lastRawX - firstRawX);
                    if (dx > -viewWidth / 2 && dx <= 0) {
                        scrollTo(dx, 0);
                    } else {
                        scrollTo(0, 0);
                        //刷新
                        mWindowManager.updateViewLayout(mFlowLayout, mLayoutParams);
                        addViewForWindow(mFloatWindowBottom, mWindowManager);
                        moveViewVibrator();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                lastRawX = event.getRawX();
                lastRawY = event.getRawY();
                float dx = Math.abs(lastRawX - firstRawX);
                float dy = Math.abs(lastRawY - firstRawY);
                int distance = getResources().getDimensionPixelSize(R.dimen.float_window_distance);
                //点击彩色图标
                if (dx < distance && dy < distance) {
//                    openBigWindow();
//                    Collect.getInstance().custom(CustomId.id_151000);
                    if (isHavenNewMessage){//有新消息提醒
                        openBigWindow(FloatWindowManager.NEW_GIFT_TIPS,"新礼包");
                    }else {//没新消息提醒，打开个人中心
                        eyeAnimation(true,0);
                    }
                } else {
                    //移动彩色图标和灰色图标
                    moveSmallView(event);
                }
                //点击灰色图标
                if (!mImageView.isEnabled()) {
                    mImageView.setEnabled(true);
                    mImageView.setRotation(0);
                    scrollTo(0, 0);
                }
                removeViewForWindow(mFloatWindowBottom, mWindowManager);

                if (isViewCovered()) {
                    FloatWindowService.getInstance().openShakeDialog();
                }
                break;
            default:
                break;
        }
        return false;

    }

    private void moveSmallView(final MotionEvent event) {
        int startX = mLayoutParams.x;
        if (mScreenWidth / 2 >= lastRawX) {
            mValueAnimator = ValueAnimator.ofInt(startX, 0);
            FloatWindowManager.getInstance(getContext()).mIsLeftDisplay = true;
        } else {
            mValueAnimator = ValueAnimator.ofInt(startX, mScreenWidth);
            FloatWindowManager.getInstance(getContext()).mIsLeftDisplay = false;
        }

        if (isHavenNewMessage) {
            if (mScreenWidth / 2 >= lastRawX) {
                setDisplayNewTips(false, true);
            } else {
                setDisplayNewTips(true, false);
            }
        } else {
            setDisplayNewTips(false, false);
        }
        mValueAnimator.setDuration(250);
        mValueAnimator.setTarget(mWindowManager);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                mLayoutParams.x = value;
                int positionY = (int) event.getRawY() - viewHeight / 2;
                FloatWindowManager.getInstance(getContext()).mWindowHeightPosition = positionY;
                if (mContext != null && !((Activity) mContext).isFinishing()) {
                    mWindowManager.updateViewLayout(mFlowLayout, mLayoutParams);
                }

            }

        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                eyeAnimation(false,0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.start();
    }


    private void eyeAnimation(boolean isOpenPersonal,int type) {
        int duration = 0;
        if(type == 0){//有消息提醒跳转页面，不做眨眼动画操作
            mImageView.setImageResource(R.drawable.game_cat_eye_animation);
            AnimationDrawable animationDrawable = (AnimationDrawable) mImageView.getDrawable();
            animationDrawable.start();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                duration += animationDrawable.getDuration(i);
            }
        }

        if (isOpenPersonal) {
            if (type != 0){
                Message message = mHandler.obtainMessage();
                message.arg1 = type;
                message.what = OPEN_TIPS_PAGE;
                mHandler.sendMessage(message);
            }else {
                mHandler.sendEmptyMessageDelayed(OPEN_PERSONAL_MESSAGE, duration);
            }
        } else {
            mHandler.sendEmptyMessageDelayed(DISPLAY_HALF_WINDOW_MESSAGE, duration);
        }
        Collect.getInstance().custom(CustomId.id_150000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        removeHandler();
    }

    private void init(boolean isOPenPage,int type) {
        mFlowLayout = LayoutInflater.from(mContext).inflate(R.layout.gamecat_float_small_window, this);
        mImageView = (ImageView) mFlowLayout.findViewById(R.id.id_image_view_game_cat);
        mLeftNewTips = (ImageView) mFlowLayout.findViewById(R.id.id_img_new_tips_left);
        mRightNewTips = (ImageView) mFlowLayout.findViewById(R.id.id_img_new_tips_right);
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        mImageView.measure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = mImageView.getMeasuredWidth();
        viewHeight = mImageView.getMeasuredHeight();
        if (isHavenNewMessage) {
            if (FloatWindowManager.getInstance(mContext).mIsLeftDisplay){
                setDisplayNewTips(false, true);
            }else {
                setDisplayNewTips(true, false);
            }
        } else {
            setDisplayNewTips(false, false);
        }
        if (isOPenPage){
            eyeAnimation(true,type);
        }else {
            eyeAnimation(false,0);

        }
        //获取Vibrator震动服务
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setImageViewEnable(boolean enable) {
        if (mImageView != null) {
            mImageView.setEnabled(enable);
        }
    }

    public void setImageViewRotation(float rotation) {
        if (mImageView != null) {
            mImageView.setRotation(rotation);
        }
    }

    public int getViewWidth() {
        return this.viewWidth;
    }

    public int getViewHeight() {
        return this.viewHeight;
    }

    public WindowManager.LayoutParams getParams() {
        return this.mLayoutParams;
    }

    public void setParams(WindowManager.LayoutParams mLayoutParams) {
        this.mLayoutParams = mLayoutParams;
    }

    /**
     *
     *
     * @param type       具体跳转到页面
     * @param message       提示消息
     */
    private void openBigWindow(int type,String message) {
        FloatWindowManager.getInstance(mContext).createBigWindow(type,message);
    }

    public void removeHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    public void addViewForWindow(FloatWindowBottom view, WindowManager windowManager) {
        if (windowManager == null || mIsAddView) {
            return;
        }

        WindowManager.LayoutParams mBottomLayoutParams = new WindowManager.LayoutParams();
        //设置window type
        mBottomLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        //设置图片格式，效果为背景透明
        mBottomLayoutParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mBottomLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        //调整悬浮窗显示的停靠位置为左侧置顶
        mBottomLayoutParams.gravity = Gravity.START | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mBottomLayoutParams.x = 0;
        int screenHeight = FloatWindowManager.getInstance(mContext).getScreenHeight();
        mBottomLayoutParams.y = screenHeight - view.getViewHeight();
        //设置悬浮窗口长宽数据
        if (screenHeight > mScreenWidth) {
            mScreenWidth = screenHeight;
        }
        mBottomLayoutParams.width = mScreenWidth;
        mBottomLayoutParams.height = view.getViewHeight();
        windowManager.addView(view, mBottomLayoutParams);
        mIsAddView = true;

    }


    public void removeViewForWindow(FloatWindowBottom view, WindowManager windowManager) {
        if (windowManager == null || view == null || !mIsAddView) {
            return;
        }
        windowManager.removeView(view);
        mIsAddView = false;
    }


    private boolean isViewCovered() {
        Rect viewRect = new Rect();
        mImageView.getGlobalVisibleRect(viewRect);
        int screenHeight = FloatWindowManager.getInstance(mContext).getScreenHeight();
        int smallHeight = screenHeight - mLayoutParams.y;
        int bottomHeight = mFloatWindowBottom.getViewHeight();
        int smallWidth = mLayoutParams.x;
        int left = mScreenWidth / 2 - mFloatWindowBottom.getHideViewWidth() / 2;
        int right = mScreenWidth / 2 + mFloatWindowBottom.getHideViewWidth() / 2;
        if (bottomHeight > smallHeight && (smallWidth > left && smallWidth < right)) {
            mFloatWindowBottom.getHideView().setEnabled(true);
            return true;
        } else {
            mFloatWindowBottom.getHideView().setEnabled(false);
        }
        return false;
    }


    private void moveViewVibrator() {
        if (isViewCovered() && !mIsVibrator) {
            mVibrator.vibrate(300);
            mIsVibrator = true;
        }

        if (!isViewCovered()) {
            mIsVibrator = false;
        }
    }

    /**
     * 跳转到个人中心
     *
     * @param context
     */
    private void openPersonalAction(Context context) {
        Collect.getInstance().custom(CustomId.id_140000);
        Small.openUri("personal?page=1", context);
    }

    /**
     * 有消息提醒　　跳转到具体对应页面
     *
     * @param context
     * @param type
     */
    private void openPersonalAction(Context context,int type) {
        Collect.getInstance().custom(CustomId.id_140000);
        if (type == FloatWindowManager.NEW_GIFT_TIPS){
            Small.openUri("personal?page=1&type="+type, context);
        }else if (type == FloatWindowManager.BIND_SECERE_PHONE){
            Small.openUri("personal?page=0&type="+type, context);
        }
    }

    /**
     * 新消息提醒
     *
     * @param left  　　左侧显示提示
     * @param right 右侧显示提示
     */
    private void setDisplayNewTips(boolean left, boolean right) {
        if (left) {
            mLeftNewTips.setVisibility(VISIBLE);
        } else {
            mLeftNewTips.setVisibility(GONE);
        }

        if (right) {
            mRightNewTips.setVisibility(VISIBLE);
        } else {
            mRightNewTips.setVisibility(GONE);
        }
    }
}
