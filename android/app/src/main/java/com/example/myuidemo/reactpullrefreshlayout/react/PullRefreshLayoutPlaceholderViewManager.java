package com.example.myuidemo.reactpullrefreshlayout.react;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class PullRefreshLayoutPlaceholderViewManager extends ViewGroupManager<PullRefreshLayoutPlaceholderView> {
    @NonNull
    @Override
    public String getName() {
        return "PullRefreshLayoutPlaceholderView";
    }

    @NonNull
    @Override
    protected PullRefreshLayoutPlaceholderView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new PullRefreshLayoutPlaceholderView(reactContext);
    }


    @ReactProp(name = "viewType")
    public void setViewType(PullRefreshLayoutPlaceholderView pullRefreshLayoutPlaceholderView, String type) {
        if (type.equals("REFRESH")) {
            pullRefreshLayoutPlaceholderView.setViewType(ViewType.REFRESH);
        } else if (type.equals("LOAD_MORE")) {
            pullRefreshLayoutPlaceholderView.setViewType(ViewType.LOAD_MORE);
        } else {
            pullRefreshLayoutPlaceholderView.setViewType(ViewType.NONE);
        }
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return false;
    }

}
