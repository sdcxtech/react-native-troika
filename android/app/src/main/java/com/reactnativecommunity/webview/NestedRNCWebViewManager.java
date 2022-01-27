package com.reactnativecommunity.webview;

import static com.reactnativecommunity.webview.RNCWebViewManager.REACT_CLASS;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;

@ReactModule(name = REACT_CLASS)
public class NestedRNCWebViewManager extends RNCWebViewManager {
    @Override
    protected RNCWebView createRNCWebViewInstance(ThemedReactContext reactContext) {
        return new NestedRNCWebView(reactContext);
    }

    @Override
    public boolean canOverrideExistingModule() {
        return true;
    }
}
