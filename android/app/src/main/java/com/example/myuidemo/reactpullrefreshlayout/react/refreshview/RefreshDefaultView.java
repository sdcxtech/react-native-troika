package com.example.myuidemo.reactpullrefreshlayout.react.refreshview;

import android.content.Context;

import com.example.myuidemo.reactpullrefreshlayout.refreshView.SpinnerRefreshView;
import com.facebook.react.uimanager.PointerEvents;
import com.facebook.react.uimanager.ReactPointerEventsView;

public class RefreshDefaultView extends SpinnerRefreshView implements ReactPointerEventsView {
    public RefreshDefaultView(Context context) {
        super(context);
    }

    PointerEvents mPointerEvent = PointerEvents.NONE;

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
        super.onRefresh();
    }

    @Override
    public void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset) {
        if (currentRefreshViewOffset == 0) {
            disableTouch();
        } else {
            enableTouch();
        }
        super.onPull(currentRefreshViewOffset, currentTargetViewOffset, totalRefreshViewOffset, totalTargetViewOffset);
    }

    @Override
    public void onStop() {
        disableTouch();
        super.onStop();
    }

    @Override
    public PointerEvents getPointerEvents() {
        return mPointerEvent;
    }

}
