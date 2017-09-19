package com.youximao.sdk.lib.common.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youximao.sdk.lib.common.R;

/**
 * Created by admin on 2016/10/24.
 */

public class ProgressDialogFragment extends BaseFragment {

    protected final static String WAITING_MESSAGE = "waitingMessage";
    protected String mWaitingText;
    protected TextView mWaitingTextView;

    public static ProgressDialogFragment getInstance(String waitingText) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString(WAITING_MESSAGE, waitingText);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLocalLayoutId(), container, false);
        init(view);
        return view;
    }

    @Override
    public void init(View view) {
        mWaitingTextView = (TextView) view.findViewById(R.id.waiting_text_view);
        initRuntime(getArguments());
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_progress_dialog;
    }

    @Override
    public int getLayoutId(String layoutName) {
        return super.getLayoutId(layoutName);
    }

    public void initRuntime(Bundle args) {
        if (args != null) {
            mWaitingText = args.getString(WAITING_MESSAGE);
            mWaitingTextView.setText(mWaitingText);
        }
    }

}
