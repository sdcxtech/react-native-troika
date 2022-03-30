package com.example.myuidemo.reactpullrefreshlayout.react;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

public class RefreshViewWrapperManager extends ViewGroupManager<RefreshViewWrapper> {
    @NonNull
    @Override
    public String getName() {
        return "RefreshViewWrapper";
    }

    @NonNull
    @Override
    protected RefreshViewWrapper createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RefreshViewWrapper(reactContext);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return false;
    }

}
