package com.reactnative.nestedscroll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

import java.util.Map;

public class NestedScrollViewHeaderManager extends ReactViewManager {
    public final static String REACT_CLASS = "NestedScrollViewHeader";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    public ReactViewGroup createViewInstance(ThemedReactContext context) {
        return new NestedScrollViewHeader(context);
    }

    @ReactProp(name = "fixedHeight", defaultInt = NestedScrollViewHeader.INVALID_FIXED_HEIGHT)
    public void setFixedHeight(NestedScrollViewHeader view, int fixedHeight) {
        view.setFixedHeight((int) PixelUtil.toPixelFromDIP(fixedHeight));
    }

    @ReactProp(name = "stickyHeaderBeginIndex", defaultInt = NestedScrollViewHeader.INVALID_STICKY_BEGIN_INDEX)
    public void setStickyHeaderBeginIndex(NestedScrollViewHeader view, int stickyHeaderBeginIndex) {
        view.setStickyHeaderBeginIndex(stickyHeaderBeginIndex);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(NestedViewHeaderScrollEvent.Name, MapBuilder.of("registrationName", NestedViewHeaderScrollEvent.JSEventName))
                .build();
    }

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull ReactViewGroup view) {
        super.addEventEmitters(reactContext, view);
        if (view instanceof NestedScrollViewHeader) {
            ((NestedScrollViewHeader) view).setOnScrollChangeListener(
                    (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
                        int viewId = view.getId();
                        EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
                        if (eventDispatcher != null) {
                            NestedViewHeaderScrollEvent nestedViewHeaderScrollEvent = new NestedViewHeaderScrollEvent(surfaceId, viewId, scrollY);
                            eventDispatcher.dispatchEvent(nestedViewHeaderScrollEvent);
                        }
                    }
            );
        }
    }
}
