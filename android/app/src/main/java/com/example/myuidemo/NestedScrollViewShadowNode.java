package com.example.myuidemo;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ReactShadowNode;

public class NestedScrollViewShadowNode extends LayoutShadowNode {
    private final static String TAG = "NestedScrollViewNode";

    @Override
    public void setLocalData(Object data) {
        if (data instanceof NestedScrollVIewLocalData) {
            NestedScrollVIewLocalData nestedScrollVIewLocalData = ((NestedScrollVIewLocalData) data);
            ReactShadowNode<?> parent = getChildAt(0);
            parent.setStyleMinHeight(nestedScrollVIewLocalData.containerNodeH);
            parent.setStyleMaxHeight(nestedScrollVIewLocalData.containerNodeH);
            for (int i = 0, count = parent.getChildCount(); i < count; i++) {
                ReactShadowNode<?> shadowNode = parent.getChildAt(i);
                float childNodeH = AppBarLayoutManager.REACT_CLASS.equals((shadowNode.getViewClass()))
                        ? nestedScrollVIewLocalData.appbarNodeH
                        : nestedScrollVIewLocalData.contentNodeH;
                shadowNode.setStyleMinHeight(childNodeH);
                shadowNode.setStyleMaxHeight(childNodeH);
            }
        }
    }
}
