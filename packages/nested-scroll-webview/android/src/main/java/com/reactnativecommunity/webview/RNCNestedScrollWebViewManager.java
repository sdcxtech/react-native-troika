package com.reactnativecommunity.webview;

import androidx.annotation.NonNull;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;

@ReactModule(name = RNCWebViewManagerImpl.NAME)
public class RNCNestedScrollWebViewManager extends RNCWebViewManager {

    @NonNull
    @Override
    public RNCWebViewWrapper createViewInstance(@NonNull ThemedReactContext context) {
        return createViewInstance(context, new RNCNestedScrollWebView(context));
    }

    @Override
    public boolean canOverrideExistingModule() {
        return true;
    }
}
