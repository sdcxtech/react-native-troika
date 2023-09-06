package com.reactnative.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.PointerEvents;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.views.view.ReactViewGroup;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

@SuppressLint("RestrictedApi")
public class PullToRefreshHeader extends ReactViewGroup implements RefreshHeader {
    private RefreshKernel mRefreshKernel;

    private OnRefreshChangeListener onRefreshChangeListener;
    private boolean mIsRefreshing = false;

    public void setOnRefreshHeaderChangeListener(OnRefreshChangeListener onRefreshChangeListener) {
        this.onRefreshChangeListener = onRefreshChangeListener;
    }

    public PullToRefreshHeader(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    public void setRefreshing(boolean refreshing) {
        mIsRefreshing = refreshing;
        if (refreshing) {
            beginRefresh();
        } else {
            finishRefresh();
        }
    }

    public void beginRefresh() {
        if (mRefreshKernel != null) {
            RefreshState refreshState = mRefreshKernel.getRefreshLayout().getState();
            if (!refreshState.isFooter && !refreshState.isOpening) {
                View scrollable = mRefreshKernel.getRefreshContent().getScrollableView();
                if (scrollable instanceof ScrollView) {
                    ScrollView scrollView = (ScrollView) scrollable;
                    scrollView.smoothScrollTo(0, 0);
                } else {
                    scrollable.scrollTo(0, 0);
                }
                mRefreshKernel.getRefreshLayout().autoRefresh();
            }
        }
    }

    public void finishRefresh() {
        if (mRefreshKernel != null) {
            RefreshState refreshState = mRefreshKernel.getRefreshLayout().getState();
            if (!refreshState.isFooter && !refreshState.isFinishing) {
                mRefreshKernel.getRefreshLayout().finishRefresh();
            }
        }
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.getRefreshLayout().setOnRefreshListener(refreshLayout -> {
            if (onRefreshChangeListener != null && refreshLayout.getState() == RefreshState.Refreshing) {
                onRefreshChangeListener.onRefresh();
            }
        });
        setRefreshing(mIsRefreshing);
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        if (isDragging && onRefreshChangeListener != null) {
            onRefreshChangeListener.onOffsetChange(offset);
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureMode = MeasureSpec.getMode(heightMeasureSpec);
        if (measureMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getParent() instanceof PullToRefresh && mRefreshKernel == null) {
            PullToRefresh refreshLayout = (PullToRefresh) getParent();
            int h = MeasureSpec.getSize(heightMeasureSpec);
            refreshLayout.setHeaderHeightPx(h);
        }
    }

    PullToRefreshHeaderLocalData headerLocalData = new PullToRefreshHeaderLocalData();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (headerLocalData.viewRect.top == top
                && headerLocalData.viewRect.bottom == bottom
                && headerLocalData.viewRect.left == left
                && headerLocalData.viewRect.right == right) {
            return;
        }
        headerLocalData.viewRect.top = top;
        headerLocalData.viewRect.bottom = bottom;
        headerLocalData.viewRect.left = left;
        headerLocalData.viewRect.right = right;
        Context context = getContext();
        if (context instanceof ReactContext) {
            ReactContext reactContext = (ReactContext) context;
            UIManagerModule uiManagerModule = reactContext.getNativeModule(UIManagerModule.class);
            if (uiManagerModule != null) {
                uiManagerModule.setViewLocalData(getId(), headerLocalData);
            }
        }
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public boolean autoOpen(int duration, float dragRate, boolean animationOnly) {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (onRefreshChangeListener != null) {
            PullToRefreshState oldPullToRefreshState = convertRefreshStateToPullToRefreshState(oldState);
            PullToRefreshState newPullToRefreshState = convertRefreshStateToPullToRefreshState(newState);
            if (newPullToRefreshState != oldPullToRefreshState) {
                onRefreshChangeListener.onStateChanged(newPullToRefreshState);
            }
        }
    }

    @Override
    public PointerEvents getPointerEvents() {
        RefreshState refreshState = mRefreshKernel != null ? mRefreshKernel.getRefreshLayout().getState() : RefreshState.None;
        if (refreshState.isHeader && refreshState.isOpening) {
            return super.getPointerEvents();
        }
        return PointerEvents.NONE;
    }

    private PullToRefreshState convertRefreshStateToPullToRefreshState(RefreshState state) {
        if (state == RefreshState.ReleaseToRefresh) {
            return PullToRefreshState.Coming;
        }
        if (state == RefreshState.Refreshing || state == RefreshState.RefreshReleased) {
            return PullToRefreshState.Refreshing;
        }
        return PullToRefreshState.Idle;
    }

}
