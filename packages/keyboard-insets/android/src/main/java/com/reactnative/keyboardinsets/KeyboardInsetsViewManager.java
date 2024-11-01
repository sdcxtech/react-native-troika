package com.reactnative.keyboardinsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

import java.util.Map;

public class KeyboardInsetsViewManager extends ReactViewManager {

    public static final String REACT_CLASS = "KeyboardInsetsView";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }
    
    @NonNull
    @Override
    public ReactViewGroup createViewInstance(ThemedReactContext context) {
        KeyboardInsetsView view = new KeyboardInsetsView(context);
        KeyboardInsetsCallback callback = new KeyboardInsetsCallback(view, context);
        ViewCompat.setWindowInsetsAnimationCallback(view, callback);
        ViewCompat.setOnApplyWindowInsetsListener(view, callback);
        return view;
    }

    @ReactProp(name = "mode")
    public void setMode(KeyboardInsetsView view, String mode) {
        view.setMode(mode);
    }
    
    @ReactProp(name = "extraHeight")
    public void setExtraHeight(KeyboardInsetsView view, float extraHeight) {
        view.setExtraHeight(extraHeight);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
            "topStatusChanged", MapBuilder.of("registrationName", "onStatusChanged"),
            "topPositionChanged", MapBuilder.of("registrationName", "onPositionChanged")
        );
    }
    
}
