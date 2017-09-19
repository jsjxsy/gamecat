package com.youximao.sdk.app.personal.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.adapter.GiftPackageAdapter;
import com.youximao.sdk.app.personal.model.GiftPackage;
import com.youximao.sdk.lib.common.base.BaseListFragment;

import java.util.ArrayList;

/**
 * Created by zhan on 17-3-20.
 * <p/>
 * 礼包
 */
public class GiftPackageFragment extends BaseListFragment{
    private TextView myGift;
    private GiftPackageAdapter mAdapter;
    private ArrayList<GiftPackage> mData;
    private boolean isJump;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1){

                for (int i = 0; i < 20; i++) {
                    GiftPackage giftPackage = new GiftPackage();
                    giftPackage.setmId(""+i);
                    mData.add(giftPackage);
                }
                mAdapter.setArrayList(mData);
                mLoadMoreListViewContainer.loadMoreFinish(false, false);
            }else if (msg.what == 2){
                mData.clear();
                mData = new ArrayList<GiftPackage>();
                for (int i = 0; i < 20; i++) {
                    GiftPackage giftPackage = new GiftPackage();
                    giftPackage.setmId(""+i);
                    mData.add(giftPackage);
                }
                mAdapter.setArrayList(mData);
                mPtrFrameLayout.refreshComplete();
                //第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                mLoadMoreListViewContainer.loadMoreFinish(false, false);
            }
        }
    };

    public static GiftPackageFragment getInstance(boolean isJump){
        GiftPackageFragment fragment = new GiftPackageFragment();
        Bundle args = new Bundle();
        args.putBoolean("isjump",isJump);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            isJump = bundle.getBoolean("isjump",false);
        }
        if (isJump){
            // TODO: 17-3-25 自动跳转
        }
    }

    @Override
    public void init(View view) {
        super.init(view);
        myGift = (TextView) view.findViewById(R.id.tv_my_gift);
        myGift.setOnClickListener(this);

        mAdapter = new GiftPackageAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mData = new ArrayList<GiftPackage>();
        for (int i = 0; i < 20; i++) {
            GiftPackage giftPackage = new GiftPackage();
            giftPackage.setmId(""+i);
            mData.add(giftPackage);
        }
        mAdapter.setArrayList(mData);
        mLoadMoreListViewContainer.loadMoreFinish(false, false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_my_gift://我的礼包
                break;
        }
    }

    @Override
    public void sendRequestData(boolean isRefrsh) {
        if (isRefrsh){
            mHandler.sendEmptyMessageDelayed(2,3000);
        }else {
            mHandler.sendEmptyMessageDelayed(1,3000);
        }
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_gift_package_fragment;
    }
}
