package com.reactnative.bottomsheet;

import androidx.annotation.NonNull;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.viewmanagers.BottomSheetManagerInterface;

import java.util.Map;

public class BottomSheetManager extends ViewGroupManager<BottomSheet>
	implements BottomSheetManagerInterface<BottomSheet> {

    @NonNull
    @Override
    public String getName() {
        return "BottomSheet";
    }

    @NonNull
    @Override
    protected BottomSheet createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new BottomSheet(reactContext);
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(OnStateChangedEvent.Name, MapBuilder.of("registrationName", OnStateChangedEvent.JSEventName))
                .put(OnSlideEvent.Name, MapBuilder.of("registrationName", OnSlideEvent.JSEventName))
                .build();
    }

	@Override
    @ReactProp(name = "peekHeight", defaultInt = 200)
    public void setPeekHeight(BottomSheet view, int dp) {
        view.setPeekHeight((int) (PixelUtil.toPixelFromDIP(dp) + 0.5));
    }

	@Override
    @ReactProp(name = "status")
    public void setStatus(BottomSheet view, String status) {
		assert status != null;
        view.setStatus(BottomSheetStatus.valueOf(status.toUpperCase()));
    }

	@Override
    @ReactProp(name = "draggable")
    public void setDraggable(BottomSheet view, boolean draggable) {
        view.setDraggable(draggable);
    }

}
