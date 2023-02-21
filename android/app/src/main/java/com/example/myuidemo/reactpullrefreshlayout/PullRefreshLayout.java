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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuidemo.Helper.LoadingMoreHelper;
import com.example.myuidemo.Helper.LoadingMoreState;
import com.example.myuidemo.Helper.RefreshHelper;
import com.example.myuidemo.Helper.RefreshState;
import com.example.myuidemo.Helper.ViewHelper;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.RefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.IRefreshView;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.ILoadMoreView;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.OnPullListener;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.SpinnerLoadMoreView;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.SpinnerRefreshView;
import com.example.myuidemo.reactpullrefreshlayout.util.DisplayHelper;
import com.facebook.react.common.MapBuilder;

import java.util.ArrayList;
import java.util.Map;

public class PullRefreshLayout extends ViewGroup implements NestedScrollingParent3, NestedScrollingChild {
    private static final String TAG = "PullRefreshLayout";
    private static final int INVALID_POINTER = -1;
    private static final int FLAG_NEED_SCROLL_TO_INIT_POSITION = 1;
    private static final int FLAG_NEED_SCROLL_TO_REFRESH_POSITION = 1 << 1;
    private static final int FLAG_NEED_DO_REFRESH = 1 << 2;
    private static final int FLAG_NEED_SCROLL_TO_LOAD_MORE = 1 << 3;
    private static final int FLAG_NEED_LOAD_MORE = 1 << 4;

    private View mTargetView;

    private View mRefreshView;
    private final RefreshHelper mRefreshHelper;
    private boolean mNotifyRefresh = false;
    private boolean mNestScrollDurationRefreshing = false;
    ArrayList<Runnable> refreshViewPendingActionList = new ArrayList<>();

    private View mLoadMoreView;
    private final LoadingMoreHelper mLoadingMoreHelper;
    private boolean mNotifyLoadMore = false;
    ArrayList<Runnable> loadMoreViewPendingActionList = new ArrayList<>();

    private OnPullListener mListener;
    private int mScrollFlag = 0;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final int mSystemTouchSlop;
    private final int mTouchSlop;
    private boolean mNestedScrollInProgress;
    private boolean mNonTouchNestedScrollInProgress = false;

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
        setChildrenDrawingOrderEnabled(true);
        setElevation(-1);
        mRefreshHelper = new RefreshHelper();
        mLoadingMoreHelper = new LoadingMoreHelper();
    }

    public void setOnPullListener(OnPullListener listener) {
        mListener = listener;
    }

    private void ensureTargetView() {
        if (mTargetView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (!view.equals(mRefreshView) && !view.equals(mLoadMoreView)) {
                    mTargetView = view;
                    break;
                }
            }
        }
    }

    void ensureRefreshViewHelper(View view) {
        mRefreshHelper.setRefreshView(view);
        while (!refreshViewPendingActionList.isEmpty()) {
            Runnable runnable = refreshViewPendingActionList.remove(0);
            runnable.run();
        }
    }

    public void addRefreshView(View view, int index, LayoutParams params) {
        if (mRefreshView != null) {
            removeView(mRefreshView);
        }
        mRefreshView = view;
        super.addView(mRefreshView, index, params);
        ensureRefreshViewHelper(mRefreshView);
    }

    /**
     * 覆盖该方法以实现自定义的默认RefreshView。
     *
     * @return 自定义的 RefreshView, 注意该 View 必须实现 {@link IRefreshView} 接口
     */
    protected View createDefaultRefreshView() {
        return new SpinnerRefreshView(getContext());
    }

    protected View createDefaultLoadMoreView() {
        return new SpinnerLoadMoreView(getContext());
    }

    public void addDefaultRefreshView() {
        mRefreshView = createDefaultRefreshView();
        if (mRefreshView.getLayoutParams() == null) {
            mRefreshView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        addView(mRefreshView);
        ensureRefreshViewHelper(mRefreshView);
    }

    public void enableRefreshAction(boolean enable) {
        Runnable runnable = () -> {
            if (mRefreshHelper.isEnable() == enable) return;
            if (mRefreshHelper.getOffsetState() != RefreshState.INIT) {
                finishRefresh();
            }
            mRefreshHelper.setEnable(enable);
            mRefreshHelper.calculateOffsetConfig(true);
            requestLayout();
        };
        executeRefreshAction(runnable);
    }

    public boolean isRefreshActionEnable() {
        return mTargetView != null && mRefreshHelper.isEnable();
    }

    public void setRefreshOffsetCalculator(RefreshOffsetCalculator refreshOffsetCalculator) {
        Runnable runnable = () -> mRefreshHelper.setRefreshOffsetCalculator(refreshOffsetCalculator);
        executeRefreshAction(runnable);
    }

    public void enableRefreshOverPull(boolean enableOverPull) {
        Runnable runnable = () -> mRefreshHelper.setEnableOverPull(enableOverPull);
        executeRefreshAction(runnable);
    }

    void executeRefreshAction(Runnable runnable) {
        if (mTargetView != null && refreshViewPendingActionList.isEmpty()) {
            runnable.run();
        } else {
            refreshViewPendingActionList.add(runnable);
        }
    }

    public void setRefreshing(boolean refreshing) {
        Runnable runnable = () -> {
            mRefreshHelper.setRefreshing(refreshing);
            if (refreshing) {
                startRefresh(false);
            } else {
                finishRefresh();
            }
        };
        executeRefreshAction(runnable);
    }

    public void startRefresh(boolean notify) {
        if (mTargetView != null) {
            mScroller.forceFinished(true);
            setTargetViewToTop(mTargetView);
            mScrollFlag = FLAG_NEED_SCROLL_TO_REFRESH_POSITION | FLAG_NEED_DO_REFRESH;
            mNotifyRefresh = notify;
            invalidate();
        }
    }

    private void notifyRefresh() {
        if (mListener != null && mNotifyRefresh) {
            mListener.onRefresh();
            mNotifyRefresh = false;
        }
        if (mRefreshView instanceof IRefreshView) {
            ((IRefreshView) mRefreshView).onRefresh();
        }
    }

    void notifyRefreshStop() {
        if (mListener != null) {
            mListener.onRefreshStop();
        }
        if (mRefreshView instanceof IRefreshView) {
            ((IRefreshView) mRefreshView).onStop();
        }
    }

    public void finishRefresh() {
        if (mTargetView != null) {
            if (mLoadingMoreHelper.isLoadingMore()) {
                notifyRefreshStop();
                startLoadingMore(false);
            } else {
                if (mProgressState == ProgressState.FromRefreshToInit) {
                    return;
                }
                mScroller.forceFinished(true);
                mScrollFlag = FLAG_NEED_SCROLL_TO_INIT_POSITION;
                mNotifyRefresh = false;
                invalidate();
            }
        }
    }

    private int offsetRefreshStateView(float dy) {
        int target = (int) (mRefreshHelper.getTargetViewCurrentOffset() + dy);
        return offsetRefreshStateViewToPosition(target);
    }

    private int offsetRefreshStateViewToPosition(int target) {
        int delta = mRefreshHelper.moveTargetViewTo(target, false);
        ViewCompat.offsetTopAndBottom(mTargetView, delta);
        int refreshViewOffset = mRefreshHelper.moveRefreshViewToNewLocation();
        ViewCompat.offsetTopAndBottom(mRefreshView, refreshViewOffset);
        onRefreshPull(mRefreshHelper.dyOfRefreshViewWithInitPosition(), mRefreshHelper.dyOfTargetViewWithInitPosition(), mRefreshHelper.totalOffsetOfRefresh(), mRefreshHelper.totalOffsetOfTarget());
        return -delta;
    }

    void onRefreshPull(int refreshOffset, int targetOffset, int totalRefreshOffset, int totalTargetOffset) {
        if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT && mRefreshHelper.getOffsetState() == RefreshState.INIT) {
            return;
        }
        if (mListener != null) {
            mListener.onPull(refreshOffset, targetOffset, totalRefreshOffset, totalTargetOffset);
        }
        if (mRefreshView instanceof IRefreshView) {
            ((IRefreshView) mRefreshView).onPull(refreshOffset, targetOffset, totalRefreshOffset, totalTargetOffset);
        }
    }

    public void addLoadMoreView(View view, int index, LayoutParams params) {
        if (mLoadMoreView != null) {
            removeView(mLoadMoreView);
        }
        mLoadMoreView = view;
        super.addView(mLoadMoreView, index, params);
        ensureLoadingMoreHelper(mLoadMoreView);
    }

    public void addDefaultLoadMoreView() {
        mLoadMoreView = createDefaultLoadMoreView();
        if (mLoadMoreView.getLayoutParams() == null) {
            mLoadMoreView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        addView(mLoadMoreView);
        ensureLoadingMoreHelper(mLoadMoreView);
    }

    void ensureLoadingMoreHelper(View view) {
        mLoadingMoreHelper.setLoadMoreView(view);
        while (!loadMoreViewPendingActionList.isEmpty()) {
            Runnable runnable = loadMoreViewPendingActionList.remove(0);
            runnable.run();
        }
    }

    void executeLoadMoreAction(Runnable runnable) {
        if (mTargetView != null && loadMoreViewPendingActionList.isEmpty()) {
            runnable.run();
        } else {
            loadMoreViewPendingActionList.add(runnable);
        }
    }

    public void setLoadingMore(boolean loadingMore) {
        Runnable runnable = () -> {
            mLoadingMoreHelper.setLoadingMore(loadingMore);
            if (loadingMore) {
                startLoadingMore(false);
            } else {
                finishLoadingMore();
            }
        };
        executeLoadMoreAction(runnable);
    }

    public void enableLoadMoreAction(boolean enable) {
        Runnable runnable = () -> {
            if (mLoadingMoreHelper.getEnable() == enable) return;
            if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT) {
                finishLoadingMore();
            }
            mLoadingMoreHelper.setEnable(enable);
            mLoadingMoreHelper.calculateLoadMoreOffsetConfig(true);
            requestLayout();
        };
        executeLoadMoreAction(runnable);
    }

    public void startLoadingMore(boolean notify) {
        if (mTargetView != null) {
            setTargetViewToBottom(mTargetView);
            mScroller.forceFinished(true);
            mScrollFlag = FLAG_NEED_SCROLL_TO_LOAD_MORE | FLAG_NEED_LOAD_MORE;
            mNotifyLoadMore = notify;
            invalidate();
        }
    }

    void notifyLoadMore() {
        if (mListener != null && mNotifyLoadMore) {
            mListener.onLoadMore();
            mNotifyLoadMore = false;
        }
        if (mLoadMoreView instanceof ILoadMoreView) {
            ((ILoadMoreView) mLoadMoreView).onLoadMore();
        }
    }

    void notifyLoadMoreStop() {
        if (mListener != null) {
            mListener.onLoadMoreStop();
        }
        if (mLoadMoreView instanceof ILoadMoreView) {
            ((ILoadMoreView) mLoadMoreView).onStop();
        }
    }

    public void finishLoadingMore() {
        if (mTargetView != null) {
            if (mRefreshHelper.isRefreshing()) {
                notifyLoadMoreStop();
                startRefresh(false);
            } else {
                if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT) {
                    if (mProgressState == ProgressState.FromLoadMoreToInit) {
                        return;
                    }
                    Log.d(TAG, "finishLoadingMore: ");
                    mScroller.forceFinished(true);
                    mScrollFlag = FLAG_NEED_SCROLL_TO_INIT_POSITION;
                    mNotifyLoadMore = false;
                    invalidate();
                }
            }
        }
    }

    int offsetLoadingMoreStateView(int offset, boolean enableOverPull) {
        return offsetLoadingMoreStateViewToPosition(mLoadingMoreHelper.getLoadMoreCurrentOffset() + offset, enableOverPull);
    }

    int offsetLoadingMoreStateViewToPosition(int position, boolean enableOverPull) {
        int delta = mLoadingMoreHelper.moveViewToPosition(position, enableOverPull);
        ViewCompat.offsetTopAndBottom(mTargetView, delta);
        ViewCompat.offsetTopAndBottom(mLoadMoreView, delta);
        onLoadMorePull(Math.abs(mLoadingMoreHelper.dyWithInit()), Math.abs(mLoadingMoreHelper.totalOffset()));
        return -delta;
    }

    void onLoadMorePull(int offset, int total) {
        if (mRefreshHelper.getOffsetState() != RefreshState.INIT && mLoadingMoreHelper.getOffsetState() == LoadingMoreState.INIT) {
            return;
        }

        if (mListener != null) {
            mListener.onLoadMorePull(offset, total);
        }
        if (mLoadMoreView instanceof ILoadMoreView) {
            ((ILoadMoreView) mLoadMoreView).onPull(offset, total);
        }
    }

    public void reset() {
        finishRefresh();
        finishLoadingMore();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureTargetView();
        if (mRefreshView == null) {
            addDefaultRefreshView();
        }
        if (mLoadMoreView == null) {
            addDefaultLoadMoreView();
        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChild(mRefreshView, widthMeasureSpec, heightMeasureSpec);
        mRefreshHelper.calculateOffsetConfig();

        measureTargetView(widthSize, heightSize);

        measureChild(mLoadMoreView, widthMeasureSpec, heightMeasureSpec);
        mLoadingMoreHelper.calculateLoadMoreOffsetConfig(false);

        setMeasuredDimension(widthSize, heightSize);
    }

    void measureTargetView(int widthSize, int heightSize) {
        ensureTargetView();
        if (mTargetView == null) {
            Log.d(TAG, "onMeasure: mTargetView == null");
            return;
        }
        int targetMeasureWidthSpec = MeasureSpec.makeMeasureSpec(widthSize - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int targetMeasureHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTargetView.measure(targetMeasureWidthSpec, targetMeasureHeightSpec);
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
            return;
        }
        layoutTargetView(width, height);
        layoutRefreshView(width);
    }

    void layoutTargetView(int parentWidth, int parentHeight) {
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop() + mRefreshHelper.getTargetViewCurrentOffset() + mLoadingMoreHelper.getLoadMoreCurrentOffset();
        final int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
        final int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();
        mTargetView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int loadMoreViewWidth = mLoadMoreView.getMeasuredWidth();
        int loadMoreViewHeight = mLoadMoreView.getMeasuredHeight();
        mLoadMoreView.layout((parentWidth / 2 - loadMoreViewWidth / 2), childTop + childHeight, (parentWidth / 2 + loadMoreViewWidth / 2), childTop + childHeight + loadMoreViewHeight);
    }

    void layoutRefreshView(int parentWidth) {
        int refreshViewWidth = mRefreshView.getMeasuredWidth();
        int refreshViewHeight = mRefreshView.getMeasuredHeight();
        int refreshViewOffset = mRefreshHelper.getRefreshViewCurrentOffset();
        mRefreshView.layout((parentWidth / 2 - refreshViewWidth / 2), refreshViewOffset, (parentWidth / 2 + refreshViewWidth / 2), refreshViewOffset + refreshViewHeight);
    }

    boolean isPullRefreshLayoutWithinMotionEventBounds(MotionEvent ev) {
        View view = ViewHelper.findSpecificView(mTargetView, new Class[]{PullRefreshLayout.class});
        return view instanceof PullRefreshLayout && ((PullRefreshLayout) view).isRefreshActionEnable() && ViewHelper.isViewWithinMotionEventBounds(view, ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTargetView();
        if (mRefreshHelper.isRefreshing() || mLoadingMoreHelper.isLoadingMore() || hasFlag(FLAG_NEED_DO_REFRESH) || hasFlag(FLAG_NEED_LOAD_MORE)) {
            return true;
        }
        if (!isEnabled() || canChildScrollUp() || mNestedScrollInProgress || isPullRefreshLayoutWithinMotionEventBounds(ev)) {
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
        if (mRefreshHelper.isRefreshing() || mLoadingMoreHelper.isLoadingMore() || hasFlag(FLAG_NEED_DO_REFRESH) || hasFlag(FLAG_NEED_LOAD_MORE)) {
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
                        offsetRefreshStateView(dy);
                    } else {
                        int move = offsetRefreshStateView(dy);
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

    private void finishPull(int vy) {
        int miniVy = vy / 1000;
        if (mRefreshHelper.getOffsetState() != RefreshState.INIT) {
            mScroller.forceFinished(true);
            mScroller.fling(0, mRefreshHelper.getTargetViewCurrentOffset() + mLoadingMoreHelper.getLoadMoreCurrentOffset(), 0, miniVy, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (mRefreshHelper.isFinalYInRefreshPosition(mScroller.getFinalY())) {
                startRefresh(true);
            } else {
                finishRefresh();
            }
        } else if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT) {
            mScroller.forceFinished(true);
            mScroller.fling(0, mLoadingMoreHelper.getLoadMoreCurrentOffset() + mRefreshHelper.getTargetViewCurrentOffset(), 0, miniVy, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (mLoadingMoreHelper.isFinalYInLoadMorePosition(mScroller.getFinalY())) {
                startLoadingMore(true);
            } else {
                finishLoadingMore();
            }
        }

    }

    protected void setTargetViewToTop(View targetView) {
        if (targetView instanceof RecyclerView) {
            ((RecyclerView) targetView).scrollToPosition(0);
        } else if (targetView instanceof AbsListView) {
            ((AbsListView) targetView).setSelectionFromTop(0, 0);
        } else if (targetView != null) {
            targetView.scrollTo(0, 0);
        }
    }

    protected void setTargetViewToBottom(View targetView) {
        if (targetView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) targetView;
            if (recyclerView.getAdapter() != null) {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        } else if (targetView instanceof AbsListView) {
            ((AbsListView) targetView).setSelection(((AbsListView) targetView).getAdapter().getCount() - 1);
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
        if (isYDrag && (dy > mTouchSlop || (dy < -mTouchSlop) && mRefreshHelper.getOffsetState() != RefreshState.INIT) && !mIsDragging) {
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
        boolean hasFlag = (mScrollFlag & flag) == flag;
        if (hasFlag) {
            Map<Integer, String> map = MapBuilder.of(FLAG_NEED_DO_REFRESH, "FLAG_NEED_DO_REFRESH", FLAG_NEED_SCROLL_TO_REFRESH_POSITION, "FLAG_NEED_SCROLL_TO_REFRESH_POSITION", FLAG_NEED_LOAD_MORE, "FLAG_NEED_LOAD_MORE", FLAG_NEED_SCROLL_TO_LOAD_MORE, "FLAG_NEED_SCROLL_TO_LOAD_MORE", FLAG_NEED_SCROLL_TO_INIT_POSITION, "FLAG_NEED_SCROLL_TO_INIT_POSITION");
            String flagName = map.containsKey(flag) ? map.get(flag) : "Unknown Flag: " + flag;
            Log.d(TAG, flagName);
        }
        return hasFlag;
    }

    private void removeFlag(int flag) {
        mScrollFlag = mScrollFlag & ~flag;
    }


    enum ProgressState {
        None, FromRefreshToInit, FromLoadMoreToInit, FromInitToLoadMore, FromInitToRefresh
    }

    ProgressState mProgressState = ProgressState.None;

    @Override
    public void computeScroll() {
        if (mScroller.isFinished() && mProgressState != ProgressState.None) {
            if (mProgressState == ProgressState.FromRefreshToInit) {
                notifyRefreshStop();
            } else if (mProgressState == ProgressState.FromLoadMoreToInit) {
                notifyLoadMoreStop();
            }
            mProgressState = ProgressState.None;
        }

        if (mScroller.computeScrollOffset()) {
            int y = mScroller.getCurrY();
            if (y < 0 || mProgressState == ProgressState.FromLoadMoreToInit) {
                offsetLoadingMoreStateViewToPosition(y, true);
            } else {
                offsetRefreshStateViewToPosition(y);
            }
            invalidate();
        } else if (hasFlag(FLAG_NEED_SCROLL_TO_INIT_POSITION)) {
            removeFlag(FLAG_NEED_SCROLL_TO_INIT_POSITION);
            if (mRefreshHelper.getOffsetState() != RefreshState.INIT) {
                mProgressState = ProgressState.FromRefreshToInit;
            } else if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT) {
                mProgressState = ProgressState.FromLoadMoreToInit;
            }
            int startY = mLoadingMoreHelper.getLoadMoreCurrentOffset() + mRefreshHelper.getTargetViewCurrentOffset();
            int dy = -mLoadingMoreHelper.dyWithInit() - mRefreshHelper.dyOfTargetViewWithInitPosition();
            mScroller.startScroll(0, startY, 0, dy);
            invalidate();
        } else if (hasFlag(FLAG_NEED_SCROLL_TO_REFRESH_POSITION)) {
            offsetLoadingMoreStateViewToPosition(0, false);

            removeFlag(FLAG_NEED_SCROLL_TO_REFRESH_POSITION);
            int startY = mRefreshHelper.getTargetViewCurrentOffset() + mLoadingMoreHelper.getLoadMoreCurrentOffset();
            int dy = -mRefreshHelper.dyOfTargetViewWithRefreshPosition() - mLoadingMoreHelper.dyWithInit();
            mScroller.startScroll(0, startY, 0, dy);
            invalidate();
        } else if (hasFlag(FLAG_NEED_SCROLL_TO_LOAD_MORE)) {
            offsetRefreshStateViewToPosition(0);

            removeFlag(FLAG_NEED_SCROLL_TO_LOAD_MORE);
            int startY = mLoadingMoreHelper.getLoadMoreCurrentOffset() + mRefreshHelper.getTargetViewCurrentOffset();
            int dy = -mLoadingMoreHelper.dyWithLoadMore() - mRefreshHelper.dyOfTargetViewWithInitPosition();
            mScroller.startScroll(0, startY, 0, dy);
            invalidate();
        } else if (hasFlag(FLAG_NEED_DO_REFRESH)) {
            removeFlag(FLAG_NEED_DO_REFRESH);
            notifyRefresh();
        } else if (hasFlag(FLAG_NEED_LOAD_MORE)) {
            removeFlag(FLAG_NEED_LOAD_MORE);
            notifyLoadMore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mNestScrollDurationRefreshing = mRefreshHelper.isRefreshing() || (mScrollFlag & FLAG_NEED_DO_REFRESH) != 0;
        } else if (mNestScrollDurationRefreshing) {
            if (action == MotionEvent.ACTION_MOVE) {
                if (!mRefreshHelper.isRefreshing() && mScroller.isFinished() && mScrollFlag == 0) {
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


    private final NestedScrollingChildHelper mNestedChildHelper;

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

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return onStartNestedScroll(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        onNestedScrollAccepted(child, target, axes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View child) {
        onStopNestedScroll(child, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (mRefreshHelper.getOffsetState() != RefreshState.INIT) {
            mNestedScrollInProgress = false;
            mIsDragging = false;
            if (!mNestScrollDurationRefreshing) {
                finishPull((int) -velocityY);
            }
            return true;
        }

        if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT) {
            finishPull((int) -velocityY);
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

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return isEnabled() && (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mScroller.abortAnimation();
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mNestedScrollInProgress = mNestedScrollInProgress || type == ViewCompat.TYPE_TOUCH;
        mNonTouchNestedScrollInProgress = mNonTouchNestedScrollInProgress || type == ViewCompat.TYPE_NON_TOUCH;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
        mNestedChildHelper.stopNestedScroll(type);
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            mNonTouchNestedScrollInProgress = false;
        } else if (type == ViewCompat.TYPE_TOUCH) {
            if (mNestedScrollInProgress) {
                mNestedScrollInProgress = false;
                mIsDragging = false;
            }
        }
        if (!mNestScrollDurationRefreshing && mScrollFlag == 0 & !mNonTouchNestedScrollInProgress) {
            finishPull(0);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            if (dy > 0) {
                if (mRefreshHelper.getOffsetState() != RefreshState.INIT) {
                    int consumedY = Math.min(dy, mRefreshHelper.dyOfTargetViewWithInitPosition());
                    offsetRefreshStateView(-consumedY);
                    consumed[1] = consumedY;
                }
            } else if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT) {
                consumed[1] = offsetLoadingMoreStateView(-dy, true);
            }

            final int[] parentConsumed = new int[2];
            if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
                consumed[0] += parentConsumed[0];
                consumed[1] += parentConsumed[1];
            }
        }
    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        int[] consumed = new int[2];
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        if (type == ViewCompat.TYPE_TOUCH) {
            if (dyUnconsumed < 0) {
                if (!canChildScrollUp() && mScroller.isFinished() && mScrollFlag == 0) {
                    int originConsumedY = consumed[1];
                    dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, consumed);
                    if (originConsumedY != consumed[1]) {
                        int delta = consumed[1] - originConsumedY;
                        dyUnconsumed = dyUnconsumed + delta;
                    }
                    int consumedByRefresh = offsetRefreshStateView(-dyUnconsumed);
                    consumed[1] += consumedByRefresh;
                } else if (mLoadingMoreHelper.getOffsetState() != LoadingMoreState.INIT) {
                    int consumedByLoadMore = offsetLoadingMoreStateView(-dyUnconsumed, true);
                    consumed[1] += consumedByLoadMore;
                    dispatchNestedScroll(dxConsumed, dyConsumed + consumedByLoadMore, dxUnconsumed, dyUnconsumed, consumed);
                }
            } else {
                int consumedByLoadMore = offsetLoadingMoreStateView(-dyUnconsumed, true);
                consumed[1] += consumedByLoadMore;
                if (dyUnconsumed - consumedByLoadMore != 0) {
                    dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, consumed);
                }
            }
        } else if (dyUnconsumed > 0) {
            int consumedByLoadMore = offsetLoadingMoreStateView(-dyUnconsumed, false);
            consumed[1] += consumedByLoadMore;
        }
    }
}