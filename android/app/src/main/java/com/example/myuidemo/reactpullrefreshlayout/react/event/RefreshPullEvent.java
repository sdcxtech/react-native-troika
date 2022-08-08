package com.example.myuidemo.reactpullrefreshlayout.react.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class RefreshPullEvent extends Event<RefreshPullEvent> {
    public static final String Name = "refreshPullEvent";
    public static final String JSEventName = "onRefreshPull";

    private WritableMap writableMap;

    public RefreshPullEvent(int surfaceId, int viewTag, WritableMap params) {
        super(surfaceId, viewTag);
        this.writableMap = params;
    }


    @Override
    public String getEventName() {
        return Name;
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
