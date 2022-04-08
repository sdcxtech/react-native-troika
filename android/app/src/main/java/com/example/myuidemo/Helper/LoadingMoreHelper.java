package com.example.myuidemo.Helper;

import android.view.View;

public class LoadingMoreHelper {
    private static final String TAG = "LoadingMoreHelper";
    private View mLoadMoreView;

    private boolean enable = true;
    private boolean isLoadingMore = false;

    int mLoadMoreInitOffset = 0;
    int mLoadMoreLoadOffset = 0;
    int mLoadMoreCurrentOffset = mLoadMoreInitOffset;
    int previousLoadMoreViewHeight = -1;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setLoadingMore(boolean loadingMore) {
        if (enable) {
            isLoadingMore = loadingMore;
        } else {
            isLoadingMore = false;
        }
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public LoadingMoreHelper() {
    }

    public void setLoadMoreView(View view) {
        mLoadMoreView = view;
    }


    public void calculateLoadMoreOffsetConfig(boolean forceCalculate) {
        if (mLoadMoreView == null) {
            return;
        }

        int loadMoreViewHeight = mLoadMoreView.getMeasuredHeight();
        if (previousLoadMoreViewHeight == -1
                || previousLoadMoreViewHeight != loadMoreViewHeight
                || forceCalculate
        ) {
            previousLoadMoreViewHeight = loadMoreViewHeight;
            mLoadMoreInitOffset = 0;
            mLoadMoreLoadOffset = mLoadMoreInitOffset - loadMoreViewHeight;
            mLoadMoreCurrentOffset = isLoadingMore ? mLoadMoreLoadOffset : mLoadMoreInitOffset;
        }
    }

    public int getLoadMoreCurrentOffset() {
        return mLoadMoreCurrentOffset;
    }

    public int dyWithInit() {
        return mLoadMoreCurrentOffset - mLoadMoreInitOffset;
    }

    public int dyWithLoadMore() {
        return mLoadMoreCurrentOffset - mLoadMoreLoadOffset;
    }

    public int totalOffset() {
        return mLoadMoreLoadOffset - mLoadMoreInitOffset;
    }

    public LoadingMoreState getOffsetState() {
        if (mLoadMoreCurrentOffset < mLoadMoreLoadOffset) return LoadingMoreState.OVER_LOAD_MORE;
        if (mLoadMoreCurrentOffset == mLoadMoreLoadOffset) return LoadingMoreState.LOAD_MORE;
        if (mLoadMoreCurrentOffset < mLoadMoreInitOffset) return LoadingMoreState.OVER_INIT;
        return LoadingMoreState.INIT;
    }

    int calculateLoadMoreOffset(int offset, int mLoadMoreInitOffset, int mLoadMoreEndOffset, boolean enableOverPull) {
        if (offset < mLoadMoreEndOffset && !enableOverPull) {
            return mLoadMoreEndOffset;
        }
        return Math.min(offset, mLoadMoreInitOffset);
    }

    public int moveViewToPosition(int position, boolean enableOverPull) {
        if (!enable) {
            return 0;
        }

        if (mLoadMoreView == null) {
            return 0;
        }

        int newPositionOffset = calculateLoadMoreOffset(position, mLoadMoreInitOffset, mLoadMoreLoadOffset, enableOverPull);
        int delta = 0;
        if (newPositionOffset != mLoadMoreCurrentOffset) {
            delta = newPositionOffset - mLoadMoreCurrentOffset;
            mLoadMoreCurrentOffset = newPositionOffset;
        }
        return delta;
    }

    public boolean isFinalYInLoadMorePosition(int finalY) {
        return finalY <= mLoadMoreLoadOffset;
    }
}
