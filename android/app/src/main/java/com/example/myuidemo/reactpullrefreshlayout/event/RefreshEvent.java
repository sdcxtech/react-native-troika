package com.example.myuidemo.reactpullrefreshlayout.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class RefreshEvent extends Event<RefreshEvent> {
    public RefreshEvent(int surfaceId, int viewTag) {
        super(surfaceId, viewTag);
    }

    @Override
    public String getEventName() {
        return "refreshEvent";
    }

    @Nullable
    protected WritableMap getEventData() {
        return Arguments.createMap();
    }
}
