package com.reactnative.bottomsheet;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class StateChangedEvent extends Event<StateChangedEvent> {
    public static final String Name = "stateEvent";
    public static final String JSEventName = "onStateChanged";

    private final String state;

    public StateChangedEvent(int surfaceId, int viewTag, String state) {
        super(surfaceId, viewTag);
        this.state = state;
    }

    @Override
    public String getEventName() {
        return Name;
    }

    @Nullable
    protected WritableMap getEventData() {
        WritableMap data = Arguments.createMap();
        data.putString("state", state);
        return data;
    }
}
