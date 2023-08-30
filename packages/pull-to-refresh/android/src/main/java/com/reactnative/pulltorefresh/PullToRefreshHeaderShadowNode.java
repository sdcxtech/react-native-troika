package com.reactnative.pulltorefresh;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.NativeViewHierarchyOptimizer;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaPositionType;

public class PullToRefreshHeaderShadowNode extends LayoutShadowNode {
    @Override
    public void setLocalData(Object data) {
        super.setLocalData(data);
        if (data instanceof PullToRefreshHeaderLocalData) {
            PullToRefreshHeaderLocalData headerLocalData = (PullToRefreshHeaderLocalData) data;
            setStyleHeight(headerLocalData.viewRect.bottom - headerLocalData.viewRect.top);
        }
    }

    @Override
    public void onBeforeLayout(NativeViewHierarchyOptimizer nativeViewHierarchyOptimizer) {
        setPositionType(YogaPositionType.ABSOLUTE);
        setPosition(YogaEdge.LEFT.intValue(), 0);
        setPosition(YogaEdge.RIGHT.intValue(), 0);
        setPosition(YogaEdge.TOP.intValue(), -getLayoutHeight());
    }
}
