package com.example.myuidemo.reactpullrefreshlayout.react.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class LoadMorePullEvent extends Event<LoadMorePullEvent> {
    public static final String Name = "loadMorePullEvent";
    public static final String JSEventName = "onLoadMorePull";
    private WritableMap writableMap;

    public LoadMorePullEvent(int surfaceId, int viewTag, WritableMap params) {
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
