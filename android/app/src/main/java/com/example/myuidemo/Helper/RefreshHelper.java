package com.example.myuidemo.Helper;

import android.view.View;

import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateTopRefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.RefreshOffsetCalculator;


public class RefreshHelper {
    private View mRefreshView;

    private final int mTargetInitOffset = 0;
    private int mTargetEndOffset;
    private int mTargetCurrentOffset = mTargetInitOffset;

    private int mRefreshInitOffset = Integer.MIN_VALUE;
    private final int mRefreshEndOffset = 0;
    private int mRefreshCurrentOffset = mRefreshInitOffset;

    private RefreshOffsetCalculator mRefreshOffsetCalculator;

    private int previousRefreshViewHeight = -1;

    private boolean mIsRefreshing = false;
    private boolean mEnableOverPull = true;
    private boolean mEnable = true;

    public RefreshHelper() {
    }

    public void setRefreshView(View view) {
        mRefreshView = view;
    }

    public void setRefreshing(boolean refreshing) {
        this.mIsRefreshing = refreshing;
    }

    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public boolean isEnable() {
        return mEnable;
    }

    public void setEnableOverPull(boolean enableOverPull) {
        mEnableOverPull = enableOverPull;
    }

    public int dyOfTargetViewWithInitPosition() {
        return mTargetCurrentOffset - mTargetInitOffset;
    }

    public int totalOffsetOfTarget() {
        return mTargetEndOffset - mTargetInitOffset;
    }

    public int dyOfTargetViewWithRefreshPosition() {
        return mTargetCurrentOffset - mTargetEndOffset;
    }

    public int totalOffsetOfRefresh() {
        return mRefreshEndOffset - mRefreshInitOffset;
    }

    public int dyOfRefreshViewWithInitPosition() {
        return mRefreshCurrentOffset - mRefreshInitOffset;
    }

    public int getTargetViewCurrentOffset() {
        return mTargetCurrentOffset;
    }

    public int getRefreshViewCurrentOffset() {
        return mRefreshCurrentOffset;
    }

    public RefreshState getOffsetState() {
        if (mTargetCurrentOffset > mTargetEndOffset) return RefreshState.OVER_PULL;
        if (mTargetCurrentOffset == mTargetEndOffset) return RefreshState.REFRESH;
        if (mTargetCurrentOffset > mTargetInitOffset) return RefreshState.PULL;
        return RefreshState.INIT;
    }

    public void calculateOffsetConfig() {
        calculateOffsetConfig(false);
    }

    public void calculateOffsetConfig(boolean forceCalculate) {
        if (mRefreshView == null) return;

        int refreshViewHeight = mRefreshView.getMeasuredHeight();
        if (previousRefreshViewHeight == -1 || previousRefreshViewHeight != refreshViewHeight || forceCalculate) {
            previousRefreshViewHeight = refreshViewHeight;
            if (mRefreshInitOffset != -refreshViewHeight) {
                mRefreshInitOffset = -refreshViewHeight;
            }
            mTargetEndOffset = refreshViewHeight;
            mRefreshCurrentOffset = mIsRefreshing ? mRefreshEndOffset : mRefreshInitOffset;
            mTargetCurrentOffset = mIsRefreshing ? mTargetEndOffset : mTargetInitOffset;
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

    public int moveTargetViewTo(int target, boolean calculateAnyWay) {
        if (!mEnable || mRefreshView == null) {
            return 0;
        }

        target = calculateTargetOffset(target, mTargetInitOffset, mTargetEndOffset, mEnableOverPull);
        int offset = 0;
        if (target != mTargetCurrentOffset || calculateAnyWay) {
            offset = target - mTargetCurrentOffset;
            mTargetCurrentOffset = target;
        }
        return offset;
    }

    /**
     * 设置在下拉过程中计算 RefreshView 偏移量的方法
     */
    public void setRefreshOffsetCalculator(RefreshOffsetCalculator refreshOffsetCalculator) {
        mRefreshOffsetCalculator = refreshOffsetCalculator;
    }

    public int moveRefreshViewToNewLocation() {
        if (!mEnable || mRefreshView == null) {
            return 0;
        }

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
        int offset = 0;
        if (newRefreshOffset != mRefreshCurrentOffset) {
            offset = newRefreshOffset - mRefreshCurrentOffset;
            mRefreshCurrentOffset = newRefreshOffset;
        }
        return offset;
    }

    public boolean isFinalYInRefreshPosition(int finalY) {
        return finalY >= mTargetEndOffset;
    }
}
