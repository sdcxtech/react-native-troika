package com.reactnative.pulltorefresh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.viewmanagers.PullToRefreshHeaderManagerDelegate;
import com.facebook.react.viewmanagers.PullToRefreshHeaderManagerInterface;
import com.reactnative.pulltorefresh.event.OffsetChangedEvent;
import com.reactnative.pulltorefresh.event.RefreshEvent;
import com.reactnative.pulltorefresh.event.StateChangedEvent;

import java.util.Map;


public class PullToRefreshHeaderManager extends ViewGroupManager<PullToRefreshHeader> implements PullToRefreshHeaderManagerInterface<PullToRefreshHeader> {
	public final static String REACT_CLASS = "PullToRefreshHeader";

	private final PullToRefreshHeaderManagerDelegate<PullToRefreshHeader, PullToRefreshHeaderManager> mDelegate = new PullToRefreshHeaderManagerDelegate<>(this);

	@Override
	protected ViewManagerDelegate<PullToRefreshHeader> getDelegate() {
		return mDelegate;
	}

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@NonNull
	@Override
	public PullToRefreshHeader createViewInstance(@NonNull ThemedReactContext context) {
		return new PullToRefreshHeader(context);
	}

	@ReactProp(name = "refreshing")
	public void setRefreshing(PullToRefreshHeader view, boolean refreshing) {
		view.setRefreshing(refreshing);
	}

	@Override
	public void setProgressViewOffset(PullToRefreshHeader view, float value) {
		view.setProgressViewOffset(value);
	}

	@Nullable
	@Override
	public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
		return MapBuilder.<String, Object>builder()
			.put(RefreshEvent.Name, MapBuilder.of("registrationName", RefreshEvent.JSEventName))
			.put(StateChangedEvent.Name, MapBuilder.of("registrationName", StateChangedEvent.JSEventName))
			.put(OffsetChangedEvent.Name, MapBuilder.of("registrationName", OffsetChangedEvent.JSEventName))
			.build();
	}

	@Override
	public void receiveCommand(@NonNull PullToRefreshHeader view, @NonNull String commandId, @Nullable ReadableArray args) {
		if ("setNativeRefreshing".equals(commandId)) {
			if (args != null && args.getType(0) == ReadableType.Boolean) {
				view.setRefreshing(args.getBoolean(0));
			}
		}
	}

	@Override
	protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull PullToRefreshHeader view) {
		super.addEventEmitters(reactContext, view);
		int surfaceId = UIManagerHelper.getSurfaceId(reactContext);
		int viewId = view.getId();
		view.setOnRefreshHeaderChangeListener(new OnRefreshChangeListener() {
			@Override
			public void onRefresh() {
				if (reactContext.hasActiveReactInstance()) {
					EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
					if (eventDispatcher != null) {
						eventDispatcher.dispatchEvent(new RefreshEvent(surfaceId, viewId));
					}
				}
			}

			@Override
			public void onOffsetChange(int offset) {
				if (reactContext.hasActiveReactInstance()) {
					EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
					if (eventDispatcher != null) {
						eventDispatcher.dispatchEvent(new OffsetChangedEvent(surfaceId, viewId, offset));
					}
				}
			}

			@Override
			public void onStateChanged(PullToRefreshState state) {
				if (reactContext.hasActiveReactInstance()) {
					EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, viewId);
					if (eventDispatcher != null) {
						eventDispatcher.dispatchEvent(new StateChangedEvent(surfaceId, viewId, state));
					}
				}
			}
		});
	}

}
