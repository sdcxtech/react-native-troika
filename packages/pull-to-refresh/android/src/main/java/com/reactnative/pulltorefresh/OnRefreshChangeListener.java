package com.reactnative.pulltorefresh;

import com.reactnative.pulltorefresh.MJRefreshState;

public interface OnRefreshChangeListener {
    void onRefresh();

    void onOffsetChange(int offset);

    void onStateChanged(MJRefreshState state);
}
