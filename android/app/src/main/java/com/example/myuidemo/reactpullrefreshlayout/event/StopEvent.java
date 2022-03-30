package com.example.myuidemo.reactpullrefreshlayout.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class StopEvent extends Event<StopEvent> {
    public StopEvent(int surfaceId, int viewTag) {
        super(surfaceId, viewTag);
    }

    @Override
    public String getEventName() {
        return "stopEvent";
    }

    @Nullable
    protected WritableMap getEventData() {
        return Arguments.createMap();
    }
}
