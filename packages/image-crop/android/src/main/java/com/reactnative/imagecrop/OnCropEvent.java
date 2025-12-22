package com.reactnative.imagecrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class OnCropEvent extends Event<OnCropEvent> {
	public static final String Name = "topCrop";
	public static final String JSEventName = "onCrop";

	private final String uri;

	public OnCropEvent(int surfaceId, int viewTag, String uri) {
		super(surfaceId, viewTag);
		this.uri = uri;
	}

	@NonNull
	@Override
	public String getEventName() {
		return Name;
	}

	@Nullable
	@Override
	protected WritableMap getEventData() {
		WritableMap event = Arguments.createMap();
		event.putString("uri", uri);
		return event;
	}
}
