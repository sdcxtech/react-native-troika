package com.example.myuidemo.reactpullrefreshlayout.react;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myuidemo.reactpullrefreshlayout.event.PullEvent;
import com.example.myuidemo.reactpullrefreshlayout.event.RefreshEvent;
import com.example.myuidemo.reactpullrefreshlayout.event.StopEvent;
import com.example.myuidemo.reactpullrefreshlayout.PullRefreshLayout;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateBottomRefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateCenterRefreshOffsetCalculator;
import com.example.myuidemo.reactpullrefreshlayout.offsetCalculator.LocateTopRefreshOffsetCalculator;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
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
                .put("refreshEvent", MapBuilder.of("registrationName", "onRefresh"))
                .put("stopEvent", MapBuilder.of("registrationName", "onStop"))
                .put("pullEvent", MapBuilder.of("registrationName", "onPull"))
                .build();
    }

    @ReactProp(name = "enable")
    public void setEnable(ReactPullRefreshLayout pullRefreshLayout, boolean enable) {
        pullRefreshLayout.setEnabled(enable);
    }

    @ReactProp(name = "refreshing")
    public void setRefreshing(ReactPullRefreshLayout pullRefreshLayout, boolean refreshing) {
        Log.d("TAG", "setRefreshing: " + refreshing);
        if (refreshing) {
            pullRefreshLayout.setToRefreshDirectly(0);
        } else {
            pullRefreshLayout.finishRefresh();
        }
    }

    @ReactProp(name = "enableOverPull")
    public void setOverPull(ReactPullRefreshLayout pullRefreshLayout, boolean enableOverPull) {
        pullRefreshLayout.setEnableOverPull(enableOverPull);
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

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull ReactPullRefreshLayout view) {
        view.setOnPullListener(new PullRefreshLayout.OnPullListener() {
            @Override
            public void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset) {
                WritableMap arguments = Arguments.createMap();
                arguments.putInt("currentRefreshViewOffset", currentRefreshViewOffset);
                arguments.putInt("currentTargetViewOffset", currentTargetViewOffset);
                arguments.putInt("totalRefreshViewOffset", totalRefreshViewOffset);
                arguments.putInt("totalTargetViewOffset", totalTargetViewOffset);
                PullEvent refreshEvent = new PullEvent(UIManagerHelper.getSurfaceId(view), view.getId(), arguments);
                emitEvent(reactContext, view.getId(), refreshEvent);
            }

            @Override
            public void onRefresh() {
                RefreshEvent refreshEvent = new RefreshEvent(UIManagerHelper.getSurfaceId(view), view.getId());
                emitEvent(reactContext, view.getId(), refreshEvent);
            }

            @Override
            public void onStop() {
                StopEvent stopEvent = new StopEvent(UIManagerHelper.getSurfaceId(view), view.getId());
                emitEvent(reactContext, view.getId(), stopEvent);
            }
        });
    }

    void emitEvent(ReactContext reactContext, int viewId, Event event) {
        EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
        if (eventDispatcher != null) {
            eventDispatcher.dispatchEvent(event);
        }
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

}
