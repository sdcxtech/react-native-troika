package com.example.myuidemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class NestedScrollViewManager extends ViewGroupManager<NestedScrollView> {

    @NonNull
    @Override
    public String getName() {
        return "NestedScrollView";
    }

    @NonNull
    @Override
    protected NestedScrollView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new NestedScrollView(reactContext);
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new NestedScrollViewShadowNode();
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return NestedScrollViewShadowNode.class;
    }

    @ReactProp(name = "overflow")
    public void setOverflow(NestedScrollView view, @Nullable String overflow) {
        view.setOverflow(overflow);
    }
}
