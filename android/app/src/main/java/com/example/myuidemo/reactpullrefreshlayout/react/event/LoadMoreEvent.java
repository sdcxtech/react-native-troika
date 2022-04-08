package com.example.myuidemo.reactpullrefreshlayout.react.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class LoadMoreEvent extends Event<LoadMoreEvent> {
    public static final String Name = "loadMoreEvent";
    public static final String JSEventName = "onLoadMore";
    public LoadMoreEvent(int surfaceId, int viewTag) {
        super(surfaceId, viewTag);
    }

    @Override
    public String getEventName() {
        return Name;
    }

    @Nullable
    protected WritableMap getEventData() {
        return Arguments.createMap();
    }
}
