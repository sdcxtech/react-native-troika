package com.reactnative.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class ReactSmartPullRefreshLayout extends SmartRefreshLayout {

    public ReactSmartPullRefreshLayout(Context context) {
        super(context);
    }

    public ReactSmartPullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final Runnable measureAndLayout = () -> {
        measure(
                View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(getHeight(), View.MeasureSpec.EXACTLY));
        layout(getLeft(), getTop(), getRight(), getBottom());
    };

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }
}
