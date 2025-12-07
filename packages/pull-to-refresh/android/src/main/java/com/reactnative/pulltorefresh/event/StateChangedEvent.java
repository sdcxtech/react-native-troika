package com.reactnative.pulltorefresh.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.reactnative.pulltorefresh.PullToRefreshState;

public class StateChangedEvent extends Event<StateChangedEvent> {

    public static final String Name = "topStateChanged";
    public static final String JSEventName = "onStateChanged";

    private final PullToRefreshState state;

    public StateChangedEvent(int surfaceId, int viewTag, PullToRefreshState state) {
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
        WritableMap map = Arguments.createMap();
        map.putInt("state", state.ordinal());
        return map;
    }

}
