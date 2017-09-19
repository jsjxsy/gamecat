package com.youximao.sdk.app.floatwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by admin on 2017/3/16.
 */

public class FloatWindowBottom extends LinearLayout implements View.OnClickListener {
    private final Context mContext;
    private View mRootView;
    private int viewWidth;
    private int viewHeight;
    private TextView mHideView;
    private int hideViewWidth;

    public FloatWindowBottom(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.gamecat_float_window_bottom, this);
        mHideView = (TextView) mRootView.findViewById(R.id.id_text_view_hide);

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        mRootView.measure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = mRootView.getMeasuredWidth();
        viewHeight = mRootView.getMeasuredHeight();

        mHideView.measure(widthMeasureSpec, heightMeasureSpec);
        hideViewWidth = mHideView.getMeasuredWidth();
    }

    @Override
    public void onClick(View v) {

    }

    public TextView getHideView() {
        return this.mHideView;
    }

    public int getViewWidth() {
        return this.viewWidth;
    }

    public int getViewHeight() {
        return this.viewHeight;
    }

    public int getHideViewWidth() {
        return this.hideViewWidth;
    }


}