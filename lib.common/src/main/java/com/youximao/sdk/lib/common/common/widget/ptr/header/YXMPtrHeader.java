package com.youximao.sdk.lib.common.common.widget.ptr.header;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youximao.sdk.lib.common.R;
import com.youximao.sdk.lib.common.common.widget.ptr.PtrFrameLayout;
import com.youximao.sdk.lib.common.common.widget.ptr.PtrUIHandler;
import com.youximao.sdk.lib.common.common.widget.ptr.indicator.PtrIndicator;

/**
 * Created by Administrator on 2017/1/21 0021.
 */

public class YXMPtrHeader extends FrameLayout implements PtrUIHandler {


    private boolean isRunAnimation;
    private Matrix mHeaderImageMatrix;
    private Drawable imageDrawable;
    private float mRotationPivotX, mRotationPivotY;
    private AnimationDrawable frameAnim_play;
    private ImageView mHeaderImage;
    private Drawable drawable3;
    private Drawable drawable2;
    private Drawable drawable4;
    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.gamecat_refresh_header, this);
        mHeaderImage= (ImageView) findViewById(R.id.iv_yxm);
        mHeaderImage.setScaleType(ImageView.ScaleType.MATRIX);
        frameAnim_play=(AnimationDrawable) getResources().getDrawable(R.drawable.gamecat_refresh_play);
        drawable3=   getResources().getDrawable
                (R.drawable.eatfish3);
        drawable2=  getResources().getDrawable
                (R.drawable.eatfish2);
        drawable4= getResources().getDrawable
                (R.drawable.eatfish4);
        mHeaderImageMatrix = new Matrix();
        mHeaderImage.setImageMatrix(mHeaderImageMatrix);
        imageDrawable=  getResources().getDrawable(R.drawable.eatfish1);
        mHeaderImage.setImageDrawable(imageDrawable);
        if (null != imageDrawable) {
            mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
            mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() );
        }
    }

    public YXMPtrHeader(Context context) {
        this(context, null, 0);
        initialize();
    }

    public YXMPtrHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YXMPtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        // 重置头View的动画状态，一般停止刷新动画。
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        // 准备刷新的UI。
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        // 开始刷新的UI动画。
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        // 刷新完成，停止刷新动画。
    }

    /**
     * 开始刷新动画。
     */
    private void startAnimation() {
        if (!isRunAnimation) {
            isRunAnimation = true;
            mHeaderImage.setImageDrawable(frameAnim_play);
            frameAnim_play.start();
        }
    }

    /**
     * 停止刷新动画。
     */
    private void stopAnimation() {
        if (isRunAnimation) {
            isRunAnimation = false;
            mHeaderImage.clearAnimation();
            frameAnim_play.stop();
        }
    }
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
                                   PtrIndicator ptrIndicator) {
        // 手指下拉的时候的状态，我们的下拉动画的控制就是通过这个方法：
        // frame是刷新的root layout。
        // isUnderTouch是手指是否按下，因为还有自动刷新，手指肯定是松开状态。
        // status是现在的加载状态，准备、加载中、完成：PREPARE、LOADING、COMPLETE。
        // ptrIndicator是一些下拉偏移量的参数封装。
        // 获取总的头部可下拉的距离：

        if(status==PtrFrameLayout.PTR_STATUS_PREPARE){
            final int offsetToRefresh = frame.getOffsetToRefresh();
            if(offsetToRefresh==0)return;
            // 获取当前手指已经下拉的距离：
            final int currentPos = ptrIndicator.getCurrentPosY();
            //下拉比例
            float percent = (float) currentPos / offsetToRefresh;

            if(percent<=0.8){
               mHeaderImageMatrix.setScale( percent * 1.25f, percent * 1.25f, mRotationPivotX, mRotationPivotY);//(angle, mRotationPivotX, mRotationPivotY);
                mHeaderImage.setImageMatrix(mHeaderImageMatrix);

            }else{
                mHeaderImageMatrix.reset();
                mHeaderImage.setImageMatrix(mHeaderImageMatrix);
                // 这样就模拟出了下拉时的效果了。
                if (percent <1&&percent>0.8 ) {
                    mHeaderImage.setImageDrawable(drawable2);
                }else if(percent >=1&&percent<1.3){
                    mHeaderImage.setImageDrawable(drawable3);
                }else if(percent >=1.3){

                    mHeaderImage.setImageDrawable(drawable4);

                }

            }


        }else if(status==PtrFrameLayout.PTR_STATUS_LOADING){
            mHeaderImageMatrix.reset();
            mHeaderImage.setImageMatrix(mHeaderImageMatrix);
            startAnimation();
        }else if(status==PtrFrameLayout.PTR_STATUS_INIT){
            mHeaderImageMatrix.reset();
            mHeaderImage.setImageMatrix(mHeaderImageMatrix);
            mHeaderImage.setImageDrawable(imageDrawable);
            stopAnimation();
        }




    }
}