package com.example.myuidemo.reactpullrefreshlayout.react.refreshview;

import androidx.annotation.NonNull;

import com.example.myuidemo.reactpullrefreshlayout.react.ReactPullRefreshLayout;
import com.example.myuidemo.reactpullrefreshlayout.react.event.OffsetChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshStateChangedEvent;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class RefreshPlaceholderViewManager extends ViewGroupManager<RefreshPlaceholderView> {
    private final static String NAME = "RefreshHeader";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @NonNull
    @Override
    protected RefreshPlaceholderView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RefreshPlaceholderView(reactContext);
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new RefreshPlaceholderShadowNode();
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return RefreshPlaceholderShadowNode.class;
    }

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull RefreshPlaceholderView view) {
        super.addEventEmitters(reactContext, view);
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(RefreshStateChangedEvent.Name, MapBuilder.of("registrationName", RefreshStateChangedEvent.JSEventName))
                .put(OffsetChangedEvent.Name, MapBuilder.of("registrationName", OffsetChangedEvent.JSEventName))
                .build();
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(RefreshPlaceholderView view, boolean refreshing) {
         ReactPullRefreshLayout pullToRefresh = (ReactPullRefreshLayout) view.getParent();
         if (pullToRefresh != null) {
             pullToRefresh.setRefreshing(refreshing);
         }
    }

}
