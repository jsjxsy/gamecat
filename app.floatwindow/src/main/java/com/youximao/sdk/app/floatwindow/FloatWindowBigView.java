package com.youximao.sdk.app.floatwindow;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by admin on 2016/10/14.
 */

public class FloatWindowBigView extends RelativeLayout implements View.OnClickListener {
    private final int DELAY_TIME = 2 * 1000;
    private final int HIDE_WINDOW_MESSAGE = 0x02;
    private final int AUTOM_HIDE_WINDOW_MESSAGE = 0x04;//过了设定时间，自动隐藏
    private int viewWidth;
    private int viewHeight;
    private Context mContext;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE_WINDOW_MESSAGE:
                    FloatWindowManager.getInstance(getContext()).closeBigWindowAnimation(type);
                    break;
                case AUTOM_HIDE_WINDOW_MESSAGE:
                    FloatWindowManager.getInstance(getContext()).closeBigWindowAnimation(-1);
                    break;
            }

        }
    };
    private ImageView mGameCatImageView;
    private TextView mNewTipsMsg;
    private String message;
    private int type;
    private ImageView mNewTipsIcon;

    public FloatWindowBigView(Context context, int type, String message) {
        super(context);
        this.mContext = context;
        this.message = message;
        this.type = type;
        init();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_layout_big_window) {
            mGameCatImageView.setEnabled(false);
            FloatWindowManager.getInstance(getContext()).closeBigWindowAnimation(type);
            mHandler.removeMessages(HIDE_WINDOW_MESSAGE);
            mGameCatImageView.setEnabled(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("xsy", "mBigWindow  onTouch");
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Rect rect = new Rect();
            getLocalVisibleRect(rect);
            if (!rect.contains(x, y)) {
                Log.e("xsy", "closeBigWindowAnimation  onTouch");
                FloatWindowManager.getInstance(getContext()).closeBigWindowAnimation(-1);
                mHandler.removeMessages(HIDE_WINDOW_MESSAGE);
            }
            Log.e("xsy", "onTouch : " + x + ", " + y + ", rect: "
                    + rect);
        }


        return super.onTouchEvent(event);

    }

    private void init() {
        View rootView;
        if (FloatWindowManager.getInstance(getContext()).mIsLeftDisplay) {
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.gamecat_float_big_window, this);
        } else {
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.gamecat_float_big_window_right, this);
        }

        View view = rootView.findViewById(R.id.id_layout_big_window);
        mGameCatImageView = (ImageView) rootView.findViewById(R.id.id_image_view_game_cat_icon);
        view.setOnClickListener(this);
        mNewTipsMsg = (TextView) rootView.findViewById(R.id.id_text_new_tips);
        mNewTipsMsg.setText(message);
        mNewTipsIcon = (ImageView) rootView.findViewById(R.id.id_image_new_tips);
        if (type == FloatWindowManager.NEW_GIFT_TIPS) {
            mNewTipsIcon.setImageResource(R.drawable.game_cat_float_window_gift_tips);
        } else if (type == FloatWindowManager.BIND_SECERE_PHONE) {
            mNewTipsIcon.setImageResource(R.drawable.game_cat_float_window_phone_tips);
        }

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = view.getMeasuredWidth();
        viewHeight = view.getMeasuredHeight();
        mHandler.sendEmptyMessageDelayed(AUTOM_HIDE_WINDOW_MESSAGE, DELAY_TIME);
    }

    public void setLayoutWidth(View view, int count) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (count == 2) {
            viewWidth = getResources().getDimensionPixelSize(R.dimen.float_window_one);
        } else if (count == 1) {
            viewWidth = getResources().getDimensionPixelSize(R.dimen.float_window_two);
        }
        layoutParams.width = viewWidth;
        view.setLayoutParams(layoutParams);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeHandler();
    }

    public int getViewWidth() {
        Log.e("xsy", "big viewWidth" + viewWidth);
        return this.viewWidth;
    }

    public int getViewHeight() {
        Log.e("xsy", "big viewHeight" + viewHeight);
        return this.viewHeight;
    }

    public void removeHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
