package com.reactnative.nestedscroll;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Scroller;

import androidx.core.view.ViewCompat;

public class NestedScrollFlingHelper {
    private final NestedScrollView mNestedScrollView;
    private final Scroller mScroller;

    private ViewGroup mTarget;

    private int mLastScrollerY = 0;

    public NestedScrollFlingHelper(NestedScrollView nestedScrollView) {
        mNestedScrollView = nestedScrollView;
        mScroller = new Scroller(nestedScrollView.getContext());
    }

    public boolean onNestedPreFling(View target, float velocityY) {
        mTarget = findScrollableView(target);
        if (mTarget != null && mTarget.getChildCount() > 0) {
            mScroller.fling(mTarget.getScrollX(), mTarget.getScrollY(), // start
                0, (int) velocityY, // velocities
                0, 0, // x
                Integer.MIN_VALUE, Integer.MAX_VALUE); // y
            runAnimatedScroll();
            return true;
        }
        return false;
    }

    private ViewGroup findScrollableView(View target) {
        if (!(target instanceof ViewGroup)) {
            return null;
        }

        ViewGroup viewGroup = (ViewGroup) target;

        if (viewGroup instanceof ScrollView) {
            return viewGroup;
        }

        ViewGroup scrollableView;
        if (viewGroup.getChildCount() > 0) {
            int size = viewGroup.getChildCount();
            for (int i = size - 1; i > -1; i--) {
                View child = viewGroup.getChildAt(i);
                scrollableView = findScrollableView(child);
                if (scrollableView != null) {
                    return scrollableView;
                }
            }
        }
        
        return null;
    }

    private void runAnimatedScroll() {
        mLastScrollerY = mTarget.getScrollY();
        ViewCompat.postInvalidateOnAnimation(mNestedScrollView);
    }

    public void computeScroll() {
        if (mScroller.isFinished()) {
            return;
        }

        mScroller.computeScrollOffset();
        final int y = mScroller.getCurrY();
        int unconsumed = y - mLastScrollerY;
        mLastScrollerY = y;

        if (unconsumed > 0) {
            unconsumed = scrollBy(unconsumed, mNestedScrollView);
        }

        unconsumed = scrollBy(unconsumed, mTarget);

        if (unconsumed < 0) {
            unconsumed = scrollBy(unconsumed, mNestedScrollView);
        }

        if (unconsumed != 0) {
            abortAnimatedScroll();
        }

        if (!mScroller.isFinished()) {
            ViewCompat.postInvalidateOnAnimation(mNestedScrollView);
        }
    }

    private int scrollBy(int unconsumed, View view) {
        final int oldScrollY = view.getScrollY();
        view.scrollBy(0, unconsumed);
        final int myConsumed = view.getScrollY() - oldScrollY;
        unconsumed -= myConsumed;
        return unconsumed;
    }

    private void abortAnimatedScroll() {
        mScroller.abortAnimation();
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            abortAnimatedScroll();
            mTarget = null;
        }
    }
}
