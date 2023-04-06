package com.example.myuidemo.reactpullrefreshlayout.react.loadmoreview;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.NativeViewHierarchyOptimizer;
import com.facebook.yoga.YogaPositionType;

public class LoadMorePlaceholderShadowNode extends LayoutShadowNode {

    @Override
    public void onBeforeLayout(NativeViewHierarchyOptimizer nativeViewHierarchyOptimizer) {
        setPositionType(YogaPositionType.ABSOLUTE);
        setPosition(0, 0); // left
        setPosition(2, 0); // right
        setPosition(3, 0); // bottom
    }
}
