package com.example.myuidemo.Helper;

import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.views.view.ReactViewGroup;

import java.util.HashMap;
import java.util.Map;

public class LinearLayoutReactViewGroupMeasureHelper {
    private final ViewGroup mDelegatedViewGroup;
    private Map<View, Size> mChildViewSizeMap = new HashMap<>();

    public LinearLayoutReactViewGroupMeasureHelper(ViewGroup viewGroup) {
        mDelegatedViewGroup = viewGroup;
    }

    private void refreshChildrenSize() {
        int childCount = mDelegatedViewGroup.getChildCount();
        Map<View, Size> childrenSizeMap = new HashMap<>();
        for (int i = 0; i < childCount; i++) {
            View view = mDelegatedViewGroup.getChildAt(i);
            childrenSizeMap.put(view, new Size(view.getMeasuredWidth(), view.getMeasuredHeight()));
        }
        mChildViewSizeMap = childrenSizeMap;
    }

    public boolean shouldDelegate() {
        ViewGroup.LayoutParams layoutParams = mDelegatedViewGroup.getLayoutParams();
        if (layoutParams == null || (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT)) {
            return isLayoutHasReactViewGroupChild();
        }
        return false;
    }

    private boolean isLayoutHasReactViewGroupChild() {
        if (mChildViewSizeMap.size() == 0) {
            refreshChildrenSize();
        }
        for (Map.Entry<View, Size> entry : mChildViewSizeMap.entrySet()) {
            String viewClass = entry.getKey().getClass().getName();
            if (isReactNativeView(viewClass)) {
                return true;
            }
        }
        return false;
    }

    boolean isReactNativeView(String className) {
        return className.toLowerCase().startsWith("com.facebook.react.views");
    }

    public Size getLayoutSize() {
        refreshChildrenSize();
        return calculateLayoutSize();
    }

    private Size calculateLayoutSize() {
        int layoutWidth = 0;
        int layoutHeight = 0;

        for (Map.Entry<View, Size> entry : mChildViewSizeMap.entrySet()) {
            Size size = entry.getValue();
            if (size.getWidth() > layoutWidth) {
                layoutWidth = size.getWidth();
            }
            layoutHeight += size.getHeight();
        }
        return new Size(layoutWidth, layoutHeight);
    }
}
