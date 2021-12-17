package com.example.myuidemo;

import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.google.android.material.appbar.AppBarLayout;

public class CoordinatorLayoutManager extends ViewGroupManager<CoordinatorLayoutView> {

    private final static String REACT_CLASS = "CoordinatorLayoutAndroid";

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
        if (child instanceof ScrollView || child instanceof ViewPager || child instanceof ViewPager2) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            AppBarLayout.ScrollingViewBehavior behavior = new AppBarLayout.ScrollingViewBehavior();
            params.setBehavior(behavior);
        }
    }

//    @Override
//    public boolean needsCustomLayoutForChildren() {
//        return true;
//    }

}
