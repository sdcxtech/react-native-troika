package com.reactnative.overlay;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.facebook.react.ReactHost;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.interfaces.fabric.ReactSurface;

@UiThread
public class Overlay {

	final Activity activity;
	final String moduleName;
	final ReactHost reactHost;

	ViewGroup rootView;
	ViewGroup decorView;
	ReactSurface reactSurface;

	public Overlay(@NonNull Activity activity, String moduleName, ReactHost reactHost) {
		this.activity = activity;
		this.moduleName = moduleName;
		this.reactHost = reactHost;
	}

	public void show(ReadableMap props, ReadableMap options) {

		decorView = getDecorView();
		if (decorView == null) {
			return;
		}

		ReactSurface reactSurface = reactHost.createSurface(activity, moduleName, Arguments.toBundle(props));
		this.reactSurface = reactSurface;
		ViewGroup view = reactSurface.getView();
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
		OverlayRootView root = new OverlayRootView(activity);
		root.addView(view, layoutParams);
		this.rootView = root;
		decorView.addView(root, layoutParams);
		if (options.hasKey("passThroughTouches")) {
			root.setPassThroughTouches(options.getBoolean("passThroughTouches"));
        }
		reactSurface.start();
	}

	public void hide() {
		if (decorView != null) {
			decorView.removeView(rootView);
			decorView = null;
		}
		unmountReactView();
	}

	public void update() {
		ViewGroup decorView = getDecorView();
		if (decorView != null && decorView != this.decorView) {
			this.decorView.removeView(rootView);
			this.decorView = decorView;
			decorView.addView(rootView);
		}
	}

	private void unmountReactView() {
		if (reactSurface != null) {
			reactSurface.stop();
		}
	}

	private ViewGroup getDecorView() {
		Window window = activity.getWindow();
		if (window == null) {
			return null;
		}
		return (ViewGroup) window.getDecorView();
	}

}
