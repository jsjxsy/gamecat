package com.youximao.sdk.app.personal.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.fragment.ForumFragment;
import com.youximao.sdk.app.personal.fragment.GiftPackageFragment;
import com.youximao.sdk.app.personal.fragment.HelpFragment;
import com.youximao.sdk.app.personal.fragment.PersonalFragment;
import com.youximao.sdk.lib.common.base.BaseActivity;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.widget.CircleImageView;

import net.wequick.small.Small;

/**
 * Created by admin on 2016/10/17.
 */

public class PersonalActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private CheckBox mcbPersonal;
    private CheckBox mcbGift;
    private CheckBox mcbForum;
    private CheckBox mcbHelp;
    private Fragment mFragment;
    private int currentID = -1;
    private FragmentManager fragmentManager;
    private Fragment fragments[] = new Fragment[4];
    private String page;
    private LinearLayout mContainer;

    private CircleImageView mPersonIcon;
    private String type;

    private Handler mHanalder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (PersonalActivity.this != null && !PersonalActivity.this.isFinishing()) {
                finish();
            }
        }
    };


    @Override
    public Fragment getFragment() {
        if (TextUtils.isEmpty(type)){
            mFragment = PersonalFragment.getInstance(false);
        }else {
            mFragment = PersonalFragment.getInstance(true);
        }
        return mFragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("xsy", "onConfigurationChanged");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = Small.getUri(this);
        if (uri != null) {
            page = uri.getQueryParameter("page");
            type = uri.getQueryParameter("type");
        }
        fragmentManager = this.getFragmentManager();
        initView();

        openAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        mPersonIcon = (CircleImageView) findViewById(R.id.gamecat_person_icon);
        mContainer = (LinearLayout) findViewById(R.id.ll_container);
        mcbPersonal = (CheckBox) findViewById(R.id.gamecat_cb_personal);
        mcbGift = (CheckBox) findViewById(R.id.gamecat_cb_gift);
        mcbForum = (CheckBox) findViewById(R.id.gamecat_cb_forum);
        mcbHelp = (CheckBox) findViewById(R.id.gamecat_cb_help);
        mcbPersonal.setOnCheckedChangeListener(this);
        mcbGift.setOnCheckedChangeListener(this);
        mcbForum.setOnCheckedChangeListener(this);
        mcbHelp.setOnCheckedChangeListener(this);
        findViewById(R.id.id_text_view_invisible).setOnClickListener(this);
        if (fragments[0] == null) {
            if (TextUtils.equals(type,"1")){
                fragments[0] = PersonalFragment.getInstance(true);
            }else {
                fragments[0] = PersonalFragment.getInstance(false);
            }
        }
        addFragment(0);

        if ("1".equals(page)) {
            setChoice(1);
            if (fragments[1] == null) {
                if (TextUtils.equals(type,"2")){
                    fragments[1] = GiftPackageFragment.getInstance(true);
                }else {
                    fragments[1] = GiftPackageFragment.getInstance(false);
                }
            }
            addFragment(1);
        } else if ("2".equals(page)) {
            setChoice(2);
            if (fragments[2] == null) {
                fragments[2] = ForumFragment.newInstance();
            }
            addFragment(2);
        } else {
            setChoice(0);
            if (fragments[0] == null) {
                if (TextUtils.equals(type,"1")){
                    fragments[0] = PersonalFragment.getInstance(true);
                }else {
                    fragments[0] = PersonalFragment.getInstance(false);
                }
            }
            addFragment(0);
        }

        String url1 = "http://disimg.youximao.tv/2017011711233818071285259011.jpg";
        Glide.with(this).load(url1).error(R.drawable.game_cat_cat).into(mPersonIcon);
    }

    @Override
    public int getLayoutId(String layoutName) {
        return R.layout.gamecat_personal_activity_main;
    }

    @Override
    protected boolean isOwnmanagementFragment() {
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int id = buttonView.getId();
            switch (id) {
                case R.id.gamecat_cb_personal:
                    setChoice(0);
                    if (fragments[0] == null) {
                        if (TextUtils.equals(type,"1")){
                            fragments[0] = PersonalFragment.getInstance(true);
                        }else {
                            fragments[0] = PersonalFragment.getInstance(false);
                        }
                    }
                    addFragment(0);
                    break;
                case R.id.gamecat_cb_gift:
                    setChoice(1);
                    if (fragments[1] == null) {
                        if (TextUtils.equals(type,"2")){
                            fragments[1] = GiftPackageFragment.getInstance(true);
                        }else {
                            fragments[1] = GiftPackageFragment.getInstance(false);
                        }
                    }
                    addFragment(1);
                    break;
                case R.id.gamecat_cb_forum:
                    setChoice(2);
                    if (fragments[2] == null) {
                        fragments[2] = ForumFragment.newInstance();
                    }
                    addFragment(2);
                    break;
                case R.id.gamecat_cb_help:
                    setChoice(3);
                    if (fragments[3] == null) {
                        fragments[3] = HelpFragment.getInstance();
                    }
                    addFragment(3);
                    break;
            }
        }
    }

    private void addFragment(int index) {
        if (currentID == index) return;
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        if (fragments[index].isAdded()) {
            beginTransaction.hide(fragments[currentID]);
            beginTransaction.show(fragments[index]);
        } else {
            beginTransaction.add(R.id.fragment, fragments[index],
                    fragments[index].getClass().getSimpleName());
            if (currentID >= 0) {
                beginTransaction.hide(fragments[currentID]);
            }

        }
        beginTransaction.commitAllowingStateLoss();
        currentID = index;
    }

    private void setChoice(int position) {
        switch (position) {
            case 0:
                mcbPersonal.setChecked(true);
                mcbGift.setChecked(false);
                mcbForum.setChecked(false);
                mcbHelp.setChecked(false);

                mcbPersonal.setEnabled(false);
                mcbGift.setEnabled(true);
                mcbForum.setEnabled(true);
                mcbHelp.setEnabled(true);
                break;
            case 1:
                mcbPersonal.setChecked(false);
                mcbGift.setChecked(true);
                mcbForum.setChecked(false);
                mcbHelp.setChecked(false);

                mcbPersonal.setEnabled(true);
                mcbGift.setEnabled(false);
                mcbForum.setEnabled(true);
                mcbHelp.setEnabled(true);
                break;
            case 2:
                mcbPersonal.setChecked(false);
                mcbGift.setChecked(false);
                mcbForum.setChecked(true);
                mcbHelp.setChecked(false);
                mcbPersonal.setEnabled(true);
                mcbGift.setEnabled(true);
                mcbForum.setEnabled(false);
                mcbHelp.setEnabled(true);
                break;
            case 3:
                mcbPersonal.setChecked(false);
                mcbGift.setChecked(false);
                mcbForum.setChecked(false);
                mcbHelp.setChecked(true);
                mcbPersonal.setEnabled(true);
                mcbGift.setEnabled(true);
                mcbForum.setEnabled(true);
                mcbHelp.setEnabled(false);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        endAnimation();
        mHanalder.sendEmptyMessageDelayed(1, 450);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            endAnimation();
            mHanalder.sendEmptyMessageDelayed(1, 450);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     *页面打开转场动画
     */
    private void openAnimation(){
        boolean isLandscape = Config.getIsLandscape();
        Animation anim;
        if (isLandscape) {//横屏
            anim = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
        } else {
            anim = AnimationUtils.loadAnimation(this, R.anim.in_from_bottom);
        }
        mContainer.startAnimation(anim);
    }

    /**
     * 页面结束转场动画
     */
    private void endAnimation(){
        boolean isLandscape = Config.getIsLandscape();
        Animation anim;
        if (isLandscape) {//横屏
            anim = AnimationUtils.loadAnimation(this, R.anim.out_to_left);
        } else {
            anim = AnimationUtils.loadAnimation(this, R.anim.out_to_bottom);
        }
        mContainer.startAnimation(anim);
    }
}
