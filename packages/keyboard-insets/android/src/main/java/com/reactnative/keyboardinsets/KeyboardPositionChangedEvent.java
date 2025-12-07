package com.reactnative.keyboardinsets;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class KeyboardPositionChangedEvent extends Event<KeyboardPositionChangedEvent> {

    private final int position;

    public KeyboardPositionChangedEvent(int viewId, int position) {
        super(viewId);
        this.position = position;
    }

	@NonNull
    @Override
    public String getEventName() {
        return "topPositionChanged";
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        WritableMap map = Arguments.createMap();
        map.putDouble("position", PixelUtil.toDIPFromPixel(position));
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), map);
    }
}
