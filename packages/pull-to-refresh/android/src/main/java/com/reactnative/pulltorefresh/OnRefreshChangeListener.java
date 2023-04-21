package com.reactnative.pulltorefresh;

public interface OnRefreshChangeListener {
    void onRefresh();

    void onOffsetChange(int offset);

    void onStateChanged(PullToRefreshState state);
}
