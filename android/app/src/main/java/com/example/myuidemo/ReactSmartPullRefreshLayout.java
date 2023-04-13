package com.example.myuidemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class ReactSmartPullRefreshLayout extends SmartRefreshLayout {
    String TAG = "SmartPullRefreshLayout";

    public ReactSmartPullRefreshLayout(Context context) {
        super(context);
    }

    public ReactSmartPullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final Runnable measureAndLayout = () -> {
        measure(
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        layout(getLeft(), getTop(), getRight(), getBottom());
    };

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

}
