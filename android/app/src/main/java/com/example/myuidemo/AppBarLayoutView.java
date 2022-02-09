package com.example.myuidemo;

import android.content.Context;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.material.appbar.AppBarLayout;

import java.util.HashMap;
import java.util.Map;

public class AppBarLayoutView extends AppBarLayout {
    public AppBarLayoutView(@NonNull Context context) {
        super(context);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(width, height);
        this.setLayoutParams(params);
    }

    private Map<View, Size> mChildViewSizeMap = new HashMap<>();
    private Size mSize;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        refreshChildrenSize();
        boolean shouldManuallySetDimension = false;

        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams == null || (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT)) {
            shouldManuallySetDimension = isLayoutHasReactViewGroupChild();
        }

        if (shouldManuallySetDimension) {
            Size size = getLayoutSizeManually();
            setMeasuredDimension(size.getWidth(), size.getHeight());
            layout(getLeft(), getTop(), getRight(), getBottom());
            if (!size.equals(mSize)) {
                mSize = size;
                requestLayout();
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private Size getLayoutSizeManually() {
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

    private void refreshChildrenSize() {
        int childCount = this.getChildCount();
        Map<View, Size> childrenSizeMap = new HashMap<>();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            childrenSizeMap.put(view, new Size(view.getMeasuredWidth(), view.getMeasuredHeight()));
        }
        mChildViewSizeMap = childrenSizeMap;
    }

    private boolean isLayoutHasReactViewGroupChild() {
        for (Map.Entry<View, Size> entry : mChildViewSizeMap.entrySet()) {
            if (entry.getKey() instanceof ReactViewGroup) {
                return true;
            }
        }
        return false;
    }
}

