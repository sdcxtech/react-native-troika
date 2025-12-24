package com.reactnative.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.UIManager;
import com.facebook.react.runtime.ReactSurfaceView;
import com.facebook.react.uimanager.TouchTargetHelper;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.common.UIManagerType;

public class OverlayRootView extends FrameLayout {
	private static final String TAG = "Overlay";

	public OverlayRootView(Context context) {
		super(context);
	}

	public OverlayRootView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OverlayRootView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private boolean passThroughTouches = false;

	public void setPassThroughTouches(boolean passThroughTouches) {
		this.passThroughTouches = passThroughTouches;
	}

	ReactSurfaceView reactSurfaceView;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// FLog.i(TAG, "dispatchTouchEvent");
		if (reactSurfaceView == null) {
			reactSurfaceView = findReactSurfaceView(this);
		}

		if (reactSurfaceView != null) {
			if (shouldPassTouches(ev)) return false;
		}

		return super.dispatchTouchEvent(ev);
	}

	private boolean shouldPassTouches(MotionEvent ev) {
		if (!passThroughTouches) {
			return false;
		}

		int action = ev.getAction() & MotionEvent.ACTION_MASK;
		if (action != MotionEvent.ACTION_DOWN) {
			return false;
		}

		int tag = TouchTargetHelper.findTargetTagForTouch(ev.getX(), ev.getY(), reactSurfaceView.getRootViewGroup());
		// FLog.i(TAG, "findTargetTagForTouch:" + tag);
		UIManager uiManager = UIManagerHelper.getUIManager(reactSurfaceView.getCurrentReactContext(), UIManagerType.FABRIC);
		if (uiManager == null) {
			return false;
		}

		View view = uiManager.resolveView(tag);
		if (view == null) {
			return false;
		}

		// FLog.i(TAG, "child name:%s", view.getClass().getSimpleName());
		// FLog.i(TAG, "target, width:%d, height:%d", view.getWidth(), view.getHeight());

		if (view == reactSurfaceView || view == reactSurfaceView.getChildAt(0)) {
			if (view.getWidth() == reactSurfaceView.getWidth() && view.getHeight() == reactSurfaceView.getHeight()) {
				reactSurfaceView.onChildStartedNativeGesture(reactSurfaceView, ev);
				return true;
			}
		}
		return false;
	}

	private ReactSurfaceView findReactSurfaceView(ViewGroup parent) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ReactSurfaceView) {
				return (ReactSurfaceView) child;
			}
			if (child instanceof ViewGroup) {
				ReactSurfaceView result = findReactSurfaceView((ViewGroup) child);
				if (result != null) return result;
			}
		}
		return null;
	}

}
