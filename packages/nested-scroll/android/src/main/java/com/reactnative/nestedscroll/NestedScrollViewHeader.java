package com.reactnative.nestedscroll;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.facebook.react.views.view.ReactViewGroup;

public class NestedScrollViewHeader extends ReactViewGroup {
    public final static int INVALID_STICKY_HEIGHT = -1;

    public final static int INVALID_STICKY_BEGIN_INDEX = Integer.MAX_VALUE;

    private int mStickyHeight = INVALID_STICKY_HEIGHT;

    private int mStickyHeaderBeginIndex = INVALID_STICKY_BEGIN_INDEX;

    private androidx.core.widget.NestedScrollView.OnScrollChangeListener mOnScrollChangeListener;

    public NestedScrollViewHeader(@NonNull Context context) {
        super(context);
    }

    public void setOnScrollChangeListener(androidx.core.widget.NestedScrollView.OnScrollChangeListener onScrollChangeListener) {
        this.mOnScrollChangeListener = onScrollChangeListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NestedScrollView nestedScrollView = getParentNestedScrollView();
        if (nestedScrollView != null && mOnScrollChangeListener != null) {
            nestedScrollView.setOnScrollChangeListener(mOnScrollChangeListener);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        notifyStickyHeightChanged();
    }

    public void setStickyHeight(int stickyHeight) {
        mStickyHeight = stickyHeight;
        notifyStickyHeightChanged();
    }

    public void setStickyHeaderBeginIndex(int index) {
        mStickyHeaderBeginIndex = index;
        notifyStickyHeightChanged();
    }

    public int getStickyHeight() {
        if (mStickyHeight >= 0) {
            return Math.min(mStickyHeight, getHeight());
        }
        if (mStickyHeaderBeginIndex != INVALID_STICKY_BEGIN_INDEX) {
            int stickyHeaderHeight = 0;
            for (int i = 0, count = getChildCount(); i < count; i++) {
                View child = getChildAt(i);
                int childHeight = i >= mStickyHeaderBeginIndex ? child.getHeight() : 0;
                stickyHeaderHeight += childHeight;
            }
            return stickyHeaderHeight;
        }
        return 0;
    }


    private void notifyStickyHeightChanged() {
        NestedScrollView nestedScrollView = getParentNestedScrollView();
        if (nestedScrollView != null) {
            nestedScrollView.notifyStickyHeightChanged();
        }
    }

    private NestedScrollView getParentNestedScrollView() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent = parent.getParent();
            if (parent instanceof NestedScrollView) {
                return (NestedScrollView) parent;
            }
        }
        return null;
    }
}

