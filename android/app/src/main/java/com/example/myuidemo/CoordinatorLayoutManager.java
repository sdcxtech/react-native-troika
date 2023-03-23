package com.example.myuidemo;

import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
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

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull CoordinatorLayoutView view) {
        super.addEventEmitters(reactContext, view);
        view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (mContext != null && mContext.hasActiveReactInstance()) {
                mContext.runOnNativeModulesQueueThread(() -> {
                    UIManagerModule uiManagerModule = mContext.getNativeModule(UIManagerModule.class);
                    if (uiManagerModule != null) {
                        for (int i = 0, count = view.getChildCount(); i < count; i++) {
                            View child = view.getChildAt(i);
                            uiManagerModule.updateNodeSize(child.getId(), child.getWidth(), child.getHeight());
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

}
