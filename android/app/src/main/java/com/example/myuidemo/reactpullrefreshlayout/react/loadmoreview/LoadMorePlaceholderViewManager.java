package com.example.myuidemo.reactpullrefreshlayout.react.loadmoreview;

import androidx.annotation.NonNull;

import com.example.myuidemo.reactpullrefreshlayout.react.ReactPullRefreshLayout;
import com.example.myuidemo.reactpullrefreshlayout.react.event.OffsetChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshStateChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.refreshview.RefreshPlaceholderView;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class LoadMorePlaceholderViewManager extends ViewGroupManager<LoadMorePlaceholderView> {
    private final static String NAME = "RefreshFooter";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @NonNull
    @Override
    protected LoadMorePlaceholderView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new LoadMorePlaceholderView(reactContext);
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(RefreshStateChangedEvent.Name, MapBuilder.of("registrationName", RefreshStateChangedEvent.JSEventName))
                .put(OffsetChangedEvent.Name, MapBuilder.of("registrationName", OffsetChangedEvent.JSEventName))
                .put(RefreshEvent.Name, MapBuilder.of("registrationName", RefreshEvent.JSEventName))
                .build();
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(LoadMorePlaceholderView view, boolean refreshing) {
        ReactPullRefreshLayout pullToRefresh = (ReactPullRefreshLayout) view.getParent();
        if (pullToRefresh != null) {
            pullToRefresh.setLoadingMore(refreshing);
        }
    }
}
