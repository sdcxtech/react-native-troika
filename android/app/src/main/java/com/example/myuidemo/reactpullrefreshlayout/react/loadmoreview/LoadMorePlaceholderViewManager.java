package com.example.myuidemo.reactpullrefreshlayout.react.loadmoreview;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

public class LoadMorePlaceholderViewManager extends ViewGroupManager<LoadMorePlaceholderView> {
    private final static String NAME = "PullRefreshLayoutLoadMorePlaceholderViewManager";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @NonNull
    @Override
    protected LoadMorePlaceholderView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new LoadMorePlaceholderView(reactContext);
    }
}
