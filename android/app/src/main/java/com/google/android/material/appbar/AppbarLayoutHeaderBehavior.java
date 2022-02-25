package com.google.android.material.appbar;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.react.views.scroll.ReactScrollView;

public class AppbarLayoutHeaderBehavior extends AppBarLayout.Behavior {
    int fixedRange = 0;

    public int getFixedRange() {
        return fixedRange;
    }

    public void setFixedRange(int fixedRange) {
        this.fixedRange = fixedRange;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (dy != 0) {
            int min;
            int max;
            if (dy < 0) {
                min = -child.getTotalScrollRange();
                max = min + child.getDownNestedPreScrollRange();
            } else {
                min = -child.getUpNestedPreScrollRange() + fixedRange;
                max = 0;
            }
            if (min != max) {
                consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
            }
        }
        if (child.isLiftOnScroll()) {
            child.setLiftedState(child.shouldLift(target));
        }
    }


    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            float v = target instanceof ReactScrollView ? -velocityY : velocityY;
            fling(coordinatorLayout, child, -getScrollRangeForDragFling(child), 0, v);
            return true;
        }
        return false;
    }

    @Override
    int getMaxDragOffset(@NonNull AppBarLayout view) {
        return super.getMaxDragOffset(view) + fixedRange;
    }

    @Override
    int getScrollRangeForDragFling(@NonNull AppBarLayout view) {
        return super.getScrollRangeForDragFling(view) - fixedRange;
    }
}
