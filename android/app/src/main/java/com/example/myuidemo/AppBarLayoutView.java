package com.example.myuidemo;

import android.content.Context;
import android.util.Size;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.myuidemo.Helper.LinearLayoutReactViewGroupMeasureHelper;
import com.google.android.material.appbar.AppBarLayout;

public class AppBarLayoutView extends AppBarLayout {
    private final LinearLayoutReactViewGroupMeasureHelper linearLayoutReactViewGroupMeasureHelper;

    public AppBarLayoutView(@NonNull Context context) {
        super(context);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(width, height);
        this.setLayoutParams(params);
        linearLayoutReactViewGroupMeasureHelper = new LinearLayoutReactViewGroupMeasureHelper(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (linearLayoutReactViewGroupMeasureHelper.shouldDelegate()) {
            Size size = linearLayoutReactViewGroupMeasureHelper.getLayoutSize();
            boolean isSizeChange = getWidth() != size.getWidth() || getHeight() != size.getHeight();
            setMeasuredDimension(size.getWidth(), size.getHeight());
            if (isSizeChange) {
                layout(getLeft(), getTop(), getRight(), getBottom());
                requestLayout();
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}

