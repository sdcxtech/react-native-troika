package com.example.myuidemo.reactpullrefreshlayout.react.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class RefreshStopEvent extends Event<RefreshStopEvent> {
    public static final String Name = "refreshStopEvent";
    public static final String JSEventName = "onRefreshStop";

    public RefreshStopEvent(int surfaceId, int viewTag) {
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
