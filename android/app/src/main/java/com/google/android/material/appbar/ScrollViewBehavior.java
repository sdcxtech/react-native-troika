package com.google.android.material.appbar;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;


public class ScrollViewBehavior extends AppBarLayout.ScrollingViewBehavior {
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        final AppBarLayout header = findFirstDependency(coordinatorLayout.getDependencies(child));
        int consumedScroll = consumeScroll(child, dy, getAppbarLayoutLimitedTop(header));
        if (consumedScroll != 0) {
            consumed[1] += consumedScroll;
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        final AppBarLayout header = findFirstDependency(coordinatorLayout.getDependencies(child));
        int consumedScroll = consumeScroll(child, dyConsumed, getAppbarLayoutLimitedTop(header));
        if (consumedScroll != 0) {
            consumed[1] += consumedScroll;
        }
    }

    int getAppbarLayoutLimitedTop(AppBarLayout appBarLayout) {
        int limitedTop = 0;
        if (appBarLayout != null) {
            ViewGroup.LayoutParams layoutParams = appBarLayout.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
                if (behavior instanceof AppbarLayoutHeaderBehavior) {
                    limitedTop = ((AppbarLayoutHeaderBehavior) behavior).getFixedRange();
                }
            }
        }
        return limitedTop;
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
        int viewTop = view.getTop();
        int consumedDy = 0;
        if (maxTop > 0) {
            if (dy > 0 && viewTop >= 0) {
                consumedDy = Math.min(viewTop, dy);
            } else if (dy < 0 && viewTop < maxTop) {
                consumedDy = Math.max(viewTop - maxTop, dy);
            }
        }
        ViewCompat.offsetTopAndBottom(view, -consumedDy);
        return consumedDy;
    }
}
