package com.reactnative.pulltorefresh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;
import com.reactnative.pulltorefresh.event.OffsetChangedEvent;
import com.reactnative.pulltorefresh.event.RefreshEvent;
import com.reactnative.pulltorefresh.event.StateChangedEvent;

import java.util.Map;


public class ReactSmartPullRefreshHeaderManager extends ReactViewManager {
    public final static String REACT_CLASS = "SPullRefreshHeader";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    public ReactViewGroup createViewInstance(ThemedReactContext context) {
        return new ReactSmartPullRefreshHeader(context);
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(ReactSmartPullRefreshHeader reactSmartPullRefreshHeader, boolean refreshing) {
        if (refreshing) {
            reactSmartPullRefreshHeader.beginRefresh();
        } else {
            reactSmartPullRefreshHeader.finishRefresh();
        }
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(RefreshEvent.Name, MapBuilder.of("registrationName", RefreshEvent.JSEventName))
                .put(StateChangedEvent.Name, MapBuilder.of("registrationName", StateChangedEvent.JSEventName))
                .put(OffsetChangedEvent.Name, MapBuilder.of("registrationName", OffsetChangedEvent.JSEventName))
                .build();
    }

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull ReactViewGroup view) {
        super.addEventEmitters(reactContext, view);
        if (view instanceof ReactSmartPullRefreshHeader) {
            ReactSmartPullRefreshHeader reactSmartPullRefreshHeader = ((ReactSmartPullRefreshHeader) view);
            int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
            int viewId = view.getId();
            reactSmartPullRefreshHeader.setOnRefreshHeaderChangeListener(new OnRefreshChangeListener() {
                @Override
                public void onRefresh() {
                    if (reactContext.hasActiveReactInstance()) {
                        EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
                        if (eventDispatcher != null) {
                            eventDispatcher.dispatchEvent(new RefreshEvent(surfaceId, viewId));
                        }
                    }
                }

                @Override
                public void onOffsetChange(int offset) {
                    if (reactContext.hasActiveReactInstance()) {
                        EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
                        if (eventDispatcher != null) {
                            eventDispatcher.dispatchEvent(new OffsetChangedEvent(surfaceId, viewId, offset));
                        }
                    }
                }

                @Override
                public void onStateChanged(MJRefreshState state) {
                    if (reactContext.hasActiveReactInstance()) {
                        EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
                        if (eventDispatcher != null) {
                            eventDispatcher.dispatchEvent(new StateChangedEvent(surfaceId, viewId, state));
                        }
                    }
                }
            });
        }
    }
}
