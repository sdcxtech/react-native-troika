package com.reactnative.pulltorefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.facebook.react.uimanager.ReactOverflowView;
import com.facebook.react.uimanager.events.NativeGestureUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;

public class PullToRefresh extends SmartRefreshLayout implements ReactOverflowView {

    private final static String TAG = "PullToRefresh";

    private final Rect mRect;
    private String mOverflow = "hidden";

    public PullToRefresh(Context context) {
        super(context);
        mRect = new Rect();
    }

    public void setOverflow(String overflow) {
        mOverflow = overflow;
        invalidate();
    }

    @Nullable
    @Override
    public String getOverflow() {
        return mOverflow;
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
        String viewName = view.getClass().getCanonicalName();

        if (viewName != null && viewName.contains("ViewPager2")) {
            if (mIsBeingDragged) {
                NativeGestureUtil.notifyNativeGestureStarted(this, ev);
            }
            return super.dispatchTouchEvent(ev);
        }

        if (view.canScrollHorizontally(-1) || view.canScrollHorizontally(1)) {
            if (mIsBeingDragged) {
                NativeGestureUtil.notifyNativeGestureStarted(this, ev);
            }
            return super.dispatchTouchEvent(ev);
        }

        // 数据不足以填满整个页面
        if (!view.canScrollVertically(-1) && !view.canScrollVertically(1)) {
            view.onInterceptTouchEvent(ev);
            view.onTouchEvent(ev);

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                view.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
            }

            if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
                view.stopNestedScroll();
            }

            if (shouldInterceptTouchEvent(ev)) {
                NativeGestureUtil.notifyNativeGestureStarted(this, ev);
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                return true;
            }
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
		requestDisallowInterceptTouchEvent(true);
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

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        getDrawingRect(mRect);
        if (!"visible".equals(mOverflow)) {
            canvas.clipRect(mRect);
        }
        super.dispatchDraw(canvas);
    }

    public RefreshKernel getRefreshKernel() {
        return mKernel;
    }
}
