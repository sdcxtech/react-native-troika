package com.example.myuidemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.react.uimanager.ReactZIndexedViewGroup;
import com.facebook.react.uimanager.ViewGroupDrawingOrderHelper;
import com.google.android.material.appbar.AppBarLayout;

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
}
