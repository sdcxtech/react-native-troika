package com.reactnative.pulltorefresh;

import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
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
        float dragRate = 0.8f;
        float maxDragRate = 9.99f;
        pullToRefresh.setDragRate(dragRate);
        pullToRefresh.setHeaderMaxDragRate(maxDragRate);
        pullToRefresh.setFooterMaxDragRate(maxDragRate);
        pullToRefresh.setEnableOverScrollBounce(false);
        return pullToRefresh;
    }

    /**
     * SmartPullRefreshLayout会对Header、Content、Footer根据需要调整顺序
     * 因此JSX的所描述的绘制顺序可能与实际的绘制顺序存在差异
     * 导致removeViewAt无法正确销毁特定子View
     * 此处记录View原始位置
     */
    HashMap<Integer, View> reactChildMap = new HashMap<>();

    @Override
    public void addView(PullToRefresh parent, View child, int index) {
        if (child instanceof PullToRefreshHeader) {
            parent.setRefreshHeader((RefreshHeader) child);
        } else if (child instanceof PullToRefreshFooter) {
            parent.setRefreshFooter((RefreshFooter) child);
        } else {
            parent.setRefreshContent(child);
        }
        reactChildMap.put(index, child);
    }

    @Override
    public void removeViewAt(PullToRefresh parent, int index) {
        View view = reactChildMap.get(index);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (view == parent.getChildAt(i)) {
                super.removeViewAt(parent, i);
                break;
            }
        }
        reactChildMap.remove(index);
    }
}
