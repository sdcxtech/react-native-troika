package com.example.myuidemo;

import androidx.annotation.NonNull;

import com.example.myuidemo.reactpullrefreshlayout.react.PullRefreshLayoutManager;
import com.example.myuidemo.reactpullrefreshlayout.react.loadmoreview.LoadMorePlaceholderViewManager;
import com.example.myuidemo.reactpullrefreshlayout.react.refreshview.RefreshPlaceholderViewManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.reactnativecommunity.webview.NestedRNCWebViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyUiPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Arrays.asList(
                new NestedRNCWebViewManager(),
                new PullRefreshLayoutManager(),
                new RefreshPlaceholderViewManager(),
                new LoadMorePlaceholderViewManager(),
                new NestedScrollViewManager(),
                new NestedScrollViewHeaderManager()
        );
    }
}
