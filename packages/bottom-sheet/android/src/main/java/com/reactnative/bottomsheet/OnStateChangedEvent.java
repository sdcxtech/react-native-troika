package com.reactnative.bottomsheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class OnStateChangedEvent extends Event<OnStateChangedEvent> {
    public static final String Name = "topStateChanged";
    public static final String JSEventName = "onStateChanged";

    private final String state;

    public OnStateChangedEvent(int surfaceId, int viewTag, String state) {
        super(surfaceId, viewTag);
        this.state = state;
    }

	@NonNull
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
