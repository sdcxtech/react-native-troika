package com.reactnative.overlay;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.common.logging.FLog;
import com.facebook.react.ReactHost;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.defaults.DefaultReactHost;
import com.facebook.react.uimanager.PixelUtil;

import java.util.HashMap;

public class OverlayModule extends NativeOverlaySpec implements LifecycleEventListener {

	private final HashMap<String, Overlay> overlays = new HashMap<>();
	private final ReactApplicationContext reactContext;
	private final ReactHost reactHost;

	public OverlayModule(ReactApplicationContext reactContext, ReactNativeHost reactNativeHost) {
		super(reactContext);
		this.reactContext = reactContext;
		this.reactHost = DefaultReactHost.getDefaultReactHost(reactContext, reactNativeHost, null);
		reactContext.addLifecycleEventListener(this);
	}

	@NonNull
	@Override
	public String getName() {
		return "OverlayHost";
	}

	@Override
	public void invalidate() {
		reactContext.removeLifecycleEventListener(this);
		final Activity activity = getCurrentActivity();
		if (activity == null || activity.isFinishing()) {
			return;
		}
		UiThreadUtil.runOnUiThread(this::handleDestroy);
	}

	private void handleDestroy() {
		for (String key : overlays.keySet()) {
			Overlay overlay = overlays.get(key);
			overlay.hide();
		}
		overlays.clear();
	}

	public void show(final String moduleName, final ReadableMap options) {
		UiThreadUtil.runOnUiThread(() -> {
			final Activity activity = getCurrentActivity();
			if (activity == null || activity.isFinishing()) {
				return;
			}

			Overlay overlay = overlays.get(moduleName);
			if (overlay != null) {
				overlay.update();
				return;
			}

			int id = (int) options.getDouble("overlayId");
			WritableMap props = JavaOnlyMap.deepClone(options);
			props.putMap("insets", getInsets(activity));
			overlay = new Overlay(activity, moduleName, reactHost);
			overlay.show(props, options);
			overlays.put(genOverlayKey(moduleName, id), overlay);
		});
	}

	@Override
	public void hide(String moduleName, double overlayId) {
		hide(moduleName, (int) overlayId);
	}

	public void hide(String moduleName, int id) {
		UiThreadUtil.runOnUiThread(() -> {
			Overlay overlay = overlays.get(genOverlayKey(moduleName, id));
			if (overlay == null) {
				return;
			}
			overlays.remove(genOverlayKey(moduleName, id));
			overlay.hide();
		});
	}

	@Override
	public void onHostResume() {
		//
	}

	@Override
	public void onHostPause() {
		//
	}

	@Override
	public void onHostDestroy() {
		FLog.i("OverlayModule", "onHostDestroy");
		handleDestroy();
	}

	private String genOverlayKey(String moduleName, int id) {
		return moduleName + "-" + id;
	}

	private ReadableMap getInsets(Activity activity) {
		WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(activity.getWindow().getDecorView());
		assert windowInsets != null;
		Insets navigationBarInsets = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars());
		Insets statusBarInsets = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars());
		Insets displayCutoutInsets = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.displayCutout());
		WritableMap insets = new JavaOnlyMap();
		insets.putDouble("left", PixelUtil.toDIPFromPixel(Math.max(navigationBarInsets.left, displayCutoutInsets.left)));
		insets.putDouble("top", PixelUtil.toDIPFromPixel(statusBarInsets.top));
		insets.putDouble("right", PixelUtil.toDIPFromPixel(Math.max(navigationBarInsets.right, displayCutoutInsets.right)));
		insets.putDouble("bottom", PixelUtil.toDIPFromPixel(navigationBarInsets.bottom));
		return insets;
	}
}
