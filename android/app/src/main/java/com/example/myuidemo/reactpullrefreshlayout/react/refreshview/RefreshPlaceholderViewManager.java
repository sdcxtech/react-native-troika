package com.example.myuidemo.reactpullrefreshlayout.react.refreshview;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

public class RefreshPlaceholderViewManager extends ViewGroupManager<RefreshPlaceholderView> {
    private final static String NAME = "PullRefreshLayoutRefreshPlaceholderViewManager";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @NonNull
    @Override
    protected RefreshPlaceholderView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RefreshPlaceholderView(reactContext);
    }
}
