package com.example.myuidemo;

import android.content.Context;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.example.myuidemo.Helper.LinearLayoutReactViewGroupMeasureHelper;
import com.google.android.material.appbar.AppBarLayout;

public class AppBarLayoutView extends AppBarLayout {
    private final static int INVALID_INDEX = -1;
    private int mStickyHeaderBeginIndex = INVALID_INDEX;
    private int mFixedHeight = 0;
    private final LinearLayoutReactViewGroupMeasureHelper mMeasureHelper;
    private int mFixedHeaderHeight = 0;

    public AppBarLayoutView(@NonNull Context context) {
        super(context);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        CoordinatorLayoutView.LayoutParams params = new CoordinatorLayoutView.LayoutParams(width, height);
        setLayoutParams(params);
        mMeasureHelper = new LinearLayoutReactViewGroupMeasureHelper(this);
    }

    public boolean canDrag() {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMeasureHelper.shouldDelegate()) {
            Size size = mMeasureHelper.getLayoutSize();
            boolean isSizeChange = getWidth() != size.getWidth() || getHeight() != size.getHeight();
            setMeasuredDimension(size.getWidth(), size.getHeight());
            if (isSizeChange) {
                layout(getLeft(), getTop(), getRight(), getBottom());
                requestLayout();
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setAppbarLayoutFixedHeight(int fixedHeight) {
        mFixedHeight = fixedHeight;
        refreshFixedHeader();
    }

    public int getFixedHeight() {
        return mFixedHeaderHeight;
    }

    public void setStickyHeaderBeginIndex(int index) {
        mStickyHeaderBeginIndex = index;
        refreshFixedHeader();
    }

    void refreshFixedHeader() {
        if (isLaidOut()) {
            refreshFixedHeaderInner();
        } else {
            addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    refreshFixedHeaderInner();
                    removeOnLayoutChangeListener(this);
                }
            });
        }
    }

    void refreshFixedHeaderInner() {
        if (mFixedHeight > 0) {
            mFixedHeaderHeight = refreshFixedHeight();
        } else {
            mFixedHeaderHeight = refreshFixedHeightByHeaderIndex();
        }
        ViewParent parent = getParent();
        while (parent != null) {
            if (parent instanceof CoordinatorLayoutView || parent instanceof NestedScrollView) {
                post(parent::requestLayout);
                return;
            }
            parent = parent.getParent();
        }
    }

    private int refreshFixedHeight() {
        int fixedRange = mFixedHeight;
        int fixedHeaderHeight = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            int childHeight = child.getHeight();
            if (childHeight >= fixedRange) {
                setAppbarLayoutFixedChildHeight(child, fixedRange);
                fixedHeaderHeight += fixedRange;
                break;
            } else {
                setAppbarLayoutFixedChildHeight(child, childHeight);
                fixedRange -= childHeight;
                fixedHeaderHeight += childHeight;
                if (fixedRange <= 0) {
                    break;
                }
            }
        }
        return fixedHeaderHeight;
    }

    private int refreshFixedHeightByHeaderIndex() {
        int fixedHeaderHeight = 0;
        if (mStickyHeaderBeginIndex != INVALID_INDEX) {
            for (int i = 0, count = getChildCount(); i < count; i++) {
                View child = getChildAt(i);
                int childHeight = i >= mStickyHeaderBeginIndex ? child.getHeight() : 0;
                fixedHeaderHeight += childHeight;
                setAppbarLayoutFixedChildHeight(child, childHeight);
            }
        }
        return fixedHeaderHeight;
    }

    void setAppbarLayoutFixedChildHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof AppBarLayout.LayoutParams) {
            int flags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
            ((AppBarLayout.LayoutParams) layoutParams).setScrollFlags(flags);
            view.setMinimumHeight(height);
        }
    }

    private final Runnable measureAndLayout = () -> {
        measure(
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        layout(getLeft(), getTop(), getRight(), getBottom());
    };

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

}