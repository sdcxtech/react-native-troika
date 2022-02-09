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

    Map<View, Size> childViewSizeMap = new HashMap<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();

        boolean shouldManuallySetDimension = false;
        if (layoutParams == null || (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT)) {
            shouldManuallySetDimension = isLayoutHasReactViewGroupChild();
        }

        if (shouldManuallySetDimension) {
            Size size = getLayoutSizeManually();
            setMeasuredDimension(size.getWidth(), size.getHeight());
            layout(getLeft(), getTop(), getRight(), getBottom());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private Size getLayoutSizeManually() {
        int layoutWidth = 0;
        int layoutHeight = 0;
        for (Map.Entry<View, Size> entry : childViewSizeMap.entrySet()) {
            Size size = entry.getValue();
            if (size.getWidth() > layoutWidth) {
                layoutWidth = size.getWidth();
            }
            layoutHeight += size.getHeight();
        }
        return new Size(layoutWidth, layoutHeight);
    }


    @Override
    public void requestLayout() {
        super.requestLayout();
        post(() -> {
            measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        });
    }


    @Override
    public void addView(View child) {
        super.addView(child);
        addChildViewOnDrawObserver(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        addChildViewOnDrawObserver(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        addChildViewOnDrawObserver(child);

    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        addChildViewOnDrawObserver(child);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        refreshViewGroup();
        requestLayout();
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        refreshViewGroup();
        requestLayout();
    }

    private void addChildViewOnDrawObserver(View child) {
        childViewSizeMap.put(child, new Size(0, 0));
        child.getViewTreeObserver().addOnDrawListener(() -> {
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            Size previousChildSize = childViewSizeMap.get(child);
            if (previousChildSize != null && (previousChildSize.getWidth() != width || previousChildSize.getHeight() != height)) {
                childViewSizeMap.put(child, new Size(width, height));
                requestLayout();
            }
        });
    }

    private void refreshViewGroup() {
        int childCount = this.getChildCount();
        Map<View, Size> refreshViewSizeMap = new HashMap<>();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            refreshViewSizeMap.put(view, new Size(view.getMeasuredWidth(), view.getMeasuredHeight()));
        }
        childViewSizeMap = refreshViewSizeMap;
    }

    private boolean isLayoutHasReactViewGroupChild() {
        for (Map.Entry<View, Size> entry : childViewSizeMap.entrySet()) {
            if (entry.getKey() instanceof ReactViewGroup) {
                return true;
            }
        }
        return false;
    }
}

