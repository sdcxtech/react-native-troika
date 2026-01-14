package com.reactnative.imagecrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.ImageCropViewManagerDelegate;
import com.facebook.react.viewmanagers.ImageCropViewManagerInterface;

import java.util.Map;

public class ImageCropViewManager extends ViewGroupManager<ImageCropView>
	implements ImageCropViewManagerInterface<ImageCropView> {
	private final ImageCropViewManagerDelegate<ImageCropView, ImageCropViewManager> mDelegate = new ImageCropViewManagerDelegate<>(this);
	private static final String REACT_CLASS = "ImageCropView";

	@Override
	protected ViewManagerDelegate<ImageCropView> getDelegate() {
		return mDelegate;
	}

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@NonNull
	@Override
	protected ImageCropView createViewInstance(@NonNull ThemedReactContext reactContext) {
		return new ImageCropView(reactContext);
	}

	@Override
	public void setFileUri(ImageCropView view, @Nullable String uri) {
		view.setFileUri(uri);
	}

	@Override
	public void setCropStyle(ImageCropView view, @Nullable String style) {
		view.setCropStyle(style);
	}

	@Override
	public void setObjectRect(ImageCropView view, @Nullable ReadableMap value) {
		view.setObjectRect(value);
	}

	@Override
	public void crop(ImageCropView view) {
		view.crop();
	}

	@Override
	protected void onAfterUpdateTransaction(@NonNull ImageCropView view) {
		super.onAfterUpdateTransaction(view);
		view.initProperties();
	}

	@Nullable
	@Override
	public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
		return MapBuilder.of(
			OnCropEvent.Name, MapBuilder.of("registrationName", OnCropEvent.JSEventName)
		);
	}

	@Override
	protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull ImageCropView view) {
		super.addEventEmitters(reactContext, view);
	}
}
