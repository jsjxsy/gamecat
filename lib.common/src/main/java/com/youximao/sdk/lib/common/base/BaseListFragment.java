package com.youximao.sdk.lib.common.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.youximao.sdk.lib.common.R;
import com.youximao.sdk.lib.common.common.util.PxUtil;
import com.youximao.sdk.lib.common.common.widget.ptr.PtrClassicFrameLayout;
import com.youximao.sdk.lib.common.common.widget.ptr.PtrDefaultHandler;
import com.youximao.sdk.lib.common.common.widget.ptr.PtrFrameLayout;
import com.youximao.sdk.lib.common.common.widget.ptr.PtrHandler;
import com.youximao.sdk.lib.common.common.widget.ptr.loadmore.LoadMoreContainer;
import com.youximao.sdk.lib.common.common.widget.ptr.loadmore.LoadMoreHandler;
import com.youximao.sdk.lib.common.common.widget.ptr.loadmore.LoadMoreListViewContainer;

/**
 * Created by zhan on 17-3-21.
 */
public abstract class BaseListFragment extends BaseFragment{
    protected PtrFrameLayout mPtrFrameLayout;
    protected LoadMoreListViewContainer mLoadMoreListViewContainer;
    protected ListView mListView;
    protected int currentPage;
    protected int pageSize;

    @Override
    public void init(View view) {
        mListView = (ListView) view.findViewById(R.id.lv_gift);
        mPtrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_refresh);
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.ptr_loadmore);

        // 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示
        View headerMarginView = new View(mContext);
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PxUtil.convertPX2DIP(mContext,20)));
        mListView.addHeaderView(headerMarginView);
        //3.设置下拉刷新组件和事件监听
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // here check list view, not content.
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
                return false;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //实现下拉刷新的功能
                sendRefrsh();
            }
        });
        //4.加载更多的组件
        mLoadMoreListViewContainer.setAutoLoadMore(true);//设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        //5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                //加载更多的业务处理
                sendRequestData(false);
            }
        });
    }

    public abstract void sendRequestData(boolean isRefrsh);

    protected void sendRefrsh(){
        currentPage = 0;
        sendRequestData(true);
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_list_fragment;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
