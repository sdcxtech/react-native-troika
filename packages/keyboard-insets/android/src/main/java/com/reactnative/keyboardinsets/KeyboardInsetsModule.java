package com.reactnative.keyboardinsets;

import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.UIManagerHelper;

public class KeyboardInsetsModule extends ReactContextBaseJavaModule {

    public KeyboardInsetsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "KeyboardInsetsModule";
    }

    @ReactMethod
    public void getEdgeInsetsForView(int viewTag, Callback callback) {
        UiThreadUtil.runOnUiThread(() -> {
			UIManager uiManager = UIManagerHelper.getUIManager(getReactApplicationContext(), 2);
			assert uiManager != null;
            View view = uiManager.resolveView(viewTag);
            WritableMap map = Arguments.createMap();
            map.putDouble("top", 0);
            map.putDouble("left", 0);
            map.putDouble("right", 0);
            map.putDouble("bottom", 0);
            if (view != null) {
                EdgeInsets insets = SystemUI.getEdgeInsetsForView(view);
                map.putDouble("top", PixelUtil.toDIPFromPixel(insets.top));
                map.putDouble("left", PixelUtil.toDIPFromPixel(insets.left));
                map.putDouble("right", PixelUtil.toDIPFromPixel(insets.right));
                map.putDouble("bottom", PixelUtil.toDIPFromPixel(insets.bottom));
            }
            callback.invoke(map);
        });
    }
}
