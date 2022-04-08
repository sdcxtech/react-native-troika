package com.example.myuidemo.reactpullrefreshlayout.react.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class LoadMoreStopEvent extends Event<LoadMoreStopEvent> {
    public static final String Name = "loadMoreStopEvent";
    public static final String JSEventName = "onLoadMoreStop";

    public LoadMoreStopEvent(int surfaceId, int viewTag) {
        super(surfaceId, viewTag);
    }

    @Override
    public String getEventName() {
        return "loadMoreStopEvent";
    }

    @Nullable
    protected WritableMap getEventData() {
        return Arguments.createMap();
    }
}
