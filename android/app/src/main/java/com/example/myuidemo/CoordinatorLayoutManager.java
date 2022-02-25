package com.example.myuidemo;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.google.android.material.appbar.AppbarLayoutHeaderBehavior;
import com.google.android.material.appbar.ScrollViewBehavior;

public class CoordinatorLayoutManager extends ViewGroupManager<CoordinatorLayoutView> {

    private final static String REACT_CLASS = "CoordinatorLayout";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected CoordinatorLayoutView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new CoordinatorLayoutView(reactContext);
    }

    @Override
    public void addView(CoordinatorLayoutView parent, View child, int index) {
        super.addView(parent, child, index);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (!(child instanceof AppBarLayoutView)) {
            ScrollViewBehavior behavior = new ScrollViewBehavior();
            params.setBehavior(behavior);
        }else {
            AppbarLayoutHeaderBehavior behavior = new AppbarLayoutHeaderBehavior();
            params.setBehavior(behavior);
        }
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

}
