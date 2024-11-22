package com.reactnative.modalx;

import android.app.Activity;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;

public class EdgeToEdgeModule extends ReactContextBaseJavaModule {

    public EdgeToEdgeModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "EdgeToEdgeModule";
    }

    @ReactMethod
    public void setNavigationBarColor(String color) {
        UiThreadUtil.runOnUiThread(() -> {
            Activity activity = getCurrentActivity();
            if (activity != null) {
                if (!SystemUI.isGestureNavigationEnabled(activity.getContentResolver())) {
                    SystemUI.setNavigationBarColor(activity.getWindow(), Color.parseColor(color));
                }
            }
        });
    }

    @ReactMethod
    public void setNavigationBarStyle(String style) {
        UiThreadUtil.runOnUiThread(() -> {
            Activity activity = getCurrentActivity();
            if (activity != null) {
                if (!SystemUI.isGestureNavigationEnabled(activity.getContentResolver())) {
                    SystemUI.setNavigationBarStyle(activity.getWindow(), "dark".equals(style));
                }
            }
        });
    }

}
