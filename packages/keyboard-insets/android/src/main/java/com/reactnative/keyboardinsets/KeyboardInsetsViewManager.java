package com.reactnative.keyboardinsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.KeyboardInsetsViewManagerDelegate;
import com.facebook.react.viewmanagers.KeyboardInsetsViewManagerInterface;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

import java.util.Map;

public class KeyboardInsetsViewManager extends ReactViewManager
	implements KeyboardInsetsViewManagerInterface<KeyboardInsetsView> {

    public static final String REACT_CLASS = "KeyboardInsetsView";

	private final KeyboardInsetsViewManagerDelegate mDelegate = new KeyboardInsetsViewManagerDelegate(this);

	@Override
	protected ViewManagerDelegate<ReactViewGroup> getDelegate() {
		return mDelegate;
	}

	@NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    public ReactViewGroup createViewInstance(@NonNull ThemedReactContext context) {
        KeyboardInsetsView view = new KeyboardInsetsView(context);
        KeyboardInsetsCallback callback = new KeyboardInsetsCallback(view, context);
        ViewCompat.setWindowInsetsAnimationCallback(view, callback);
        ViewCompat.setOnApplyWindowInsetsListener(view, callback);
        return view;
    }

	@Override
	public void setMode(KeyboardInsetsView view, @Nullable String mode) {
		view.setMode(mode);
	}

	@Override
	public void setExtraHeight(KeyboardInsetsView view, float extraHeight) {
		view.setExtraHeight(extraHeight);
	}

	@Override
	public void setExplicitly(KeyboardInsetsView view, boolean value) {

	}

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
            "topStatusChanged", MapBuilder.of("registrationName", "onStatusChanged"),
            "topPositionChanged", MapBuilder.of("registrationName", "onPositionChanged")
        );
    }

}
