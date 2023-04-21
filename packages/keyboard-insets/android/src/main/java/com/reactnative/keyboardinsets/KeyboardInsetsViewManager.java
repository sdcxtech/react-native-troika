package com.reactnative.keyboardinsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class KeyboardInsetsViewManager extends ViewGroupManager<KeyboardInsetsView> {

    public static final String REACT_CLASS = "KeyboardInsetsView";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected KeyboardInsetsView createViewInstance(@NonNull ThemedReactContext themedReactContext) {
        KeyboardInsetsView view = new KeyboardInsetsView(themedReactContext);
        KeyboardInsetsCallback callback = new KeyboardInsetsCallback(view, themedReactContext);
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
