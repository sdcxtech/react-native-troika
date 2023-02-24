package com.example.myuidemo;

import android.content.Context;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.myuidemo.Helper.LinearLayoutReactViewGroupMeasureHelper;
import com.google.android.material.appbar.AppBarLayout;

public class AppBarLayoutView extends AppBarLayout {
    private final static int INVALID_INDEX = -1;
    int mStickyHeaderBeginIndex = INVALID_INDEX;
    int mFixedHeight = 0;

    private final LinearLayoutReactViewGroupMeasureHelper linearLayoutReactViewGroupMeasureHelper;

    public AppBarLayoutView(@NonNull Context context) {
        super(context);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        CoordinatorLayoutView.LayoutParams params = new CoordinatorLayoutView.LayoutParams(width, height);
        setLayoutParams(params);
        linearLayoutReactViewGroupMeasureHelper = new LinearLayoutReactViewGroupMeasureHelper(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (linearLayoutReactViewGroupMeasureHelper.shouldDelegate()) {
            Size size = linearLayoutReactViewGroupMeasureHelper.getLayoutSize();
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
        this.mFixedHeight = fixedHeight;
        refreshFixedHeader();
    }


    public void setStickyHeaderBeginIndex(int index) {
        this.mStickyHeaderBeginIndex = index;
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
            refreshFixedHeight();
        } else {
            refreshFixedHeightByHeaderIndex();
        }
        if (getParent() instanceof CoordinatorLayoutView) {
            post(() -> getParent().requestLayout());
        }
    }

    private void refreshFixedHeight() {
        int fixedRange = mFixedHeight;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            int childHeight = child.getHeight();
            if (childHeight >= fixedRange) {
                setAppbarLayoutFixedChildHeight(child, fixedRange);
                break;
            } else {
                setAppbarLayoutFixedChildHeight(child, childHeight);
                fixedRange -= childHeight;
                if (fixedRange <= 0) {
                    break;
                }
            }
        }
    }

    private void refreshFixedHeightByHeaderIndex() {
        if (mStickyHeaderBeginIndex != INVALID_INDEX) {
            for (int i = 0, count = getChildCount(); i < count; i++) {
                View child = getChildAt(i);
                int childHeight = i >= mStickyHeaderBeginIndex ? child.getHeight() : 0;
                setAppbarLayoutFixedChildHeight(child, childHeight);
            }
        }
    }

    void setAppbarLayoutFixedChildHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof AppBarLayout.LayoutParams) {
            ((AppBarLayout.LayoutParams) layoutParams).setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
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