package com.reactnative.nestedscroll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.viewmanagers.NestedScrollViewHeaderManagerDelegate;
import com.facebook.react.viewmanagers.NestedScrollViewHeaderManagerInterface;

import java.util.Map;

public class NestedScrollViewHeaderManager extends ViewGroupManager<NestedScrollViewHeader>
	implements NestedScrollViewHeaderManagerInterface<NestedScrollViewHeader> {
	public final static String REACT_CLASS = "NestedScrollViewHeader";

	private final NestedScrollViewHeaderManagerDelegate<NestedScrollViewHeader, NestedScrollViewHeaderManager> mDelegate
		= new NestedScrollViewHeaderManagerDelegate<>(this);

	@Override
	protected ViewManagerDelegate<NestedScrollViewHeader> getDelegate() {
		return mDelegate;
	}

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@NonNull
	@Override
	public NestedScrollViewHeader createViewInstance(@NonNull ThemedReactContext context) {
		return new NestedScrollViewHeader(context);
	}

	@ReactProp(name = "stickyHeaderHeight", defaultInt = NestedScrollViewHeader.INVALID_STICKY_HEIGHT)
	@Override
	public void setStickyHeaderHeight(NestedScrollViewHeader view, float stickyHeight) {
		view.setStickyHeight((int) PixelUtil.toPixelFromDIP(stickyHeight));
	}

	@ReactProp(name = "stickyHeaderBeginIndex", defaultInt = NestedScrollViewHeader.INVALID_STICKY_BEGIN_INDEX)
	public void setStickyHeaderBeginIndex(NestedScrollViewHeader view, int stickyHeaderBeginIndex) {
		view.setStickyHeaderBeginIndex(stickyHeaderBeginIndex);
	}

	@Nullable
	@Override
	public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
		return MapBuilder.of(
			NestedViewHeaderScrollEvent.Name, MapBuilder.of("registrationName", NestedViewHeaderScrollEvent.JSEventName)
		);
	}

	@Override
	protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull NestedScrollViewHeader view) {
		super.addEventEmitters(reactContext, view);
		view.setOnScrollChangeListener(
			(NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
				int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
				int viewId = view.getId();
				EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
				if (eventDispatcher != null) {
					NestedViewHeaderScrollEvent headerScrollEvent = new NestedViewHeaderScrollEvent(surfaceId, viewId, scrollY);
					eventDispatcher.dispatchEvent(headerScrollEvent);
				}
			}
		);
	}
}
