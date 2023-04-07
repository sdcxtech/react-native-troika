package com.example.myuidemo;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class NestedViewHeaderScrollEvent extends Event<NestedViewHeaderScrollEvent> {
    public static final String Name = "scrollEvent";
    public static final String JSEventName = "onScroll";
    private int mScrollY;

    public NestedViewHeaderScrollEvent(int surfaceId, int viewTag, int scrollY) {
        super(surfaceId, viewTag);
        mScrollY = scrollY;
    }

    @Override
    public String getEventName() {
        return Name;
    }

    @Nullable
    protected WritableMap getEventData() {
        WritableMap event = Arguments.createMap();
        event.putInt("scrollY", mScrollY);
        return event;
    }
}
