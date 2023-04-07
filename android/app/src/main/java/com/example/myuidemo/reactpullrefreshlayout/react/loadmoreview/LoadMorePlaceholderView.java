package com.example.myuidemo.reactpullrefreshlayout.react.loadmoreview;

import androidx.annotation.NonNull;

import com.example.myuidemo.reactpullrefreshlayout.react.MJRefreshState;
import com.example.myuidemo.reactpullrefreshlayout.react.event.OffsetChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.RefreshEvent;
import com.example.myuidemo.reactpullrefreshlayout.react.event.StateChangedEvent;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.ILoadMoreView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.view.ReactViewGroup;


public class LoadMorePlaceholderView extends ReactViewGroup implements ILoadMoreView {
    private static final String TAG = "LoadMorePlaceholderView";

    private final ReactContext reactContext;
    private MJRefreshState state = MJRefreshState.Idle;

    public LoadMorePlaceholderView(@NonNull ThemedReactContext context) {
        super(context);
        this.reactContext = context;
    }

    @Override
    public void onLoadMore() {
        setState(MJRefreshState.Refreshing);
        emitEvent(reactContext, new RefreshEvent(UIManagerHelper.getSurfaceId(this), getId()));
    }

    @Override
    public void onPull(int offset, int total) {
        // FLog.i(TAG, "offset:" + offset + " total:" + total);
        emitEvent(reactContext, new OffsetChangedEvent(UIManagerHelper.getSurfaceId(this), getId(), offset));

        if (this.state == MJRefreshState.Refreshing) {
            return;
        }

        if (offset >= total) {
            setState(MJRefreshState.Coming);
        } else {
            setState(MJRefreshState.Idle);
        }
    }

    @Override
    public void onStop() {
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

