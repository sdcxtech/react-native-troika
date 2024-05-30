package com.reactnative.bottomsheet;

import androidx.annotation.NonNull;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class BottomSheetManager extends ViewGroupManager<BottomSheet> {

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
                .put(StateChangedEvent.Name, MapBuilder.of("registrationName", StateChangedEvent.JSEventName))
                .put(OffsetChangedEvent.Name, MapBuilder.of("registrationName", OffsetChangedEvent.JSEventName))
                .build();
    }

    @ReactProp(name = "peekHeight", defaultInt = 200)
    public void setPeekHeight(BottomSheet view, int dp) {
        view.setPeekHeight((int) (PixelUtil.toPixelFromDIP(dp) + 0.5));
    }

    @ReactProp(name = "state")
    public void setState(BottomSheet view, String state) {
        view.setState(BottomSheetState.valueOf(state.toUpperCase()));
    }

    @ReactProp(name = "draggable")
    public void setDraggable(BottomSheet view, boolean draggable) {
        view.setDraggable(draggable);
    }

}
