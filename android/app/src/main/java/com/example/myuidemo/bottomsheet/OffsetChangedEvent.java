package com.example.myuidemo.bottomsheet;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.Event;

public class OffsetChangedEvent extends Event<OffsetChangedEvent> {
    public static final String Name = "offsetEvent";
    public static final String JSEventName = "onSlide";

    private final int offset;
    private final int minOffset;
    private final int maxOffset;

    public OffsetChangedEvent(int surfaceId, int viewTag, int offset, int minOffset, int maxOffset) {
        super(surfaceId, viewTag);
        this.offset = offset;
        this.maxOffset = maxOffset;
        this.minOffset = minOffset;
    }

    @Override
    public String getEventName() {
        return Name;
    }

    @Nullable
    protected WritableMap getEventData() {
        double progress = Math.min((offset - minOffset) * 1.0f / (maxOffset - minOffset), 1);
        WritableMap data = Arguments.createMap();
        data.putDouble("progress", progress);
        data.putDouble("offset", PixelUtil.toDIPFromPixel(offset));
        data.putDouble("expandedOffset", PixelUtil.toDIPFromPixel(minOffset));
        data.putDouble("collapsedOffset", PixelUtil.toDIPFromPixel(maxOffset));
        return data;
    }
}
