package com.reactnative.keyboardinsets;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class KeyboardStatusChangedEvent extends Event<KeyboardStatusChangedEvent> {

    private final int height;
    private final boolean shown;
    private final boolean transitioning;

    public KeyboardStatusChangedEvent(int viewTag, int height, boolean shown, boolean transitioning) {
        super(viewTag);
        this.height = height;
        this.shown = shown;
        this.transitioning = transitioning;
    }

	@NonNull
    @Override
    public String getEventName() {
        return "topStatusChanged";
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        WritableMap map = Arguments.createMap();
        map.putDouble("height", PixelUtil.toDIPFromPixel(height));
        map.putBoolean("transitioning", transitioning);
        map.putBoolean("shown", shown);
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), map);
    }

}
