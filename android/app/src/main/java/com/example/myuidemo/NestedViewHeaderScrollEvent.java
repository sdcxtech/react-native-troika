package com.example.myuidemo;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.Event;

public class NestedViewHeaderScrollEvent extends Event<NestedViewHeaderScrollEvent> {
    public static final String Name = "scrollEvent";
    public static final String JSEventName = "onScroll";
    private final float y;

    public NestedViewHeaderScrollEvent(int surfaceId, int viewTag, int offset) {
        super(surfaceId, viewTag);
        this.y = PixelUtil.toDIPFromPixel(offset);
    }

    @Override
    public String getEventName() {
        return Name;
    }

    @Nullable
    protected WritableMap getEventData() {
        WritableMap event = Arguments.createMap();
        WritableMap offset = Arguments.createMap();
        offset.putDouble("y", y);
        offset.putDouble("x", 0);
        event.putMap("contentOffset", offset);
        return event;
    }
}
