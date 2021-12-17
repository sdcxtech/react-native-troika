package com.example.myuidemo;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.material.appbar.AppBarLayout;

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

    @ReactProp(name = "height")
    public void setHeight(AppBarLayoutView view, double height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height != 0 ? (int) PixelUtil.toPixelFromDIP(height) : AppBarLayout.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(params);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
