package com.reactnative.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.core.view.ViewCompat;

import com.facebook.react.uimanager.events.NativeGestureUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;

public class PullToRefresh extends SmartRefreshLayout {

    public PullToRefresh(Context context) {
        super(context);
    }

    public PullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final Runnable measureAndLayout = () -> {
        measure(
            View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(getHeight(), View.MeasureSpec.EXACTLY));
        layout(getLeft(), getTop(), getRight(), getBottom());
    };

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewGroup view = (ViewGroup) mRefreshContent.getScrollableView();
        // 数据不足以填满整个页面
        if (!view.canScrollVertically(-1) && !view.canScrollVertically(1)) {
            if (shouldInterceptTouchEvent(ev)) {
                NativeGestureUtil.notifyNativeGestureStarted(this, ev);
            }

            final float offsetX = getScrollX() - view.getLeft();
            final float offsetY = getScrollY() - view.getTop();
            final MotionEvent transformedEvent = MotionEvent.obtain(ev);
            transformedEvent.offsetLocation(offsetX, offsetY);
            view.onInterceptTouchEvent(transformedEvent);
            view.onTouchEvent(transformedEvent);
            transformedEvent.recycle();

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                view.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
            }

            if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
                view.stopNestedScroll();
            }
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }
    
    private int mLastMotionY;
    
    private boolean shouldInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int y = (int) ev.getRawY();
                final int yDiff = Math.abs(y - mLastMotionY);
                if (yDiff >= mTouchSlop) {
                    mIsBeingDragged = true;
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                mLastMotionY = (int) ev.getRawY();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
        }
        
        return mIsBeingDragged;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (super.onInterceptTouchEvent(ev)) {
            NativeGestureUtil.notifyNativeGestureStarted(this, ev);
            return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float height = (float) (getMeasuredHeight() * 0.3);
        RefreshHeader header = getRefreshHeader();
        if (header != null) {
            int headerHeight = header.getView().getMeasuredHeight();
            setHeaderMaxDragRate(height / headerHeight);
        }
        RefreshFooter footer = getRefreshFooter();
        if (footer != null) {
            int footerHeight = footer.getView().getMeasuredHeight();
            setFooterMaxDragRate(height / footerHeight);
        }
    }

    public RefreshKernel getRefreshKernel() {
        return mKernel;
    }
}
