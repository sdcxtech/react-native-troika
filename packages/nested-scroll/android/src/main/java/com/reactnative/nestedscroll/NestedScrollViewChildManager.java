package com.reactnative.nestedscroll;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.NestedScrollViewChildManagerDelegate;
import com.facebook.react.viewmanagers.NestedScrollViewChildManagerInterface;

public class NestedScrollViewChildManager extends ViewGroupManager<NestedScrollViewChild>
	implements NestedScrollViewChildManagerInterface<NestedScrollViewChild> {

	public final static String REACT_CLASS = "NestedScrollViewChild";

	private final NestedScrollViewChildManagerDelegate<NestedScrollViewChild, NestedScrollViewChildManager> mDelegate
		= new NestedScrollViewChildManagerDelegate<>(this);

	@Override
	protected ViewManagerDelegate<NestedScrollViewChild> getDelegate() {
		return mDelegate;
	}

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@NonNull
	@Override
	protected NestedScrollViewChild createViewInstance(@NonNull ThemedReactContext reactContext) {
		return new NestedScrollViewChild(reactContext);
	}
}
