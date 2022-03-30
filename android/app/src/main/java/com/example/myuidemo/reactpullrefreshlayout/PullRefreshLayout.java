/**
 * fork from https://github.com/Tencent/QMUI_Android
 */
package com.example.myuidemo.reactpullrefreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuidemo.Helper.ViewHelper;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateTopRefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.RefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.react.ReactPullRefreshLayout;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.IRefreshView;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.SpinnerRefreshView;
import com.example.myuidemo.reactpullrefreshlayout.util.DisplayHelper;

public class PullRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {
    private static final String TAG = "PullRefreshLayout";
    private static final int INVALID_POINTER = -1;
    private static final int FLAG_NEED_SCROLL_TO_INIT_POSITION = 1;
    private static final int FLAG_NEED_SCROLL_TO_REFRESH_POSITION = 1 << 1;
    private static final int FLAG_NEED_DO_REFRESH = 1 << 2;
    private static final int FLAG_NEED_DELIVER_VELOCITY = 1 << 3;

    boolean mIsRefreshing = false;

    //刷新时展示的View
    private View mRefreshView;
    private int mRefreshInitOffset = Integer.MIN_VALUE;
    private int mRefreshEndOffset = 0;
    private int mRefreshCurrentOffset = mRefreshInitOffset;

    //主体View，如ScrollView、List
    private View mTargetView;
    private int mTargetInitOffset = 0;
    private int mTargetEndOffset;
    private int mTargetCurrentOffset = mTargetInitOffset;

    private RefreshOffsetCalculator mRefreshOffsetCalculator;
    private OnPullListener mListener;
    private boolean mEnableOverPull = true;
    private int mScrollFlag = 0;

    //滑动事件相关
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final int mSystemTouchSlop;
    private final int mTouchSlop;
    private boolean mNestedScrollInProgress;

    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsDragging;
    private float mInitialDownY;
    private float mInitialDownX;
    private float mInitialMotionY;
    private float mLastMotionY;
    private float mDragRate = 0.65f;
    private VelocityTracker mVelocityTracker;
    private final float mMaxVelocity;
    private final float mMiniVelocity;
    private final Scroller mScroller;
    private boolean mNestScrollDurationRefreshing = false;
    private Runnable mPendingRefreshDirectlyAction = null;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        final ViewConfiguration vc = ViewConfiguration.get(context);
        mMaxVelocity = vc.getScaledMaximumFlingVelocity();
        mMiniVelocity = vc.getScaledMinimumFlingVelocity();
        mSystemTouchSlop = vc.getScaledTouchSlop();
        mTouchSlop = DisplayHelper.px2dp(context, mSystemTouchSlop);

        mScroller = new Scroller(getContext());
        mScroller.setFriction(ViewConfiguration.getScrollFriction());
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        this.setChildrenDrawingOrderEnabled(true);
        setTranslationZ(-1);
    }

    public void setOnPullListener(OnPullListener listener) {
        mListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            reset();
            invalidate();
        }
    }

    public void setEnableOverPull(boolean enableOverPull) {
        mEnableOverPull = enableOverPull;
    }

    public boolean canChildScrollUp() {
        return canChildScrollUp(mTargetView);
    }

    public boolean canChildScrollUp(View view) {
        if (view == null) {
            return false;
        }
        return view.canScrollVertically(-1);
    }

    public void addRefreshView(View view, int index, LayoutParams params) {
        mRefreshView = view;
        super.addView(mRefreshView, index, params);
    }

    /**
     * 覆盖该方法以实现自定义的默认RefreshView。
     *
     * @return 自定义的 RefreshView, 注意该 View 必须实现 {@link IRefreshView} 接口
     */
    protected View createRefreshView() {
        return new SpinnerRefreshView(getContext());
    }

    private void addDefaultRefreshView() {
        if (mRefreshView == null) {
            mRefreshView = createRefreshView();
        }
        if (!(mRefreshView instanceof IRefreshView)) {
            throw new RuntimeException("refreshView must be a instance of IRefreshView");
        }
        if (mRefreshView.getLayoutParams() == null) {
            mRefreshView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        addView(mRefreshView);
    }

    /**
     * 设置在下拉过程中计算 RefreshView 偏移量的方法
     */
    public void setRefreshOffsetCalculator(RefreshOffsetCalculator refreshOffsetCalculator) {
        mRefreshOffsetCalculator = refreshOffsetCalculator;
    }

    protected void refresh(boolean notify) {
        if (mIsRefreshing) {
            return;
        }
        mIsRefreshing = true;
        if (mRefreshView instanceof IRefreshView) {
            ((IRefreshView) mRefreshView).onRefresh();
        }
        if (mListener != null && notify) {
            mListener.onRefresh();
        }
    }

    public void reset() {
        if (mRefreshView instanceof IRefreshView) {
            ((IRefreshView) mRefreshView).onStop();
        }
        mIsRefreshing = false;
        mScroller.forceFinished(true);
        mScrollFlag = 0;
        moveTargetViewTo(mTargetInitOffset);
    }

    private void ensureTargetView() {
        if (mTargetView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (!view.equals(mRefreshView)) {
                    mTargetView = view;
                    break;
                }
            }
        }
        if (mTargetView != null && mPendingRefreshDirectlyAction != null) {
            Runnable runnable = mPendingRefreshDirectlyAction;
            mPendingRefreshDirectlyAction = null;
            runnable.run();
        }
    }

    public View getTargetView() {
        return this.mTargetView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRefreshView == null) {
            addDefaultRefreshView();
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChild(mRefreshView, widthMeasureSpec, heightMeasureSpec);
        calculateOffset();
        measureTargetView(widthSize, heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }

    void measureTargetView(int widthSize, int heightSize) {
        ensureTargetView();
        if (mTargetView == null) {
            Log.d(TAG, "onMeasure: mTargetView == null");
            setMeasuredDimension(widthSize, heightSize);
            return;
        }
        int targetMeasureWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int targetMeasureHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTargetView.measure(targetMeasureWidthSpec, targetMeasureHeightSpec);
    }

    private int previousRefreshViewHeight = -1;

    void calculateOffset() {
        int refreshViewHeight = mRefreshView.getMeasuredHeight();
        if (previousRefreshViewHeight == -1 || previousRefreshViewHeight != refreshViewHeight) {
            previousRefreshViewHeight = refreshViewHeight;
            if (mRefreshInitOffset != -refreshViewHeight) {
                mRefreshInitOffset = -refreshViewHeight;
            }
            mTargetEndOffset = refreshViewHeight;
            mRefreshCurrentOffset = mIsRefreshing ? mRefreshEndOffset : mRefreshInitOffset;
            mTargetCurrentOffset = mIsRefreshing ? mTargetEndOffset : mTargetInitOffset;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        ensureTargetView();
        if (mTargetView == null) {
            Log.d(TAG, "onLayout: mTargetView == null");
            return;
        }
        layoutTargetView(width, height);
        layoutRefreshView(width);
    }

    void layoutTargetView(int parentWidth, int parentHeight) {
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
        final int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();
        mTargetView.layout(
                childLeft,
                childTop + mTargetCurrentOffset,
                childLeft + childWidth,
                childTop + childHeight + mTargetCurrentOffset);
    }

    void layoutRefreshView(int parentWidth) {
        int refreshViewWidth = mRefreshView.getMeasuredWidth();
        int refreshViewHeight = mRefreshView.getMeasuredHeight();
        mRefreshView.layout(
                (parentWidth / 2 - refreshViewWidth / 2),
                mRefreshCurrentOffset,
                (parentWidth / 2 + refreshViewWidth / 2),
                mRefreshCurrentOffset + refreshViewHeight);
    }

    boolean isPullRefreshLayoutWithinMotionEventBounds(MotionEvent ev) {
        View view = ViewHelper.findSpecificView(getTargetView(), new Class[]{ReactPullRefreshLayout.class});
        return view instanceof ReactPullRefreshLayout && ViewHelper.isViewWithinMotionEventBounds(view, ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTargetView();

        if (mIsRefreshing) {
            return true;
        }

        if (!isEnabled()
                || canChildScrollUp()
                || mNestedScrollInProgress
                || isPullRefreshLayoutWithinMotionEventBounds(ev)) {
            return false;
        }


        final int action = ev.getAction();
        int pointerIndex;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsDragging = false;
                mActivePointerId = ev.getPointerId(0);
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                mInitialDownX = ev.getX(pointerIndex);
                mInitialDownY = ev.getY(pointerIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);
                startDragging(x, y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsDragging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        int pointerIndex;

        if (mIsRefreshing) {
            return true;
        }

        if (!isEnabled() || canChildScrollUp() || mNestedScrollInProgress) {
            return false;
        }

        acquireVelocityTracker(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsDragging = false;
                mScrollFlag = 0;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mActivePointerId = ev.getPointerId(0);
                break;

            case MotionEvent.ACTION_MOVE: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "onTouchEvent Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);
                startDragging(x, y);

                if (mIsDragging) {
                    float dy = (y - mLastMotionY) * mDragRate;
                    if (dy >= 0) {
                        moveTargetView(dy);
                    } else {
                        int move = moveTargetView(dy);
                        float delta = Math.abs(dy) - Math.abs(move);
                        if (delta > 0) {
                            final ViewParent parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                            // 重新dispatch一次down事件，使得列表可以继续滚动
                            ev.setAction(MotionEvent.ACTION_DOWN);
                            // 立刻dispatch一个大于mSystemTouchSlop的move事件，防止触发TargetView
                            float offsetLoc = mSystemTouchSlop + 1;
                            if (delta > offsetLoc) {
                                offsetLoc = delta;
                            }
                            ev.offsetLocation(0, offsetLoc);
                            super.dispatchTouchEvent(ev);
                            ev.setAction(action);
                            // 再dispatch一次move事件，消耗掉所有dy
                            ev.offsetLocation(0, -offsetLoc);
                            super.dispatchTouchEvent(ev);
                        }
                    }
                    mLastMotionY = y;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                pointerIndex = ev.getActionIndex();
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = ev.getPointerId(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                if (mIsDragging) {
                    mIsDragging = false;
                    mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    float vy = mVelocityTracker.getYVelocity(mActivePointerId);
                    if (Math.abs(vy) < mMiniVelocity) {
                        vy = 0;
                    }
                    finishPull((int) vy);
                }
                mActivePointerId = INVALID_POINTER;
                releaseVelocityTracker();
                return false;
            }
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                return false;
        }

        return true;
    }

    void flingDownWhenCurrentOffsetHitThreshold(int velocity) {
        mScrollFlag = FLAG_NEED_SCROLL_TO_REFRESH_POSITION | FLAG_NEED_DO_REFRESH;
        mScroller.fling(0, mTargetCurrentOffset, 0, velocity,
                0, 0, mTargetInitOffset, Integer.MAX_VALUE);
        invalidate();
    }

    void flingUpWhenCurrentOffsetHitThreshold(int velocity) {
        mScroller.fling(0, mTargetCurrentOffset, 0, velocity,
                0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (mScroller.getFinalY() < mTargetInitOffset) {
            mScrollFlag = FLAG_NEED_DELIVER_VELOCITY;
        } else if (mScroller.getFinalY() < mTargetEndOffset) {
            int dy = mTargetInitOffset - mTargetCurrentOffset;
            mScroller.startScroll(0, mTargetCurrentOffset, 0, dy);
        } else if (mScroller.getFinalY() == mTargetEndOffset) {
            mScrollFlag = FLAG_NEED_DO_REFRESH;
        } else {
            mScroller.startScroll(0, mTargetCurrentOffset, 0, mTargetEndOffset - mTargetCurrentOffset);
            mScrollFlag = FLAG_NEED_DO_REFRESH;
        }
        invalidate();
    }

    void onMoveFinishWhenCurrentOffsetHitThreshold() {
        if (mTargetCurrentOffset > mTargetEndOffset) {
            mScroller.startScroll(0, mTargetCurrentOffset, 0, mTargetEndOffset - mTargetCurrentOffset);
        }
        mScrollFlag = FLAG_NEED_DO_REFRESH;
        invalidate();
    }

    void flingDown(int velocity) {
        mScroller.fling(0, mTargetCurrentOffset, 0, velocity, 0, 0, mTargetInitOffset, Integer.MAX_VALUE);
        if (mScroller.getFinalY() > mTargetEndOffset) {
            mScrollFlag = FLAG_NEED_SCROLL_TO_REFRESH_POSITION | FLAG_NEED_DO_REFRESH;
        } else {
            mScrollFlag = FLAG_NEED_SCROLL_TO_INIT_POSITION;
        }
        invalidate();
    }

    void flingUp(int velocity) {
        mScrollFlag = 0;
        mScroller.fling(0, mTargetCurrentOffset, 0, velocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (mScroller.getFinalY() < mTargetInitOffset) {
            mScrollFlag = FLAG_NEED_DELIVER_VELOCITY;
        } else {
            mScroller.startScroll(0, mTargetCurrentOffset, 0, mTargetInitOffset - mTargetCurrentOffset);
            mScrollFlag = 0;
        }
        invalidate();
    }

    void onMoveFinish() {
        if (mTargetCurrentOffset == mTargetInitOffset) {
            return;
        }
        mScroller.startScroll(0, mTargetCurrentOffset, 0, mTargetInitOffset - mTargetCurrentOffset);
        mScrollFlag = 0;
        invalidate();
    }

    private void finishPull(int vy) {
        int miniVy = vy / 1000;
        if (mTargetCurrentOffset >= mTargetEndOffset) {
            if (miniVy > 0) {
                flingDownWhenCurrentOffsetHitThreshold(miniVy);
            } else if (miniVy < 0) {
                flingUpWhenCurrentOffsetHitThreshold(miniVy);
            } else {
                onMoveFinishWhenCurrentOffsetHitThreshold();
            }
        } else {
            if (miniVy > 0) {
                flingDown(miniVy);
            } else if (miniVy < 0) {
                flingUp(miniVy);
            } else {
                onMoveFinish();
            }
        }
    }

    void triggerPullEvent() {
        if (!mIsRefreshing && allowNotifyListener) {
            if (mListener != null) {
                mListener.onPull(mRefreshCurrentOffset - mRefreshInitOffset,
                        mTargetCurrentOffset - mTargetInitOffset,
                        mRefreshEndOffset - mRefreshInitOffset,
                        mTargetEndOffset - mTargetInitOffset);
            }
            if (mRefreshView instanceof IRefreshView) {
                ((IRefreshView) mRefreshView).onPull(mRefreshCurrentOffset - mRefreshInitOffset,
                        mTargetCurrentOffset - mTargetInitOffset,
                        mRefreshEndOffset - mRefreshInitOffset,
                        mTargetEndOffset - mTargetInitOffset);
            }
        }
    }


    private boolean allowNotifyListener = true;

    public void setToRefreshDirectly(final long delay) {
        if (mTargetView != null) {
            Runnable runnable = () -> {
                allowNotifyListener = false;
                setTargetViewToTop(mTargetView);
                mScrollFlag = FLAG_NEED_DO_REFRESH;
                invalidate();
            };
            if (delay == 0) {
                runnable.run();
            } else {
                postDelayed(runnable, delay);
            }
        } else {
            mPendingRefreshDirectlyAction = () -> setToRefreshDirectly(delay);
        }
    }

    public void finishRefresh() {
        if (mTargetView != null) {
            mIsRefreshing = false;
            allowNotifyListener = true;
            if (mListener != null) {
                mListener.onStop();
            }
            if (mRefreshView instanceof IRefreshView) {
                ((IRefreshView) mRefreshView).onStop();
            }
            mScrollFlag = FLAG_NEED_SCROLL_TO_INIT_POSITION;
            mScroller.forceFinished(true);
            invalidate();
        }
    }

    protected void setTargetViewToTop(View targetView) {
        if (targetView instanceof RecyclerView) {
            ((RecyclerView) targetView).scrollToPosition(0);
        } else if (targetView instanceof AbsListView) {
            ((AbsListView) targetView).setSelectionFromTop(0, 0);
        } else {
            targetView.scrollTo(0, 0);
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            Log.d(TAG, "onSecondaryPointerUp: ");
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    protected void startDragging(float x, float y) {
        final float dx = x - mInitialDownX;
        final float dy = y - mInitialDownY;
        boolean isYDrag = isYDrag(dx, dy);
        if (isYDrag && (dy > mTouchSlop || (dy < -mTouchSlop && mTargetCurrentOffset > mTargetInitOffset)) && !mIsDragging) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mLastMotionY = mInitialMotionY;
            mIsDragging = true;
        }
    }

    protected boolean isYDrag(float dx, float dy) {
        return Math.abs(dy) > Math.abs(dx);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mScroller.abortAnimation();
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int parentCanConsume = mTargetCurrentOffset - mTargetInitOffset;
        if (dy > 0 && parentCanConsume > 0) {
            if (dy >= parentCanConsume) {
                consumed[1] = parentCanConsume;
                moveTargetViewTo(mTargetInitOffset);
            } else {
                consumed[1] = dy;
                moveTargetView(-dy);
            }
        }
        dy = dy - consumed[1];
        final int[] parentConsumed = new int[2];
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0 && !canChildScrollUp() && mScroller.isFinished() && mScrollFlag == 0) {
            int[] con = new int[2];
            dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, con);
            dyUnconsumed = dyUnconsumed + con[1];
            moveTargetView(-dyUnconsumed);
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View child) {
        mNestedScrollingParentHelper.onStopNestedScroll(child);
        mNestedChildHelper.stopNestedScroll();
        if (mNestedScrollInProgress) {
            mNestedScrollInProgress = false;
            mIsDragging = false;
            if (!mNestScrollDurationRefreshing) {
                finishPull(0);
            }
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (mTargetCurrentOffset > mTargetInitOffset) {
            mNestedScrollInProgress = false;
            mIsDragging = false;
            if (!mNestScrollDurationRefreshing) {
                finishPull((int) -velocityY);
            }
            return true;
        }
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        try {
            return mNestedChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
        } catch (Throwable e) {
            // android 24及以上ViewGroup会继续往上派发， 23以及以下直接返回false
            // 低于5.0的机器和RecyclerView配合工作时，部分机型会调用这个方法，但是ViewGroup并没有实现这个方法，会报错，这里catch一下
        }
        return false;
    }

    private int moveTargetView(float dy) {
        int target = (int) (mTargetCurrentOffset + dy);
        return moveTargetViewTo(target);
    }

    private int moveTargetViewTo(int target) {
        return moveTargetViewTo(target, false);
    }

    private int moveTargetViewTo(int target, boolean calculateAnyWay) {
        target = calculateTargetOffset(target, mTargetInitOffset, mTargetEndOffset, mEnableOverPull);
        int offset = 0;
        if (target != mTargetCurrentOffset || calculateAnyWay) {
            offset = target - mTargetCurrentOffset;
            ViewCompat.offsetTopAndBottom(mTargetView, offset);
            mTargetCurrentOffset = target;
            moveRefreshViewToNewLocation();
            triggerPullEvent();
        }
        return offset;
    }

    void moveRefreshViewToNewLocation() {
        if (mRefreshOffsetCalculator == null) {
            mRefreshOffsetCalculator = new LocateTopRefreshOffsetCalculator();
        }
        int newRefreshOffset = mRefreshOffsetCalculator.calculateRefreshOffset(
                mRefreshInitOffset,
                mRefreshEndOffset,
                mRefreshView.getMeasuredHeight(),
                mTargetCurrentOffset,
                mTargetInitOffset,
                mTargetEndOffset);
        if (newRefreshOffset != mRefreshCurrentOffset) {
            ViewCompat.offsetTopAndBottom(mRefreshView, newRefreshOffset - mRefreshCurrentOffset);
            mRefreshCurrentOffset = newRefreshOffset;
        }
    }

    /**
     * @param target              目标位置
     * @param targetInitOffset    初始位置
     * @param targetRefreshOffset 进行刷新时TargetView偏移位置
     * @param enableOverPull      是否允许TargetView偏移至刷新时偏移位置之下
     * @return enableOverPull
     * enableOverPull为false，区间为[targetInitOffset,targetRefreshOffset]
     * enableOverPull为true，区间为[targetInitOffset,Integer.Max]
     */
    protected int calculateTargetOffset(int target, int targetInitOffset, int targetRefreshOffset, boolean enableOverPull) {
        target = Math.max(target, targetInitOffset);
        if (!enableOverPull) {
            target = Math.min(target, targetRefreshOffset);
        }
        return target;
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private boolean hasFlag(int flag) {
        return (mScrollFlag & flag) == flag;
    }

    private void removeFlag(int flag) {
        mScrollFlag = mScrollFlag & ~flag;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int offsetY = mScroller.getCurrY();
            moveTargetViewTo(offsetY);
            if (offsetY <= 0 && hasFlag(FLAG_NEED_DELIVER_VELOCITY)) {
                deliverVelocity();
                mScroller.forceFinished(true);
            }
            invalidate();
        } else if (hasFlag(FLAG_NEED_SCROLL_TO_INIT_POSITION)) {
            removeFlag(FLAG_NEED_SCROLL_TO_INIT_POSITION);
            if (mTargetCurrentOffset != mTargetInitOffset) {
                mScroller.startScroll(0, mTargetCurrentOffset, 0, mTargetInitOffset - mTargetCurrentOffset);
            }
            Log.d(TAG, "FLAG:  FLAG_NEED_SCROLL_TO_INIT_POSITION");
            invalidate();
        } else if (hasFlag(FLAG_NEED_SCROLL_TO_REFRESH_POSITION)) {
            removeFlag(FLAG_NEED_SCROLL_TO_REFRESH_POSITION);
            if (mTargetCurrentOffset != mTargetEndOffset) {
                mScroller.startScroll(0, mTargetCurrentOffset, 0, mTargetEndOffset - mTargetCurrentOffset);
            } else {
                moveTargetViewTo(mTargetEndOffset, true);
            }
            Log.d(TAG, "FLAG:  FLAG_NEED_SCROLL_TO_REFRESH_POSITION");
            invalidate();
        } else if (hasFlag(FLAG_NEED_DO_REFRESH)) {
            removeFlag(FLAG_NEED_DO_REFRESH);
            moveTargetViewTo(mTargetEndOffset, true);
            refresh(allowNotifyListener);
            Log.d(TAG, "FLAG:  FLAG_NEED_DO_REFRESH");
        } else {
            deliverVelocity();
        }
    }

    private void deliverVelocity() {
        if (hasFlag(FLAG_NEED_DELIVER_VELOCITY)) {
            removeFlag(FLAG_NEED_DELIVER_VELOCITY);
            if (mScroller.getCurrVelocity() > mMiniVelocity) {
                // if there is a velocity, pass it on
                if (mTargetView instanceof RecyclerView) {
                    ((RecyclerView) mTargetView).fling(0, (int) mScroller.getCurrVelocity());
                } else if (mTargetView instanceof AbsListView && android.os.Build.VERSION.SDK_INT >= 21) {
                    ((AbsListView) mTargetView).fling((int) mScroller.getCurrVelocity());
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mNestScrollDurationRefreshing = mIsRefreshing || (mScrollFlag & FLAG_NEED_DO_REFRESH) != 0;
        } else if (mNestScrollDurationRefreshing) {
            if (action == MotionEvent.ACTION_MOVE) {
                if (!mIsRefreshing && mScroller.isFinished() && mScrollFlag == 0) {
                    // 这里必须要 dispatch 一次 down 事件，否则不能触发 NestScroll，具体可参考 RecyclerView
                    // down 过程中会触发 onStopNestedScroll，mNestScrollDurationRefreshing 必须在之后
                    // 置为false，否则会触发 finishPull
                    ev.offsetLocation(0, -mSystemTouchSlop - 1);
                    ev.setAction(MotionEvent.ACTION_DOWN);
                    super.dispatchTouchEvent(ev);
                    mNestScrollDurationRefreshing = false;
                    ev.setAction(action);
                    // offset touch slop, 避免触发点击事件
                    ev.offsetLocation(0, mSystemTouchSlop + 1);
                }
            } else {
                mNestScrollDurationRefreshing = false;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public interface OnPullListener {

        void onRefresh();

        void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset);

        void onStop();
    }

    private NestedScrollingChildHelper mNestedChildHelper;

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedChildHelper.stopNestedScroll();
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return mNestedChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return mNestedChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}