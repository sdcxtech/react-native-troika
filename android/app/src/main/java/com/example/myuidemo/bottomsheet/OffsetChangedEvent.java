package com.example.myuidemo.bottomsheet;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class OffsetChangedEvent extends Event<OffsetChangedEvent> {
    public static final String Name = "offsetEvent";
    public static final String JSEventName = "onOffsetChanged";

    public OffsetChangedEvent(int surfaceId, int viewTag) {
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
