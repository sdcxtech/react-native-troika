package com.example.myuidemo.reactpullrefreshlayout.refreshView;

public interface OnPullListener {

    void onRefresh();

    void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset);

    void onRefreshStop();

    void onLoadMore();

    void onLoadMorePull(int offset,int total);

    void onLoadMoreStop();
}