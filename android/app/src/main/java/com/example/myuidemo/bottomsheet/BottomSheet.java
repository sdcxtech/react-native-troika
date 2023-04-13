package com.example.myuidemo.bottomsheet;

import static com.example.myuidemo.bottomsheet.BottomSheetState.COLLAPSED;
import static com.example.myuidemo.bottomsheet.BottomSheetState.DRAGGING;
import static com.example.myuidemo.bottomsheet.BottomSheetState.EXPANDED;
import static com.example.myuidemo.bottomsheet.BottomSheetState.SETTLING;
import static java.lang.Math.max;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.util.Pools;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.view.ReactViewGroup;

import java.lang.ref.WeakReference;

@SuppressLint("ViewConstructor")
public class BottomSheet extends ReactViewGroup {

    private final ReactContext reactContext;

    public BottomSheet(ThemedReactContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.peekHeight = (int) PixelUtil.toPixelFromDIP(200);
    }

    private BottomSheetState state = COLLAPSED;

    private SettleRunnable settleRunnable = null;

    private int peekHeight;

    private int expandedOffset;

    private int collapsedOffset;

    @Nullable
    private WeakReference<View> viewRef;

    @Nullable
    private WeakReference<View> nestedScrollingChildRef;

    private boolean touchingScrollingChild;

    @Nullable
    private ViewDragHelper viewDragHelper;

    private boolean ignoreEvents;

    private VelocityTracker velocityTracker;

    private int activePointerId;

    private int initialY;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(this, dragCallback);
        }

        calculateOffset();
        layoutChild();
    }

    private void layoutChild() {
        int count = getChildCount();
        if (count == 1) {
            View child = getChildAt(0);
            if (viewRef == null) {
                viewRef = new WeakReference<>(child);
            }
            child.offsetTopAndBottom(collapsedOffset);
        }
    }

    public void setPeekHeight(int peekHeight) {
        this.peekHeight = max(peekHeight, 0);
        if (viewRef != null) {
            calculateOffset();
            if (state == COLLAPSED) {
                View view = viewRef.get();
                if (view != null) {
                    settleToState(view, state);
                }
            }
        }
    }

    private void calculateOffset() {
        this.collapsedOffset = getHeight() - this.peekHeight;
        this.expandedOffset = 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        // Record the velocity
        if (action == MotionEvent.ACTION_DOWN) {
            reset();
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchingScrollingChild = false;
                activePointerId = MotionEvent.INVALID_POINTER_ID;
                // Reset the ignore flag
                if (ignoreEvents) {
                    ignoreEvents = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                int initialX = (int) event.getX();
                initialY = (int) event.getY();
                // Only intercept nested scrolling events here if the view not being moved by the
                // ViewDragHelper.
                if (state != SETTLING) {
                    View scroll = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
                    if (scroll != null && isPointInChildBounds(scroll, initialX, initialY)) {
                        activePointerId = event.getPointerId(event.getActionIndex());
                        touchingScrollingChild = true;
                    }
                }
                View child = getChildAt(0);
                ignoreEvents =
                        activePointerId == MotionEvent.INVALID_POINTER_ID
                                && !isPointInChildBounds(child, initialX, initialY);
                break;
            default: // fall out
        }

        if (!ignoreEvents && viewDragHelper != null && viewDragHelper.shouldInterceptTouchEvent(event)) {
            return true;
        }

        // We have to handle cases that the ViewDragHelper does not capture the bottom sheet because
        // it is not the top most view of its parent. This is not necessary when the touch event is
        // happening over the scrolling content as nested scrolling logic handles that case.
        View scroll = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
        return action == MotionEvent.ACTION_MOVE
                && scroll != null
                && !ignoreEvents
                && state != DRAGGING
                && !isPointInChildBounds(scroll, (int) event.getX(), (int) event.getY())
                && viewDragHelper != null
                && Math.abs(initialY - event.getY()) > viewDragHelper.getTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (state == DRAGGING && action == MotionEvent.ACTION_DOWN) {
            return true;
        }
        if (viewDragHelper != null) {
            viewDragHelper.processTouchEvent(event);
        }
        // Record the velocity
        if (action == MotionEvent.ACTION_DOWN) {
            reset();
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        if (viewDragHelper != null && action == MotionEvent.ACTION_MOVE && !ignoreEvents) {
            if (Math.abs(initialY - event.getY()) > viewDragHelper.getTouchSlop()) {
                viewDragHelper.captureChildView(getChildAt(0), event.getPointerId(event.getActionIndex()));
            }
        }

        return !ignoreEvents;
    }

    private void reset() {
        activePointerId = ViewDragHelper.INVALID_POINTER;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private static final Pools.Pool<Rect> sRectPool = new Pools.SynchronizedPool<>(12);

    private static Rect acquireTempRect() {
        Rect rect = sRectPool.acquire();
        if (rect == null) {
            rect = new Rect();
        }
        return rect;
    }

    private static void releaseTempRect(@NonNull Rect rect) {
        rect.setEmpty();
        sRectPool.release(rect);
    }

    public boolean isPointInChildBounds(@NonNull View child, int x, int y) {
        final Rect r = acquireTempRect();
        child.getDrawingRect(r);
        offsetDescendantRectToMyCoords(child, r);
        try {
            return r.contains(x, y);
        } finally {
            releaseTempRect(r);
        }
    }

    private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            if (state == DRAGGING) {
                return false;
            }
            if (touchingScrollingChild) {
                return false;
            }
            if (state == EXPANDED && activePointerId == pointerId) {
                View scroll = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
                if (scroll != null && scroll.canScrollVertically(-1)) {
                    // Let the content scroll up
                    return false;
                }
            }
            return viewRef != null && viewRef.get() == child;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            dispatchOnSlide(top);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (state == ViewDragHelper.STATE_DRAGGING) {
                setStateInternal(DRAGGING);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int top;
            BottomSheetState targetState;
            if (yvel < 0) { // Moving up
                top = expandedOffset;
                targetState = EXPANDED;
            } else if (yvel == 0.f || Math.abs(xvel) > Math.abs(yvel)) {
                // If the Y velocity is 0 or the swipe was mostly horizontal indicated by the X velocity
                // being greater than the Y velocity, settle to the nearest correct height.
                int currentTop = releasedChild.getTop();
                if (Math.abs(currentTop - collapsedOffset) < Math.abs(currentTop - expandedOffset)) {
                    top = collapsedOffset;
                    targetState = COLLAPSED;
                } else {
                    top = expandedOffset;
                    targetState = EXPANDED;
                }
            } else { // Moving Down
                top = collapsedOffset;
                targetState = COLLAPSED;
            }
            startSettlingAnimation(releasedChild, targetState, top, true);
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return MathUtils.clamp(top, expandedOffset, collapsedOffset);
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return child.getLeft();
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return collapsedOffset;
        }
    };

    void dispatchOnSlide(int top) {
        // TODO: onOffsetChanged
    }

    public void setState(BottomSheetState state) {
        if (state == this.state) {
            return;
        }
        if (isLaidOut()) {
            // settleToState();
        } else {
            final BottomSheetState finalState = state;
        }
    }

    void settleToState(@NonNull View child, BottomSheetState state) {
        int top;
        if (state == COLLAPSED) {
            top = collapsedOffset;
        } else if (state == EXPANDED) {
            top = expandedOffset;
        } else {
            throw new IllegalArgumentException("Illegal state argument: " + state);
        }
        startSettlingAnimation(child, state, top, false);
    }

    void startSettlingAnimation(View child, BottomSheetState state, int top, boolean settleFromViewDragHelper) {
        boolean startedSettling =
                viewDragHelper != null
                        && (settleFromViewDragHelper
                        ? viewDragHelper.settleCapturedViewAt(child.getLeft(), top)
                        : viewDragHelper.smoothSlideViewTo(child, child.getLeft(), top));
        if (startedSettling) {
            setStateInternal(SETTLING);
            // STATE_SETTLING won't animate the material shape, so do that here with the target state.
            // updateDrawableForTargetState(state);
            if (settleRunnable == null) {
                // If the singleton SettleRunnable instance has not been instantiated, create it.
                settleRunnable = new SettleRunnable(child, state);
            }
            // If the SettleRunnable has not been posted, post it with the correct state.
            if (!settleRunnable.isPosted) {
                settleRunnable.targetState = state;
                ViewCompat.postOnAnimation(child, settleRunnable);
                settleRunnable.isPosted = true;
            } else {
                // Otherwise, if it has been posted, just update the target state.
                settleRunnable.targetState = state;
            }
        } else {
            setStateInternal(state);
        }
    }

    void setStateInternal(BottomSheetState state) {
        if (this.state == state) {
            return;
        }
        this.state = state;

        // TODO: onStateChanged
    }

    private class SettleRunnable implements Runnable {

        private final View view;

        private boolean isPosted;

        BottomSheetState targetState;

        SettleRunnable(View view, BottomSheetState targetState) {
            this.view = view;
            this.targetState = targetState;
        }

        @Override
        public void run() {
            if (viewDragHelper != null && viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(view, this);
            } else {
                setStateInternal(targetState);
            }
            this.isPosted = false;
        }
    }

}
