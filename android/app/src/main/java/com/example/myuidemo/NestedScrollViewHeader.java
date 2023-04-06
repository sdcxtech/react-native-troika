package com.example.myuidemo;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.facebook.react.views.view.ReactViewGroup;

public class NestedScrollViewHeader extends ReactViewGroup {
    public final static int INVALID_FIXED_HEIGHT = -1;

    public final static int INVALID_STICKY_BEGIN_INDEX = Integer.MAX_VALUE;

    private int mFixedHeight = 0;

    private int mStickyHeaderBeginIndex = INVALID_STICKY_BEGIN_INDEX;

    public NestedScrollViewHeader(@NonNull Context context) {
        super(context);
    }

    public void setFixedHeight(int fixedHeight) {
        mFixedHeight = fixedHeight;
        notifyFixedHeightChange();
    }

    public void setStickyHeaderBeginIndex(int index) {
        mStickyHeaderBeginIndex = index;
        notifyFixedHeightChange();
    }

    public int getFixedHeight() {
        if (mFixedHeight >= 0) {
            return Math.min(mFixedHeight, getHeight());
        }
        if (mStickyHeaderBeginIndex != INVALID_STICKY_BEGIN_INDEX) {
            int fixedHeaderHeight = 0;
            for (int i = 0, count = getChildCount(); i < count; i++) {
                View child = getChildAt(i);
                int childHeight = i >= mStickyHeaderBeginIndex ? child.getHeight() : 0;
                fixedHeaderHeight += childHeight;
            }
            return fixedHeaderHeight;
        }
        return 0;
    }


    private void notifyFixedHeightChange() {
        ViewParent parent = getParent();
        while (parent != null) {
            if (parent instanceof NestedScrollView) {
                post(parent::requestLayout);
                return;
            }
            parent = parent.getParent();
        }
    }
}