package com.reactnative.wheelpicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

public class OnItemSelectedEvent extends Event<OnItemSelectedEvent> {
	public static final String Name = "topItemSelected";
	public static final String JSEventName = "onItemSelected";

	private final int selectedIndex;

	public OnItemSelectedEvent(int surfaceId, int viewTag, int selectedIndex) {
		super(surfaceId, viewTag);
		this.selectedIndex = selectedIndex;
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
		event.putInt("selectedIndex", selectedIndex);
		return event;
	}
}
