package com.example.myuidemo;


import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

public class CoordinatorLayoutManager extends ViewGroupManager<CoordinatorLayoutView> {

    private final static String REACT_CLASS = "CoordinatorLayout";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    ReactContext mContext;

    @NonNull
    @Override
    protected CoordinatorLayoutView createViewInstance(@NonNull ThemedReactContext reactContext) {
        mContext = reactContext;
        return new CoordinatorLayoutView(reactContext);
    }
}
