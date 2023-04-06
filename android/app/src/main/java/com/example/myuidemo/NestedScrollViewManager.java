package com.example.myuidemo;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.scroll.ScrollEvent;
import com.facebook.react.views.scroll.ScrollEventType;

import java.util.Map;

public class NestedScrollViewManager extends ViewGroupManager<NestedScrollView> {

    @NonNull
    @Override
    public String getName() {
        return "NestedScrollView";
    }

    @NonNull
    @Override
    protected NestedScrollView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new NestedScrollView(reactContext);
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new NestedScrollViewShadowNode();
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return NestedScrollViewShadowNode.class;
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(ScrollEventType.getJSEventName(ScrollEventType.SCROLL), MapBuilder.of("registrationName", "onScroll"))
                .build();
    }

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull NestedScrollView scrollView) {
        super.addEventEmitters(reactContext, scrollView);
        scrollView.setOnScrollChangeListener((androidx.core.widget.NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            View contentView = scrollView.getChildAt(0);
            if (contentView != null) {
                int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
                EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, scrollView.getId());
                if (eventDispatcher != null) {
                    ScrollEvent scrollEvent = ScrollEvent.obtain(
                            surfaceId, scrollView.getId(),
                            ScrollEventType.SCROLL,
                            scrollX, scrollY,
                            0, 0,
                            contentView.getWidth(), contentView.getHeight(),
                            scrollView.getWidth(), scrollView.getHeight());
                    eventDispatcher.dispatchEvent(scrollEvent);
                }
            }
        });
    }
}
