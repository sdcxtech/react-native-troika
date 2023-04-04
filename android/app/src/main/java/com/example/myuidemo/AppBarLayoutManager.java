package com.example.myuidemo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.material.appbar.AppBarLayout;

public class AppBarLayoutManager extends ViewGroupManager<AppBarLayoutView> {
    public final static String REACT_CLASS = "AppBarLayout";

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
    public void addView(AppBarLayoutView parent, View child, int index) {
        AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        child.setLayoutParams(layoutParams);
        super.addView(parent, child, index);

    }

    @ReactProp(name = "fixedHeight")
    public void setFixedHeight(AppBarLayoutView view, int fixedHeight) {
        view.setAppbarLayoutFixedHeight((int) PixelUtil.toPixelFromDIP(fixedHeight));
    }

    @ReactProp(name = "stickyHeaderBeginIndex")
    public void setStickyHeaderBeginIndex(AppBarLayoutView view, int stickyHeaderBeginIndex) {
        view.setStickyHeaderBeginIndex(stickyHeaderBeginIndex);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
