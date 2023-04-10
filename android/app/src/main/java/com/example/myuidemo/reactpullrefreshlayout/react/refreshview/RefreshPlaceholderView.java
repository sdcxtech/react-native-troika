package com.example.myuidemo.reactpullrefreshlayout.react.refreshview;

import androidx.annotation.NonNull;

import com.example.myuidemo.reactpullrefreshlayout.react.MJRefreshState;
import com.example.myuidemo.reactpullrefreshlayout.react.event.OffsetChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.StateChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.IRefreshView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.PointerEvents;
import com.facebook.react.uimanager.ReactPointerEventsView;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.view.ReactViewGroup;

public class RefreshPlaceholderView extends ReactViewGroup implements IRefreshView, ReactPointerEventsView {
    private static final String TAG = "RefreshPlaceholderView";

    private final ReactContext reactContext;

    private MJRefreshState state = MJRefreshState.Idle;

    public RefreshPlaceholderView(@NonNull ReactContext context) {
        super(context);
        this.reactContext = context;
    }

    PointerEvents mPointerEvent = PointerEvents.NONE;

    @Override
    public PointerEvents getPointerEvents() {
        return mPointerEvent;
    }

    void enableTouch() {
        if (mPointerEvent == PointerEvents.NONE) {
            mPointerEvent = PointerEvents.AUTO;
        }
    }

    void disableTouch() {
        mPointerEvent = PointerEvents.NONE;
    }

    @Override
    public void onRefresh() {
        enableTouch();
        setState(MJRefreshState.Refreshing);
        emitEvent(reactContext, new RefreshEvent(UIManagerHelper.getSurfaceId(this), getId()));
    }

    @Override
    public void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset) {
        if (currentRefreshViewOffset == 0) {
            disableTouch();
        } else {
            enableTouch();
        }

        emitEvent(reactContext, new OffsetChangedEvent(UIManagerHelper.getSurfaceId(this), getId(), currentTargetViewOffset));

        if (this.state == MJRefreshState.Refreshing) {
            return;
        }

//        FLog.i(TAG, "currentRefreshViewOffset:" + currentRefreshViewOffset +
//                " currentTargetViewOffset:" + currentTargetViewOffset +
//                " totalRefreshViewOffset:" + totalRefreshViewOffset +
//                " totalTargetViewOffset:" + totalTargetViewOffset);

        if (currentRefreshViewOffset >= totalRefreshViewOffset) {
            setState(MJRefreshState.Coming);
        } else {
            setState(MJRefreshState.Idle);
        }
    }

    @Override
    public void onStop() {
        disableTouch();
        setState(MJRefreshState.Idle);
    }

    public void setState(MJRefreshState state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        emitEvent(reactContext, new StateChangedEvent(UIManagerHelper.getSurfaceId(this), getId(), this.state));
    }

    void emitEvent(ReactContext reactContext, Event<?> event) {
        if (reactContext.hasActiveReactInstance()) {
            EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, getId());
            if (eventDispatcher != null) {
                eventDispatcher.dispatchEvent(event);
            }
        }
    }
}

