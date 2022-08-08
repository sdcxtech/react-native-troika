package com.google.android.material.appbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
                min = -child.getUpNestedPreScrollRange();
                max = 0;
            }
            if (min != max) {
                consumed[1] = scroll(coordinatorLayout, child, dy, min + fixedRange, max);
            }
        }
        if (child.isLiftOnScroll()) {
            child.setLiftedState(child.shouldLift(target));
        }

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        if (dyUnconsumed < 0) {
            consumed[1] =
                    scroll(coordinatorLayout, child, dyUnconsumed, -child.getDownNestedScrollRange() + fixedRange, 0);
        } else if (dyUnconsumed == 0) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        }
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
