package com.example.myuidemo;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.views.view.ReactViewGroup;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReactSmartPullRefreshLayoutManager extends ViewGroupManager<ReactSmartPullRefreshLayout> {
    public final static String REACT_CLASS = "SPullRefreshLayout";
    String TAG = REACT_CLASS;

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected ReactSmartPullRefreshLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
        ReactSmartPullRefreshLayout reactSmartPullRefreshLayout = new ReactSmartPullRefreshLayout(reactContext);
        reactSmartPullRefreshLayout.setEnableOverScrollBounce(false);
        return reactSmartPullRefreshLayout;
    }

    /**
     * SmartPullRefreshLayout会对Header、Content、Footer根据需要调整顺序
     * 因此JSX的所描述的绘制顺序可能与实际的绘制顺序存在差异
     * 导致removeViewAt无法正确销毁特定子View
     * 此处记录View原始位置
     */
    HashMap<Integer, View> childMap = new HashMap<>();

    @Override
    public void addView(ReactSmartPullRefreshLayout parent, View child, int index) {
        if (child instanceof ReactSmartPullRefreshHeader) {
            parent.setRefreshHeader((RefreshHeader) child);
        } else if (child instanceof ReactSmartPullRefreshFooter) {
            parent.setRefreshFooter((RefreshFooter) child);
        } else {
            parent.setRefreshContent(child);
        }
        childMap.put(index, child);
    }

    @Override
    public void removeViewAt(ReactSmartPullRefreshLayout parent, int index) {
        View view = childMap.get(index);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (view == parent.getChildAt(i)) {
                super.removeViewAt(parent, i);
                break;
            }
        }
    }

    void printViewGroupChild(ViewGroup viewGroup, String stage) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            Log.d(TAG, stage + "  printViewGroupChild: " + "index:" + i + "  name:" + child.getClass().getSimpleName());
        }
    }

}
