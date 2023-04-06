package com.example.myuidemo;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ReactShadowNode;

public class NestedScrollViewShadowNode extends LayoutShadowNode {
    private final static String TAG = "NestedScrollViewNode";

    @Override
    public void setLocalData(Object data) {
        if (data instanceof NestedScrollViewLocalData) {
            NestedScrollViewLocalData nestedScrollViewLocalData = ((NestedScrollViewLocalData) data);
            ReactShadowNode<?> parent = getChildAt(0);
            float parentNodeH = nestedScrollViewLocalData.headerNodeH + nestedScrollViewLocalData.contentNodeH;
            setNodeHeight(parent, parentNodeH);
            for (int i = 0, count = parent.getChildCount(); i < count; i++) {
                ReactShadowNode<?> shadowNode = parent.getChildAt(i);
                float childNodeH = NestedScrollViewHeaderManager.REACT_CLASS.equals((shadowNode.getViewClass()))
                        ? nestedScrollViewLocalData.headerNodeH
                        : nestedScrollViewLocalData.contentNodeH;
                setNodeHeight(shadowNode, childNodeH);
            }
        }
    }

    void setNodeHeight(ReactShadowNode<?> shadowNode, float h) {
        shadowNode.setStyleMinHeight(h);
        shadowNode.setStyleMaxHeight(h);
    }
}
