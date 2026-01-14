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
import com.facebook.react.viewmanagers.PullToRefreshFooterManagerDelegate;
import com.facebook.react.viewmanagers.PullToRefreshFooterManagerInterface;
import com.reactnative.pulltorefresh.event.OffsetChangedEvent;
import com.reactnative.pulltorefresh.event.RefreshEvent;
import com.reactnative.pulltorefresh.event.StateChangedEvent;

import java.util.Map;

public class PullToRefreshFooterManager extends ViewGroupManager<PullToRefreshFooter> implements PullToRefreshFooterManagerInterface<PullToRefreshFooter> {
	public final static String REACT_CLASS = "PullToRefreshFooter";

	private final PullToRefreshFooterManagerDelegate<PullToRefreshFooter, PullToRefreshFooterManager> mDelegate = new PullToRefreshFooterManagerDelegate<>(this);

	@Override
	protected ViewManagerDelegate<PullToRefreshFooter> getDelegate() {
		return mDelegate;
	}

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@NonNull
	@Override
	public PullToRefreshFooter createViewInstance(@NonNull ThemedReactContext context) {
		return new PullToRefreshFooter(context);
	}

	@ReactProp(name = "refreshing")
	public void setRefreshing(PullToRefreshFooter footer, boolean refreshing) {
		footer.setLoadingMore(refreshing);
	}

	@ReactProp(name = "noMoreData")
	public void setNoMoreData(PullToRefreshFooter footer, boolean noMoreData) {
		footer.setNoMoreData(noMoreData);
	}

	@ReactProp(name = "manual")
	public void setManual(PullToRefreshFooter footer, boolean manual) {
		footer.setAutoLoadMore(!manual);
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
	public void receiveCommand(@NonNull PullToRefreshFooter view, String commandId, @Nullable ReadableArray args) {
		if ("setNativeRefreshing".equals(commandId)) {
			if (args != null && args.getType(0) == ReadableType.Boolean) {
				view.setLoadingMore(args.getBoolean(0));
			}
		}
	}

	@Override
	protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull PullToRefreshFooter view) {
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
