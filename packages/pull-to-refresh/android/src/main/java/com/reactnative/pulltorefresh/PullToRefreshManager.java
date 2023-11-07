package com.reactnative.pulltorefresh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;

import java.util.HashMap;

public class PullToRefreshManager extends ViewGroupManager<PullToRefresh> {
    public final static String REACT_CLASS = "PullToRefresh";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected PullToRefresh createViewInstance(@NonNull ThemedReactContext reactContext) {
        PullToRefresh pullToRefresh = new PullToRefresh(reactContext);
        pullToRefresh.setEnableOverScrollBounce(false);
        pullToRefresh.setEnableOverScrollDrag(true);
        pullToRefresh.setEnableRefresh(false);
        pullToRefresh.setEnableLoadMore(false);
        return pullToRefresh;
    }

    /**
     * SmartPullRefreshLayout 会对 Header、Content、Footer 根据需要调整顺序
     * 因此 JSX 的所描述的绘制顺序可能与实际的绘制顺序存在差异
     * 导致 removeViewAt 无法正确销毁特定子 View
     * 此处记录 View 原始位置
     */
    HashMap<Integer, View> reactChildMap = new HashMap<>();

    @Override
    public void addView(PullToRefresh parent, View child, int index) {
        if (child instanceof PullToRefreshHeader) {
            parent.setEnableRefresh(true);
            parent.setRefreshHeader((RefreshHeader) child);
            if (parent.isLaidOut()) {
                ((PullToRefreshHeader) child).onInitialized(parent.getRefreshKernel(), 0, 0);
            }
        } else if (child instanceof PullToRefreshFooter) {
            parent.setEnableLoadMore(true);
            parent.setRefreshFooter((RefreshFooter) child);
            if (parent.isLaidOut()) {
                ((PullToRefreshFooter) child).onInitialized(parent.getRefreshKernel(), 0, 0);
            }
        } else {
            parent.setRefreshContent(child);
        }
        reactChildMap.put(index, child);
    }

    @Override
    public void removeViewAt(PullToRefresh parent, int index) {
        View child = reactChildMap.get(index);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (child == parent.getChildAt(i)) {
                if (child instanceof PullToRefreshHeader) {
                    parent.setEnableRefresh(false);
                    parent.setOnRefreshListener(null);
                } else if (child instanceof PullToRefreshFooter) {
                    parent.setEnableLoadMore(false);
                    parent.setOnLoadMoreListener(null);
                }
                super.removeViewAt(parent, i);
                break;
            }
        }
        reactChildMap.remove(index);
    }

    @ReactProp(name = "overflow")
    public void setOverflow(PullToRefresh view, @Nullable String overflow) {
        view.setOverflow(overflow);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
