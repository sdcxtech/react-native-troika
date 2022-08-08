package com.example.myuidemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;

import com.example.myuidemo.Helper.ViewHelper;
import com.facebook.react.uimanager.ReactZIndexedViewGroup;
import com.facebook.react.uimanager.ViewGroupDrawingOrderHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.AppbarLayoutHeaderBehavior;

public class CoordinatorLayoutView extends CoordinatorLayout implements ReactZIndexedViewGroup, NestedScrollingChild3 {

    private final ViewGroupDrawingOrderHelper drawingOrderHelper;

    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    public CoordinatorLayoutView(@NonNull Context context) {
        super(context);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(width, height);
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        this.setLayoutParams(params);
        drawingOrderHelper = new ViewGroupDrawingOrderHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
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

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        drawingOrderHelper.handleAddView(child);
        setChildrenDrawingOrderEnabled(drawingOrderHelper.shouldEnableCustomDrawingOrder());
        super.addView(child, index, params);
    }

    @Override
    public void removeView(View view) {
        drawingOrderHelper.handleRemoveView(view);
        setChildrenDrawingOrderEnabled(drawingOrderHelper.shouldEnableCustomDrawingOrder());
        super.removeView(view);
    }

    @Override
    public void removeViewAt(int index) {
        drawingOrderHelper.handleRemoveView(getChildAt(index));
        setChildrenDrawingOrderEnabled(drawingOrderHelper.shouldEnableCustomDrawingOrder());
        super.removeViewAt(index);
    }

    @Override
    public int getZIndexMappedChildIndex(int index) {
        return drawingOrderHelper.getChildDrawingOrder(getChildCount(), index);
    }

    @Override
    public void updateDrawingOrder() {
        drawingOrderHelper.update();
        setChildrenDrawingOrderEnabled(drawingOrderHelper.shouldEnableCustomDrawingOrder());
        invalidate();
    }

    public boolean canScrollUp() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            final CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior instanceof AppbarLayoutHeaderBehavior &&
                    (((AppbarLayoutHeaderBehavior) viewBehavior).getTopAndBottomOffset() < 0)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (direction == -1 && canScrollUp()) {
            return true;
        }
        return super.canScrollVertically(direction);
    }

    @Override
    public void scrollTo(int x, int y) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            final CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior instanceof AppbarLayoutHeaderBehavior) {
                ((AppbarLayoutHeaderBehavior) viewBehavior).setTopAndBottomOffset(0);
            }
            View view = ViewHelper.findSpecificView(child, new Class[]{ScrollView.class, WebView.class});
            if (view != null) {
                view.scrollTo(x, y);
            }
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes, int type) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes, type);
        startNestedScroll(nestedScrollAxes, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type, consumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        dispatchNestedPreScroll(dx, dy, consumed, null, type);
        if (dy - consumed[1] != 0) {
            super.onNestedPreScroll(target, dx, dy, consumed, type);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (super.onNestedFling(target, velocityX, velocityY, consumed)) {
            return true;
        }
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (dispatchNestedPreFling(velocityX, velocityY)) {
            return true;
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(View target, int type) {
        super.onStopNestedScroll(target, type);
        mNestedScrollingChildHelper.stopNestedScroll(type);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return mNestedScrollingChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        mNestedScrollingChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return mNestedScrollingChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}
