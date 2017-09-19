package com.youximao.sdk.app.forum.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.youximao.sdk.app.forum.fragment.ForumFragment;
import com.youximao.sdk.lib.common.base.BaseActivity;


/**
 * Created by admin on 2016/12/6.
 */

public class ForumActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public Fragment getFragment() {
        return ForumFragment.newInstance();
    }
}
