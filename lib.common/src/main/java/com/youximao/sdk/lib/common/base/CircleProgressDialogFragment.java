package com.youximao.sdk.lib.common.base;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.youximao.sdk.lib.common.R;

/**
 * Created by zhan on 17-3-15.
 */
public class CircleProgressDialogFragment extends BaseFragment {
    protected final static String WAITING_MESSAGE = "waitingMessage";
    protected TextView mWaitingTextView;
    private ImageView mPprogressBar;


    public static CircleProgressDialogFragment getInstance(String waitingText) {
        CircleProgressDialogFragment fragment = new CircleProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString(WAITING_MESSAGE, waitingText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        mWaitingTextView = (TextView) view.findViewById(R.id.waiting_text_view);
        mPprogressBar = (ImageView) view.findViewById(R.id.img_progress_bar);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mWaitingTextView.setText(bundle.getString(WAITING_MESSAGE, "加载中。。。"));
        }

        RotateAnimation mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(1000 * 1);
        mRotateAnimation.setRepeatCount(-1);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mPprogressBar.startAnimation(mRotateAnimation);
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_circle_progress_dialog;
    }
}
