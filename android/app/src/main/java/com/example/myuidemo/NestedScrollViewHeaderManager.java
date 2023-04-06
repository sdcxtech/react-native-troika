package com.example.myuidemo;


import androidx.annotation.NonNull;

import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

public class NestedScrollViewHeaderManager extends ReactViewManager {
    public final static String REACT_CLASS = "NestedScrollViewHeader";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    public ReactViewGroup createViewInstance(ThemedReactContext context) {
        return new NestedScrollViewHeader(context);
    }

    @ReactProp(name = "fixedHeight", defaultInt = NestedScrollViewHeader.INVALID_FIXED_HEIGHT)
    public void setFixedHeight(NestedScrollViewHeader view, int fixedHeight) {
        view.setFixedHeight((int) PixelUtil.toPixelFromDIP(fixedHeight));
    }

    @ReactProp(name = "stickyHeaderBeginIndex", defaultInt = NestedScrollViewHeader.INVALID_STICKY_BEGIN_INDEX)
    public void setStickyHeaderBeginIndex(NestedScrollViewHeader view, int stickyHeaderBeginIndex) {
        view.setStickyHeaderBeginIndex(stickyHeaderBeginIndex);
    }
}
