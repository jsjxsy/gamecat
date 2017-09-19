package com.youximao.sdk.lib.common.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


/**
 * 下滑到底部自动加载ListView
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {

    private OnLoadListener mOnLoadListener;
    private OnScrollListener mOnScrollListener;
    private View mLoadView;
    private boolean mIsLoading;
    private boolean mIsLoadEnable = true;

    /**
     * 回调接口，用于数据加载
     */
    public interface OnLoadListener {
        void loadData();
    }

    public LoadListView(Context context) {
        this(context, null);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
//        mLoadView = LayoutInflater.from(context).inflate(R.layout.gamecat_load_view, null);
//        FrameLayout footerParent = new FrameLayout(context);
//        footerParent.addView(mLoadView);
//        addFooterView(footerParent);
        updateLoadViewVisibility(false);
        super.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (totalItemCount <= visibleItemCount) return;
        if (mIsLoading || !mIsLoadEnable) return;
        int lastItemIndex = firstVisibleItem + visibleItemCount;
        if (lastItemIndex >= totalItemCount && mOnLoadListener != null) {
            load();
        }
    }

    /**
     * 设置load view
     *
     * @param view
     */
    public void setLoadView(View view) {
        mLoadView = view;
    }

    /**
     * 设置load listener
     *
     * @param onLoadListener
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    /**
     * 判断是否正在加载中
     *
     * @return
     */
    public boolean isLoading() {
        return mIsLoading;
    }

    private void updateLoadViewVisibility(boolean visible) {
        if (mLoadView == null) return;
        if (visible) {
            mLoadView.setVisibility(View.VISIBLE);
        } else {
            mLoadView.setVisibility(View.GONE);
        }
    }

    private void load() {
        mIsLoading = true;
        updateLoadViewVisibility(true);
        mOnLoadListener.loadData();
    }

    /**
     * 数据加载结束后调用此方法
     */
    public void onLoadComplete() {
        mIsLoading = false;
        updateLoadViewVisibility(false);
    }

    /**
     * 判断是否可以自动加载
     *
     * @return
     */
    public boolean getLoadEnable() {
        return mIsLoadEnable;
    }

    /**
     * 设置是否启用自动加载功能
     *
     * @param isEnable
     */
    public void setLoadEnable(boolean isEnable) {
        mIsLoadEnable = isEnable;
    }

    /**
     * 兼容ListView原本的OnScrollListener
     *
     * @param l
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }
}
