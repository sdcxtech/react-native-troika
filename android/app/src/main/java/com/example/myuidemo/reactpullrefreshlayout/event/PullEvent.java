package com.example.myuidemo.reactpullrefreshlayout.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class PullEvent extends Event<PullEvent> {
    private WritableMap writableMap;

    public PullEvent(int surfaceId, int viewTag, WritableMap params) {
        super(surfaceId, viewTag);
        this.writableMap = params;
    }


    @Override
    public String getEventName() {
        return "pullEvent";
    }

    @Nullable
    @Override
    protected WritableMap getEventData() {
        if (this.writableMap != null) {
            return this.writableMap;
        }
        return Arguments.createMap();
    }
}
