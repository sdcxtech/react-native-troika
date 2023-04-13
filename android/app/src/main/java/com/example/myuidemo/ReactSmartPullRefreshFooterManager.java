package com.example.myuidemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myuidemo.listener.OnRefreshChangeListener;
import com.example.myuidemo.reactpullrefreshlayout.react.MJRefreshState;
import com.example.myuidemo.reactpullrefreshlayout.react.event.OffsetChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.StateChangedEvent;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

import java.util.Map;


public class ReactSmartPullRefreshFooterManager extends ReactViewManager {
    public final static String REACT_CLASS = "SPullRefreshFooter";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    public ReactViewGroup createViewInstance(ThemedReactContext context) {
        return new ReactSmartPullRefreshFooter(context);
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(ReactSmartPullRefreshFooter footer, boolean refreshing) {
        if (refreshing) {
            footer.beginLoadMore();
        } else {
            footer.finishLoadMore();
        }
    }

    @ReactProp(name = "noMoreData")
    public void setNoMoreData(ReactSmartPullRefreshFooter footer, boolean noMoreData) {
        footer.setNoMoreData(noMoreData);
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
        if (view instanceof ReactSmartPullRefreshFooter) {
            ReactSmartPullRefreshFooter reactSmartPullRefreshFooter = ((ReactSmartPullRefreshFooter) view);
            int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
            int viewId = view.getId();
            reactSmartPullRefreshFooter.setOnRefreshHeaderChangeListener(new OnRefreshChangeListener() {
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
