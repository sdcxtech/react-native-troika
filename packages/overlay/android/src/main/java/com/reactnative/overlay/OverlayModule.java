package com.reactnative.overlay;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.facebook.common.logging.FLog;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;

import java.util.HashMap;

public class OverlayModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final HashMap<String, Overlay> overlays = new HashMap<>();
    private final ReactApplicationContext reactContext;

    private final ReactNativeHost reactNativeHost;

    public OverlayModule(ReactApplicationContext reactContext, ReactNativeHost reactNativeHost) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactNativeHost = reactNativeHost;
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public void invalidate() {
        reactContext.removeLifecycleEventListener(this);
        final Activity activity = getCurrentActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        UiThreadUtil.runOnUiThread(this::handleDestroy);
    }

    private void handleDestroy() {
        for (String key : overlays.keySet()) {
            Overlay overlay = overlays.get(key);
            overlay.hide();
        }
        overlays.clear();
    }

    @NonNull
    @Override
    public String getName() {
        return "OverlayHost";
    }

    @ReactMethod
    public void show(final String moduleName, final ReadableMap props, final ReadableMap options) {
        UiThreadUtil.runOnUiThread(() -> {
            final Activity activity = getCurrentActivity();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            Overlay overlay = overlays.get(moduleName);
            if (overlay != null) {
                overlay.update();
                return;
            }

            overlay = new Overlay(activity, moduleName, reactNativeHost.getReactInstanceManager());
            overlay.show(props, options);
            overlays.put(moduleName, overlay);
        });
    }

    @ReactMethod
    public void hide(String moduleName) {
        UiThreadUtil.runOnUiThread(() -> {
            Overlay overlay = overlays.get(moduleName);
            if (overlay == null) {
                return;
            }
            overlays.remove(moduleName);
            overlay.hide();
        });
    }

    @Override
    public void onHostResume() {
        UiThreadUtil.assertOnUiThread();
    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        FLog.i("OverlayModule", "onHostDestroy");
        handleDestroy();
    }
}
