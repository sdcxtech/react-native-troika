package com.example.myuidemo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.material.appbar.AppbarLayoutHeaderBehavior;

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

    void setAppbarLayoutFixedRange(AppBarLayoutView view, int fixedRange) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (behavior instanceof AppbarLayoutHeaderBehavior) {
                ((AppbarLayoutHeaderBehavior) behavior).setFixedRange(fixedRange);
            }
        }
    }

    void refreshAppbarLayoutFixedRange(AppBarLayoutView appBarLayoutView, int fixedRange) {
        if (appBarLayoutView.isAttachedToWindow()) {
            setAppbarLayoutFixedRange(appBarLayoutView, fixedRange);
        } else {
            appBarLayoutView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    setAppbarLayoutFixedRange(appBarLayoutView, fixedRange);
                    appBarLayoutView.removeOnAttachStateChangeListener(this);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            });
        }
    }

    @ReactProp(name = "fixedRange")
    public void setFixedRange(AppBarLayoutView view, int top) {
        refreshAppbarLayoutFixedRange(view, top);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
