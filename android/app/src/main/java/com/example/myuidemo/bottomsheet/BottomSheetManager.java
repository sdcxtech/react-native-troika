package com.example.myuidemo.bottomsheet;

import androidx.annotation.NonNull;

import com.facebook.react.common.MapBuilder;
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

    @ReactProp(name = "peekHeight")
    public void setPeekHeight(BottomSheet view, int dp) {

    }

    @ReactProp(name = "expanded")
    public void setExpanded(BottomSheet view, boolean expanded) {

    }

}
