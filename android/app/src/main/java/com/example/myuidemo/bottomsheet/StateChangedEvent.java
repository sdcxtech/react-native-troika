package com.example.myuidemo.bottomsheet;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class StateChangedEvent extends Event<StateChangedEvent> {
    public static final String Name = "stateEvent";
    public static final String JSEventName = "onStateChanged";

    public StateChangedEvent(int surfaceId, int viewTag) {
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
