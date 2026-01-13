package com.reactnative.nestedscroll;

import static com.facebook.react.uimanager.PixelUtil.toDIPFromPixel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.MeasureSpecAssertions;
import com.facebook.react.uimanager.ReactOverflowView;
import com.facebook.react.uimanager.StateWrapper;
import com.facebook.react.uimanager.events.NativeGestureUtil;

public class NestedScrollView extends androidx.core.widget.NestedScrollView implements ReactOverflowView {
	private static final String TAG = "NestedScrollView";
	private final NestedScrollViewLocalData mNestedScrollViewLocalData = new NestedScrollViewLocalData();
	private String mOverflow = "hidden";
	private final Rect mRect;

	private final NestedScrollFlingHelper mFlingHelper;

	private StateWrapper mStateWrapper;

	public NestedScrollView(@NonNull Context context) {
		super(context);
		mRect = new Rect();
		mFlingHelper = new NestedScrollFlingHelper(this);
		setOverScrollMode(OVER_SCROLL_NEVER);
	}

	public void setStateWrapper(StateWrapper wrapper) {
		mStateWrapper = wrapper;
	}

	public void setOverflow(String overflow) {
		mOverflow = overflow;
		invalidate();
	}

	@Nullable
	@Override
	public String getOverflow() {
		return mOverflow;
	}

	@Override
	public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
		super.onNestedPreScroll(target, dx, dy, consumed, type);
		int dyUnconsumed = dy - consumed[1];
		if (dyUnconsumed > 0) {
			final int oldScrollY = getScrollY();
			scrollBy(0, dyUnconsumed);
			final int myConsumed = getScrollY() - oldScrollY;
			consumed[1] += myConsumed;
		}
	}

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mStateWrapper != null) {
            ReadableMap currentState = mStateWrapper.getStateData();
            double currentOffsetY = 0;
            if (currentState != null && currentState.hasKey("contentOffsetY")) {
                currentOffsetY = currentState.getDouble("contentOffsetY");
            }
            final int newOffsetY = (int) toDIPFromPixel(getScrollY());

            if (Math.abs(currentOffsetY - newOffsetY) > 0.1) {
                WritableMap map = Arguments.createMap();
                FLog.i(TAG, "updateState contentOffsetY : " + newOffsetY);
                map.putDouble("contentOffsetY", newOffsetY);
                mStateWrapper.updateState(map);
            }
        }
    }

    @Override
	public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
		boolean consumed = super.onNestedPreFling(target, velocityX, velocityY);
		if (!consumed) {
			consumed = mFlingHelper.onNestedPreFling(target, velocityY);
		}
		return consumed;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		mFlingHelper.computeScroll();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mFlingHelper.dispatchTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
		if (super.onInterceptTouchEvent(ev)) {
			NativeGestureUtil.notifyNativeGestureStarted(this, ev);
			return true;
		}
		return false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		MeasureSpecAssertions.assertExplicitMeasureSpec(widthMeasureSpec, heightMeasureSpec);
		this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}

	void notifyStickyHeightChanged() {
		if (isLaidOut() && !isInLayout()) {
			fitStickyHeightIfNeeded();
		}
	}

	private void fitStickyHeightIfNeeded() {
		Context context = getContext();
		if (context instanceof ReactContext) {
			ViewGroup content = (ViewGroup) getChildAt(0);
			if (content == null) {
				return;
			}

			int headerFixedHeight = 0;
			float headerHeight = 0;
			for (int i = 0; i < content.getChildCount(); i++) {
				View child = content.getChildAt(i);
				if (child instanceof NestedScrollViewHeader) {
					headerFixedHeight = ((NestedScrollViewHeader) child).getStickyHeight();
					headerHeight = child.getHeight();
				}
			}

			int nestedScrollViewH = getHeight();
			float contentHeight = nestedScrollViewH - headerFixedHeight;
			if (contentHeight != mNestedScrollViewLocalData.contentNodeH || headerHeight != mNestedScrollViewLocalData.headerNodeH) {
				mNestedScrollViewLocalData.contentNodeH = contentHeight;
				mNestedScrollViewLocalData.headerNodeH = headerHeight;
				if (mStateWrapper != null) {
					WritableMap map = Arguments.createMap();
					map.putDouble("contentHeight", toDIPFromPixel(contentHeight));
					map.putDouble("headerHeight", toDIPFromPixel(headerHeight));
					FLog.i(TAG, "contentHeight:" + toDIPFromPixel(contentHeight) + " headerHeight:" + toDIPFromPixel(headerHeight));
					mStateWrapper.updateState(map);
				}
			}

			int maxScrollRange = (int) (headerHeight - headerFixedHeight);
			if (getScrollY() > maxScrollRange) {
				scrollTo(0, maxScrollRange);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		fitStickyHeightIfNeeded();
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		getDrawingRect(mRect);
		if (!"visible".equals(mOverflow)) {
			canvas.clipRect(mRect);
		}
		super.draw(canvas);
	}
}
