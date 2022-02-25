package com.example.myuidemo;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.material.appbar.AppBarLayout;
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

    void refreshAppbarLayoutFixedRangeByAnchorView(AppBarLayoutView appBarLayoutView, View anchorView) {
        refreshAppbarLayoutFixedRange(appBarLayoutView, appBarLayoutView.getHeight() - calculateAnchorViewTop(anchorView));
    }

    @ReactProp(name = "fixedRange")
    public void setFixedRange(AppBarLayoutView view, int top) {
        refreshAppbarLayoutFixedRange(view, top);
    }

    @ReactProp(name = "anchorViewId")
    public void setAnchorViewId(AppBarLayoutView view, int id) {
        View anchorView = view.findViewById(id);
        if (anchorView != null) {
            refreshAppbarLayoutFixedRangeByAnchorView(view, anchorView);
            ViewTreeObserver.OnDrawListener onDrawListener = () -> refreshAppbarLayoutFixedRangeByAnchorView(view, anchorView);
            if (anchorView.getViewTreeObserver().isAlive()) {
                anchorView.getViewTreeObserver().addOnDrawListener(onDrawListener);
            }
            anchorView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View anchorView) {
                }

                @Override
                public void onViewDetachedFromWindow(View anchorView) {
                    if (anchorView.getViewTreeObserver().isAlive()) {
                        anchorView.getViewTreeObserver().removeOnDrawListener(onDrawListener);
                        refreshAppbarLayoutFixedRange(view, 0);
                    }
                }
            });
        }
    }

    int calculateAnchorViewTop(View anchorView) {
        int top = anchorView.getTop();
        ViewParent viewParent = anchorView.getParent();
        while (viewParent != null && !(viewParent instanceof AppBarLayout)) {
            if (viewParent instanceof View) {
                top += ((View) viewParent).getTop();
            }
            viewParent = viewParent.getParent();
        }
        return top;
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }
}
