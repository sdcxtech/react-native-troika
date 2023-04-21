package com.reactnative.overlay;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;

@UiThread
public class Overlay {

    final Activity activity;
    final String moduleName;

    final ReactInstanceManager reactInstanceManager;

    OverlayRootView rootView;
    ViewGroup decorView;

    public Overlay(@NonNull Activity activity, String moduleName, ReactInstanceManager reactInstanceManager) {
        this.activity = activity;
        this.moduleName = moduleName;
        this.reactInstanceManager = reactInstanceManager;
    }

    public void show(ReadableMap props, ReadableMap options) {
        OverlayRootView reactRootView = createReactRootView();

        if (options.hasKey("passThroughTouches")) {
            reactRootView.setShouldConsumeTouchEvent(!options.getBoolean("passThroughTouches"));
        }

        this.rootView = reactRootView;
        startReactApplication(reactRootView, Arguments.toBundle(props));
        decorView = getDecorView();
        if (decorView != null) {
            decorView.addView(reactRootView);
        }
    }

    public void hide() {
        if (decorView != null) {
            decorView.removeView(rootView);
            decorView = null;
        }

        unmountReactView();
    }

    public void update() {
        ViewGroup decorView = getDecorView();
        if (decorView != null && decorView != this.decorView) {
            this.decorView.removeView(rootView);
            this.decorView = decorView;
            decorView.addView(rootView);
        }
    }

    private void unmountReactView() {
        ReactContext reactContext = reactInstanceManager.getCurrentReactContext();
        if (reactContext == null || !reactContext.hasActiveCatalystInstance()) {
            return;
        }

        if (rootView != null) {
            rootView.unmountReactApplication();
            rootView = null;
        }
    }

    private void startReactApplication(OverlayRootView reactRootView, Bundle props) {
        reactRootView.startReactApplication(reactInstanceManager, moduleName, props);
    }

    private OverlayRootView createReactRootView() {
        OverlayRootView reactRootView = new OverlayRootView(activity);
        reactRootView.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER));
        return reactRootView;
    }

    private ViewGroup getDecorView() {
        Window window = activity.getWindow();
        if (window == null) {
            return null;
        }
        return (ViewGroup) window.getDecorView();
    }

}
