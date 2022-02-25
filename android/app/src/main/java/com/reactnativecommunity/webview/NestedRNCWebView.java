package com.reactnativecommunity.webview;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.facebook.react.uimanager.ThemedReactContext; 

public class NestedRNCWebView extends RNCWebViewManager.RNCWebView implements NestedScrollingChild3 {
    public NestedRNCWebView(ThemedReactContext reactContext) {
        super(reactContext);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        initScrollView();
    }

    private static final String TAG = "NestedRNCWebView";
    private static final int INVALID_POINTER = -1;

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];

    private int mLastMotionY;
    private final NestedScrollingChildHelper mChildHelper;
    private boolean mIsBeingDragged = false;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mActivePointerId = INVALID_POINTER;
    private int mNestedYOffset;
    //mScroller仅作计算，对view的操作主要于computeScroll进行
    private OverScroller mScroller;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private void initScrollView() {
        mScroller = new OverScroller(getContext(), null);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (nestedScrollEnabled) {
            return super.onTouchEvent(ev);
        }

        initVelocityTrackerIfNotExists();
        boolean consumedScrollEvent = false;
        final int actionMasked = ev.getAction();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }

        MotionEvent vtev = MotionEvent.obtain(ev);
        vtev.offsetLocation(0, mNestedYOffset);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                initOrResetVelocityTracker();
                if (mIsBeingDragged = !mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                mNestedYOffset = 0;
                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);
                if (hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
                    stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
                }
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }

                final int y = (int) ev.getY(activePointerIndex);
                int deltaY = mLastMotionY - y;
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    deltaY = deltaY > 0 ? deltaY - mTouchSlop : deltaY + mTouchSlop;
                }

                if (mIsBeingDragged) {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                    mScrollConsumed[1] = 0;
                    if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                        deltaY -= mScrollConsumed[1];
                        mNestedYOffset += mScrollOffset[1];
                    }

                    mLastMotionY = y - mScrollOffset[1];
                    final int oldScrollY = getScrollY();
                    final int range = getScrollRange();
                    if (overScrollByCompat(deltaY, oldScrollY, range) && !hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)) {
                        mVelocityTracker.clear();
                    }
                    final int scrolledDeltaY = getScrollY() - oldScrollY;
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    mScrollConsumed[1] = 0;
                    dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset, ViewCompat.TYPE_TOUCH, mScrollConsumed);
                    mLastMotionY -= mScrollOffset[1];
                    mNestedYOffset += mScrollOffset[1];
                    consumedScrollEvent = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity(mActivePointerId);
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    flingWithNestedDispatch(-initialVelocity);
                } else if (springBack(getScrollY())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                mActivePointerId = INVALID_POINTER;
                endDrag();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && springBack(getScrollY())) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                mActivePointerId = INVALID_POINTER;
                endDrag();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionY = (int) ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        if (consumedScrollEvent) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.onTouchEvent(ev);
    }

    boolean overScrollByCompat(int deltaY, int scrollY, int scrollRangeY) {
        final int top = 0;
        int newScrollY = scrollY + deltaY;
        boolean clampedY = false;
        if (newScrollY > scrollRangeY) {
            newScrollY = scrollRangeY;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }
        if (clampedY && !hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
            springBack(newScrollY);
        }
        onOverScrolled(0, newScrollY, false, clampedY);
        return clampedY;
    }

    boolean springBack(int startY) {
        return mScroller.springBack(0, startY, 0, 0, 0, getScrollRange());
    }

    int getScrollRange() {
        return computeVerticalScrollRange();
    }

    private void endDrag() {
        mIsBeingDragged = false;
        recycleVelocityTracker();
        stopNestedScroll(ViewCompat.TYPE_TOUCH);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void flingWithNestedDispatch(int velocityY) {
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, true);
            mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, getScrollRange());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        if (nestedScrollEnabled) {
            super.computeScroll();
            return;
        }

        if (mScroller.isFinished()) {
            return;
        }

        mScroller.computeScrollOffset();
        int unconsumed = mScroller.getCurrY() - getScrollY();
        if (unconsumed != 0) {
            if (hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)) {
                stopNestedScroll(ViewCompat.TYPE_TOUCH);
            }
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);

            mScrollConsumed[1] = 0;
            dispatchNestedPreScroll(0, unconsumed, mScrollConsumed, mScrollOffset, ViewCompat.TYPE_NON_TOUCH);
            unconsumed -= mScrollConsumed[1];

            final int oldScrollY = getScrollY();
            final int range = getScrollRange();
            overScrollByCompat(unconsumed, oldScrollY, range);

            final int consumedByScroll = getScrollY() - oldScrollY;
            unconsumed -= consumedByScroll;
            mScrollConsumed[1] = 0;
            dispatchNestedScroll(0, consumedByScroll, 0, unconsumed, mScrollOffset, ViewCompat.TYPE_NON_TOUCH, mScrollConsumed);
            mNestedYOffset += mScrollOffset[1];
        }

        if (!mScroller.isFinished()) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
            if (getScrollY() == 0) {
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                dispatchNestedFling(0, mScroller.getCurrVelocity(), false);
                stopNestedScroll();
            }
        }
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_NONE;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return mChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public void stopNestedScroll(int type) {
        mChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return mChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }
}
