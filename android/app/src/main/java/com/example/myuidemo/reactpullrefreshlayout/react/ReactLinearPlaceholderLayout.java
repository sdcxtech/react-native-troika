package com.example.myuidemo.reactpullrefreshlayout.react;

import android.content.Context;
import android.util.Size;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.myuidemo.Helper.LinearLayoutReactViewGroupMeasureHelper;


public class ReactLinearPlaceholderLayout extends LinearLayout {
    private static final String TAG = "PullRefreshLayoutPlaceholderView";
    private final LinearLayoutReactViewGroupMeasureHelper linearLayoutReactViewGroupMeasureHelper;

    public ReactLinearPlaceholderLayout(@NonNull Context context) {
        super(context);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setLayoutParams(new LayoutParams(width, height));
        setOrientation(VERTICAL);
        linearLayoutReactViewGroupMeasureHelper = new LinearLayoutReactViewGroupMeasureHelper(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Size size = linearLayoutReactViewGroupMeasureHelper.getLayoutSize();
        boolean isSizeChange = getWidth() != size.getWidth() || getHeight() != size.getHeight();
        setMeasuredDimension(size.getWidth(), size.getHeight());
        if (isSizeChange) {
            layout(getLeft(), getTop(), getRight(), getBottom());
            postDelayed(this::requestLayout, 0);
        }
    }
}

