package com.youximao.sdk.lib.common.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youximao.sdk.lib.common.R;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;

/**
 * Created by zhan on 17-3-22.
 */
public class TipsDialogFragment extends BaseFragment {
    public static final int TIPS_SUCCEED = 0;//成功
    public static final int TIPS_FAIL = 1;//失败
    public static final int TIPS_WARNING = 2;//警告

    private TextView mTitle;
    private ImageView mTipsIcon;
    private int type;
    private String tips;
    private long delayMillis;

    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (getActivity() != null && !getActivity().isFinishing()){
                FragmentManagerUtil.getInstance().closeDialogFragment(getActivity(),TipsDialogFragment.this);
            }

        }
    };

    /**
     *
     *
     * @param type              提示框类型
     * @param tips              提示信息
     * @param delayMillis       提示框停留时间
     * @return
     */
    public static TipsDialogFragment getInstance(int type, String tips, long delayMillis){
        TipsDialogFragment fragment = new TipsDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("tips",tips);
        args.putLong("delayMillis",delayMillis);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args= getArguments();
        type = args.getInt("type");
        tips =args.getString("tips");
        delayMillis = args.getLong("delayMillis");
    }

    @Override
    public void init(View view) {
        mTitle = (TextView) view.findViewById(R.id.tv_tips);
        mTipsIcon = (ImageView) view.findViewById(R.id.img_tips_icon);
        switch (type) {
            case TIPS_SUCCEED:
                mTipsIcon.setImageResource(R.drawable.gamecat_succeed_tips_icon);
                break;
            case TIPS_FAIL:
                mTipsIcon.setImageResource(R.drawable.gamecat_fail_tips_icon);
                break;
            case TIPS_WARNING:
                mTipsIcon.setImageResource(R.drawable.gamecat_warning_tips_icon);
                break;
        }
        mTitle.setText(tips);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHanlder.sendEmptyMessageDelayed(1,delayMillis);
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_tips_dialog;
    }
}
