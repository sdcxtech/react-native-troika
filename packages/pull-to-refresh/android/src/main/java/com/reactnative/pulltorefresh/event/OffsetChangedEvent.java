package com.reactnative.pulltorefresh.event;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.Event;

public class OffsetChangedEvent extends Event<OffsetChangedEvent> {

    public static final String Name = "offsetChangedEvent";
    public static final String JSEventName = "onOffsetChanged";

    private final float offset;

    public OffsetChangedEvent(int surfaceId, int viewTag, float offset) {
        super(surfaceId, viewTag);
        this.offset = PixelUtil.toDIPFromPixel(offset);
    }

    @Override
    public String getEventName() {
        return Name;
    }

    @Nullable
    protected WritableMap getEventData() {
        WritableMap map = Arguments.createMap();
        map.putDouble("offset", offset);
        return map;
    }

}
