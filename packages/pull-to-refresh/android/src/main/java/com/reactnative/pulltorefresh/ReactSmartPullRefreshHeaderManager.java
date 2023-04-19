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


public class ReactSmartPullRefreshHeaderManager extends ReactViewManager {
    public final static String REACT_CLASS = "RefreshHeader";

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

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new ReactSmartPullRefreshHeaderShadowNode();
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return ReactSmartPullRefreshHeaderShadowNode.class;
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(ReactSmartPullRefreshHeader reactSmartPullRefreshHeader, boolean refreshing) {
        reactSmartPullRefreshHeader.setRefreshing(refreshing);
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
    public void receiveCommand(ReactViewGroup root, String commandId, @Nullable ReadableArray args) {
        if (root instanceof ReactSmartPullRefreshHeader) {
            ReactSmartPullRefreshHeader header = (ReactSmartPullRefreshHeader) root;
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
