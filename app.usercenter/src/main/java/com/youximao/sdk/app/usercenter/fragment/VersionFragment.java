package com.youximao.sdk.app.usercenter.fragment;

import android.view.View;
import android.widget.TextView;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.config.Config;


/**
 * Created by admin on 2017/3/8.
 */

public class VersionFragment extends BaseFragment {
    private TextView mVersion;

    @Override
    public void init(View view) {
        mVersion = (TextView) view.findViewById(R.id.id_text_view_version_game_cat);
        mVersion.setText("Version: " + Config.getVersion());
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_version;
    }
}
