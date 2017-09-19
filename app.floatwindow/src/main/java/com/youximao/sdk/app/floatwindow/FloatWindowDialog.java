package com.youximao.sdk.app.floatwindow;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.youximao.sdk.lib.common.base.BaseFragment;

/**
 * Created by admin on 2017/3/16.
 */

public class FloatWindowDialog extends BaseFragment {
    private TextView mTextViewNoDisturb;
    private TextView mTextViewCancelButton;
    private TextView mTextViewConfirmButton;
    private ImageView mImageViewShake;

    public static FloatWindowDialog newInstance() {
        Bundle args = new Bundle();
        FloatWindowDialog fragment = new FloatWindowDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        mImageViewShake = (ImageView) view.findViewById(R.id.id_image_view_shake);
        mTextViewNoDisturb = (TextView) view.findViewById(R.id.id_text_view_no_disturb);
        mTextViewNoDisturb.setOnClickListener(this);
        mTextViewCancelButton = (TextView) view.findViewById(R.id.id_text_view_cancel_button);
        mTextViewCancelButton.setOnClickListener(this);
        mTextViewConfirmButton = (TextView) view.findViewById(R.id.id_text_view_confirm_button);
        mTextViewConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ShakeAnimation();
    }

    private void ShakeAnimation() {
        RotateAnimation animation = new RotateAnimation(-30, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(250);
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setFillAfter(true);
        mImageViewShake.setAnimation(animation);
        animation.start();
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_float_window_dialog;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.id_text_view_cancel_button) {
            FloatWindowService.getInstance().resetShake();
            getActivity().finish();
        } else if (v.getId() == R.id.id_text_view_confirm_button) {
            getActivity().finish();
        }
    }
}
