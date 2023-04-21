package com.reactnative.pulltorefresh;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.NativeViewHierarchyOptimizer;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaPositionType;

public class PullToRefreshFooterShadowNode extends LayoutShadowNode {
    @Override
    public void setLocalData(Object data) {
        super.setLocalData(data);
        if (data instanceof PullToRefreshFooterLocalData) {
            PullToRefreshFooterLocalData footerLocalData = (PullToRefreshFooterLocalData) data;
            setStyleHeight(footerLocalData.viewRect.bottom - footerLocalData.viewRect.top);
            calculateLayout();
        }
    }

    @Override
    public void onBeforeLayout(NativeViewHierarchyOptimizer nativeViewHierarchyOptimizer) {
        setPositionType(YogaPositionType.ABSOLUTE);
        setPosition(YogaEdge.LEFT.intValue(), 0);
        setPosition(YogaEdge.RIGHT.intValue(), 0);
        setPosition(YogaEdge.BOTTOM.intValue(), -getLayoutHeight());
    }
}
