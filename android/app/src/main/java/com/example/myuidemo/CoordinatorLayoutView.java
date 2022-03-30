package com.example.myuidemo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.myuidemo.Helper.ViewHelper;
import com.example.myuidemo.reactpullrefreshlayout.react.ReactPullRefreshLayout;
import com.facebook.react.uimanager.ReactZIndexedViewGroup;
import com.facebook.react.uimanager.ViewGroupDrawingOrderHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.AppbarLayoutHeaderBehavior;

public class CoordinatorLayoutView extends CoordinatorLayout implements ReactZIndexedViewGroup {

    private final ViewGroupDrawingOrderHelper drawingOrderHelper;

    public CoordinatorLayoutView(@NonNull Context context) {
        super(context);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(width, height);
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        this.setLayoutParams(params);
        drawingOrderHelper = new ViewGroupDrawingOrderHelper(this);
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
}
