package com.example.myuidemo.reactpullrefreshlayout.react.event;

import androidx.annotation.Nullable;

import com.example.myuidemo.reactpullrefreshlayout.react.MJRefreshState;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class StateChangedEvent extends Event<StateChangedEvent> {

    public static final String Name = "stateChangedEvent";
    public static final String JSEventName = "onStateChanged";

    private final MJRefreshState state;

    public StateChangedEvent(int surfaceId, int viewTag, MJRefreshState state) {
        super(surfaceId, viewTag);
        this.state = state;
    }

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
