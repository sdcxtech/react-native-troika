package com.example.myuidemo.listener;

import com.example.myuidemo.reactpullrefreshlayout.react.MJRefreshState;

public interface OnRefreshChangeListener {
    void onRefresh();

    void onOffsetChange(int offset);

    void onStateChanged(MJRefreshState state);
}
