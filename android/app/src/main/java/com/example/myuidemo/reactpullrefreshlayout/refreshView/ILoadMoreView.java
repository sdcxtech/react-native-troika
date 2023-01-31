package com.example.myuidemo.reactpullrefreshlayout.refreshView;

public interface ILoadMoreView {

    void onLoadMore();

    void onPull(int offset, int total);

    void onStop();

}