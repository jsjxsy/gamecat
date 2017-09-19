package com.youximao.sdk.lib.common.common.widget.ptr;

import android.content.Context;
import android.util.AttributeSet;

import com.youximao.sdk.lib.common.common.widget.ptr.header.YXMPtrHeader;


public class PtrClassicFrameLayout extends PtrFrameLayout {

    private YXMPtrHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new YXMPtrHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
        disableWhenHorizontalMove(true);
    }

    public YXMPtrHeader getHeader() {
        return mPtrClassicHeader;
    }




}
