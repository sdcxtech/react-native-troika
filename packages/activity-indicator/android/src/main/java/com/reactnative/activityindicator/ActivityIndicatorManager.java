package com.reactnative.activityindicator;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.ActivityIndicatorManagerDelegate;
import com.facebook.react.viewmanagers.ActivityIndicatorManagerInterface;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;

public class ActivityIndicatorManager extends SimpleViewManager<ActivityIndicator>
	implements ActivityIndicatorManagerInterface<ActivityIndicator> {

	public static final String REACT_CLASS = "ActivityIndicator";

	private final ActivityIndicatorManagerDelegate<ActivityIndicator, ActivityIndicatorManager> mDelegate
		= new ActivityIndicatorManagerDelegate<>(this);

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@Override
	protected ViewManagerDelegate<ActivityIndicator> getDelegate() {
		return mDelegate;
	}

	@NonNull
	@Override
	protected ActivityIndicator createViewInstance(@NonNull ThemedReactContext reactContext) {
		return new ActivityIndicator(reactContext);
	}

	@Override
	public void setAnimating(ActivityIndicator view, boolean animating) {
		view.setAnimating(animating);
	}

	@Override
	public void setColor(ActivityIndicator view, @Nullable Integer color) {
		if (color != null) {
			view.setColor(color);
		} else {
			view.setColor(Color.parseColor("#999999"));
		}
	}

	@Override
	public void setSize(ActivityIndicator view, @Nullable String size) {
		if (size != null && size.equals("large")) {
			view.setSize((int) (PixelUtil.toPixelFromDIP(36) + 0.5));
		} else {
			view.setSize((int) (PixelUtil.toPixelFromDIP(20) + 0.5));
		}
	}

	@Override
	public long measure(Context context, ReadableMap localData, ReadableMap props, ReadableMap state, float width, YogaMeasureMode widthMode, float height, YogaMeasureMode heightMode, @Nullable float[] attachmentsPositions) {
		String sizeStr = props.getString("size");
		if (sizeStr == null) {
			sizeStr = "small";
		}
		int size = sizeStr.equals("small") ? 20 : 36;
		return YogaMeasureOutput.make(size, size);
	}
}
