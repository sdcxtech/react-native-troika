package com.example.myuidemo;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

public class AppBarLayoutManager extends ViewGroupManager<AppBarLayoutView> {

    private final static String REACT_CLASS = "AppBarLayout";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected AppBarLayoutView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new AppBarLayoutView(reactContext);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
