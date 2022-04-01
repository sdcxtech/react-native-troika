package com.google.android.material.appbar;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;


public class ScrollViewBehavior extends AppBarLayout.ScrollingViewBehavior {
    private int currentConsumedFixedRange = 0;

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        final AppbarLayoutHeaderBehavior headerBehavior = findFirstAppbarLayoutHeaderBehavior(coordinatorLayout, child);
        int limitedTop = headerBehavior != null ? headerBehavior.getFixedRange() : 0;
        int consumedScroll = consumeScroll(child, dy, limitedTop);
        if (consumedScroll != 0) {
            consumed[1] += consumedScroll;
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        final AppbarLayoutHeaderBehavior headerBehavior = findFirstAppbarLayoutHeaderBehavior(coordinatorLayout, child);
        int limitedTop = headerBehavior != null ? headerBehavior.getFixedRange() : 0;
        int consumedScroll = consumeScroll(child, dyConsumed, limitedTop);
        if (consumedScroll != 0) {
            consumed[1] += consumedScroll;
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }

    private AppbarLayoutHeaderBehavior findFirstAppbarLayoutHeaderBehavior(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child) {
        final AppBarLayout appBarLayout = findFirstDependency(coordinatorLayout.getDependencies(child));
        if (appBarLayout != null) {
            ViewGroup.LayoutParams layoutParams = appBarLayout.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
                if (behavior instanceof AppbarLayoutHeaderBehavior) {
                    return (AppbarLayoutHeaderBehavior) behavior;
                }
            }
        }
        return null;
    }

    /**
     * 上滑，滑动至顶部为0处
     * 下滑时，仅当view顶部高于maxTop时此处消费滑动距离，最多滑动至maxTop处
     *
     * @param view
     * @param dy     可消费的滑动距离 dy>0 => 上滑，dy<0 => 下滑
     * @param maxTop 其它组件限制的顶部最低位置
     * @return 已消费的滑动距离
     */
    public int consumeScroll(View view, int dy, int maxTop) {
        int originViewTop = view.getTop();
        int consumedDy = 0;
        if (maxTop > 0) {
            if (dy > 0 && originViewTop >= 0) {
                consumedDy = Math.min(originViewTop, dy);
            } else if (dy < 0 && originViewTop < maxTop) {
                consumedDy = Math.max(originViewTop - maxTop, dy);
            }
        }
        ViewCompat.offsetTopAndBottom(view, -consumedDy);
        int currentViewTop = view.getTop();
        if (currentViewTop >= 0 && currentViewTop <= maxTop) {
            currentConsumedFixedRange = maxTop - currentViewTop;
        } else {
            currentConsumedFixedRange = 0;
        }
        return consumedDy;
    }

    @Override
    protected void layoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        ViewCompat.offsetTopAndBottom(child, -currentConsumedFixedRange);
    }
}
