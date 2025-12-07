package com.reactnative.pulltorefresh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
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


public class PullToRefreshHeaderManager extends ReactViewManager {
    public final static String REACT_CLASS = "PullToRefreshHeader";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    public ReactViewGroup createViewInstance(@NonNull ThemedReactContext context) {
        return new PullToRefreshHeader(context);
    }

//    @Override
//    public LayoutShadowNode createShadowNodeInstance() {
//        return new PullToRefreshHeaderShadowNode();
//    }
//
//    @Override
//    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
//        return PullToRefreshHeaderShadowNode.class;
//    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(PullToRefreshHeader pullToRefreshHeader, boolean refreshing) {
        pullToRefreshHeader.setRefreshing(refreshing);
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
    public void receiveCommand(@NonNull ReactViewGroup root, @NonNull String commandId, @Nullable ReadableArray args) {
        if (root instanceof PullToRefreshHeader) {
            PullToRefreshHeader header = (PullToRefreshHeader) root;
            if ("setNativeRefreshing".equals(commandId)) {
                if (args != null && args.getType(0) == ReadableType.Boolean) {
                    header.setRefreshing(args.getBoolean(0));
                }
            }
        }
    }

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull ReactViewGroup view) {
        super.addEventEmitters(reactContext, view);
        if (view instanceof PullToRefreshHeader) {
            PullToRefreshHeader pullToRefreshHeader = ((PullToRefreshHeader) view);
            int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
            int viewId = view.getId();
            pullToRefreshHeader.setOnRefreshHeaderChangeListener(new OnRefreshChangeListener() {
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
                public void onStateChanged(PullToRefreshState state) {
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
