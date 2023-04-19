package com.reactnative.pulltorefresh;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.NativeViewHierarchyOptimizer;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaPositionType;

public class ReactSmartPullRefreshHeaderShadowNode extends LayoutShadowNode {
    @Override
    public void setLocalData(Object data) {
        super.setLocalData(data);
        if (data instanceof ReactSmartPullRefreshHeaderLocalData) {
            ReactSmartPullRefreshHeaderLocalData headerLocalData = (ReactSmartPullRefreshHeaderLocalData) data;
            setStyleHeight(headerLocalData.viewRect.bottom - headerLocalData.viewRect.top);
            calculateLayout();
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
