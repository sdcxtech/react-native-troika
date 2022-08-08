package com.example.myuidemo.reactpullrefreshlayout.react;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myuidemo.reactpullrefreshlayout.react.event.LoadMoreEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.LoadMorePullEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.LoadMoreStopEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshPullEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshStopEvent;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateBottomRefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateCenterRefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateTopRefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.OnPullListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;

import java.util.Map;

public class PullRefreshLayoutManager extends ViewGroupManager<ReactPullRefreshLayout> {
    private final static String REACT_CLASS = "PullRefreshLayout";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected ReactPullRefreshLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
        ReactPullRefreshLayout reactPullRefreshLayout = new ReactPullRefreshLayout(reactContext);
        return reactPullRefreshLayout;
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(RefreshEvent.Name, MapBuilder.of("registrationName", RefreshEvent.JSEventName))
                .put(RefreshStopEvent.Name, MapBuilder.of("registrationName", RefreshStopEvent.JSEventName))
                .put(RefreshPullEvent.Name, MapBuilder.of("registrationName", RefreshPullEvent.JSEventName))
                .put(LoadMorePullEvent.Name, MapBuilder.of("registrationName", LoadMorePullEvent.JSEventName))
                .put(LoadMoreEvent.Name, MapBuilder.of("registrationName", LoadMoreEvent.JSEventName))
                .put(LoadMoreStopEvent.Name, MapBuilder.of("registrationName", LoadMoreStopEvent.JSEventName))
                .build();
    }

    @ReactProp(name = "enableRefreshAction")
    public void setEnableRefreshAction(ReactPullRefreshLayout pullRefreshLayout, boolean enable) {
        pullRefreshLayout.enableRefreshAction(enable);
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(ReactPullRefreshLayout pullRefreshLayout, boolean refreshing) {
        pullRefreshLayout.setRefreshing(refreshing);
    }

    @ReactProp(name = "enableRefreshOverPull")
    public void enableRefreshOverPull(ReactPullRefreshLayout pullRefreshLayout, boolean enableOverPull) {
        pullRefreshLayout.enableRefreshOverPull(enableOverPull);
    }

    @ReactProp(name = "refreshViewOverPullLocation")
    public void setRefreshViewOverPullLocation(ReactPullRefreshLayout pullRefreshLayout, String type) {
        if (type.equals("center")) {
            pullRefreshLayout.setRefreshOffsetCalculator(new LocateCenterRefreshOffsetCalculator());
        } else if (type.equals("bottom")) {
            pullRefreshLayout.setRefreshOffsetCalculator(new LocateBottomRefreshOffsetCalculator());
        } else {
            pullRefreshLayout.setRefreshOffsetCalculator(new LocateTopRefreshOffsetCalculator());
        }
    }

    @ReactProp(name = "loadingMore")
    public void setLoadingMore(ReactPullRefreshLayout pullRefreshLayout, boolean loadingMore) {
        pullRefreshLayout.setLoadingMore(loadingMore);
    }

    @ReactProp(name = "enableLoadMoreAction")
    public void setEnableLoadMoreAction(ReactPullRefreshLayout pullRefreshLayout, boolean enable) {
        pullRefreshLayout.enableLoadMoreAction(enable);
    }

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull ReactPullRefreshLayout view) {
        view.setOnPullListener(new OnPullListener() {
            @Override
            public void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset) {
                WritableMap arguments = Arguments.createMap();
                arguments.putInt("currentRefreshViewOffset", currentRefreshViewOffset);
                arguments.putInt("currentTargetViewOffset", currentTargetViewOffset);
                arguments.putInt("totalRefreshViewOffset", totalRefreshViewOffset);
                arguments.putInt("totalTargetViewOffset", totalTargetViewOffset);
                RefreshPullEvent refreshPullEvent = new RefreshPullEvent(UIManagerHelper.getSurfaceId(view), view.getId(), arguments);
                emitEvent(reactContext, view.getId(), refreshPullEvent);
            }

            @Override
            public void onRefresh() {
                RefreshEvent refreshEvent = new RefreshEvent(UIManagerHelper.getSurfaceId(view), view.getId());
                emitEvent(reactContext, view.getId(), refreshEvent);
            }

            @Override
            public void onRefreshStop() {
                RefreshStopEvent refreshStopEvent = new RefreshStopEvent(UIManagerHelper.getSurfaceId(view), view.getId());
                emitEvent(reactContext, view.getId(), refreshStopEvent);
            }

            @Override
            public void onLoadMorePull(int offset, int total) {
                WritableMap arguments = Arguments.createMap();
                arguments.putInt("offset", offset);
                arguments.putInt("total", total);
                LoadMorePullEvent loadMorePullEvent = new LoadMorePullEvent(UIManagerHelper.getSurfaceId(view), view.getId(), arguments);
                emitEvent(reactContext, view.getId(), loadMorePullEvent);
            }

            @Override
            public void onLoadMore() {
                LoadMoreEvent loadMoreEvent = new LoadMoreEvent(UIManagerHelper.getSurfaceId(view), view.getId());
                emitEvent(reactContext, view.getId(), loadMoreEvent);
            }

            @Override
            public void onLoadMoreStop() {
                LoadMoreStopEvent loadMoreStopEvent = new LoadMoreStopEvent(UIManagerHelper.getSurfaceId(view), view.getId());
                emitEvent(reactContext, view.getId(), loadMoreStopEvent);
            }
        });
    }

    void emitEvent(ReactContext reactContext, int viewId, Event event) {
        if (reactContext.hasActiveReactInstance()) {
            EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
            if (eventDispatcher != null) {
                eventDispatcher.dispatchEvent(event);
            }
        }
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    public final int SET_NATIVE_REFRESHING = 1;
    public final int SET_NATIVE_LOADING_MORE = 2;

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "setNativeRefreshing", SET_NATIVE_REFRESHING,
                "setNativeLoadingMore", SET_NATIVE_LOADING_MORE);
    }

    @Override
    public void receiveCommand(@NonNull ReactPullRefreshLayout root, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        int commandIdInt = Integer.parseInt(commandId);
        switch (commandIdInt) {
            case SET_NATIVE_REFRESHING:
                boolean refreshing = args.getBoolean(0);
                root.setRefreshing(refreshing);
                break;
            case SET_NATIVE_LOADING_MORE:
                boolean loadingMore = args.getBoolean(0);
                root.setLoadingMore(loadingMore);
                break;
        }
    }
}
