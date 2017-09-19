package com.youximao.sdk.app.logout.fragment;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youximao.sdk.app.logout.R;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.callback.CancelUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

import net.wequick.small.Small;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhan on 17-3-16.
 */
public class LogoutFragment extends BaseFragment {
    private LinearLayout mLlGift;
    private LinearLayout mLlForum;
    private Button mBtnLogout;
    private RelativeLayout mRlGiftForum;
    private boolean isLogin;

    public static LogoutFragment getInstance() {
        LogoutFragment fragment = new LogoutFragment();
        return fragment;
    }

    @Override
    public void init(View view) {
        mLlGift = (LinearLayout) view.findViewById(R.id.ll_gift);
        mLlForum = (LinearLayout) view.findViewById(R.id.ll_forum);
        mBtnLogout = (Button) view.findViewById(R.id.btn_logout);
        mRlGiftForum = (RelativeLayout) view.findViewById(R.id.rl_gift_forum);
        mLlGift.setOnClickListener(this);
        mLlForum.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        isLogin = Config.isLogin();
        String giftState = AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_BBS);
        String forumState = AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_GIFT_PACKAGE);
        if (!isLogin) {//未登录状态二者都显示点击跳转到登陆界面
            mLlGift.setVisibility(View.VISIBLE);
            mLlForum.setVisibility(View.VISIBLE);
        } else {//登陆状态按正常逻辑
            if (TextUtils.equals("1", giftState) || TextUtils.equals("1", forumState)) {//二者显示其一
                if (TextUtils.equals("1", giftState)) {
                    mLlGift.setVisibility(View.VISIBLE);
                } else {
                    mLlGift.setVisibility(View.GONE);
                }
                if (TextUtils.equals("1", forumState)) {
                    mLlForum.setVisibility(View.VISIBLE);
                } else {
                    mLlForum.setVisibility(View.GONE);
                }
            } else {//两者都没有
                mLlGift.setVisibility(View.GONE);
                mLlForum.setVisibility(View.GONE);
                mRlGiftForum.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.game_cat_logout_fragment_layout;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_gift://礼包
                if (isLogin) {
                    Small.openUri("personal?page=1", mContext);
                    getActivity().finish();
                } else {
                    CancelUtil.cancelLogin(mContext);
                    getActivity().finish();
                }
                break;
            case R.id.ll_forum://论坛
                if (isLogin) {
                    Small.openUri("personal?page=2", mContext);
                    getActivity().finish();
                } else {
                    CancelUtil.cancelLogin(mContext);
                    getActivity().finish();
                }
                break;
            case R.id.btn_logout://退出游戏
                //发送广播
                Intent intent = new Intent();
                intent.setAction("com.youximao.sdk.app.gamelogout");
                intent.putExtra("result", "success");
                intent.putExtra("message", "退出游戏");
                LocalBroadcastManager mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
                mLocalBroadcastManager.sendBroadcast(intent);
                getActivity().finish();
                break;
        }
    }
}
