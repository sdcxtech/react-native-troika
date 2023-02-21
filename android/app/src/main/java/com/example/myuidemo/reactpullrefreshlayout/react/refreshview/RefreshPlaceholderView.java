package com.example.myuidemo.reactpullrefreshlayout.react.refreshview;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myuidemo.reactpullrefreshlayout.react.ReactLinearPlaceholderLayout;
import com.example.myuidemo.reactpullrefreshlayout.refreshView.IRefreshView;
import com.facebook.react.uimanager.PointerEvents;
import com.facebook.react.uimanager.ReactPointerEventsView;


public class RefreshPlaceholderView extends ReactLinearPlaceholderLayout implements IRefreshView, ReactPointerEventsView {
    private static final String TAG = "RefreshPlaceholderView";

    public RefreshPlaceholderView(@NonNull Context context) {
        super(context);
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
    }

    @Override
    public void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset) {
        if (currentRefreshViewOffset == 0) {
            disableTouch();
        } else {
            enableTouch();
        }
    }

    @Override
    public void onStop() {
        disableTouch();
    }
}

