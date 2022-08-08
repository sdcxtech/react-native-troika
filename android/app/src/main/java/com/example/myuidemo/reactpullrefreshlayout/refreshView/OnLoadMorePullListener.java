package com.example.myuidemo.reactpullrefreshlayout.refreshView;

public interface OnLoadMorePullListener {

    void onLoadMore();

    void onPull(int offset, int total);

    void onStop();

}