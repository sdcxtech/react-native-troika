package com.reactnative.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;

public class PullToRefresh extends SmartRefreshLayout {

    public PullToRefresh(Context context) {
        super(context);
    }

    public PullToRefresh(Context context, AttributeSet attrs) {
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float height = (float) (getMeasuredHeight() * 0.3);
        RefreshHeader header = getRefreshHeader();
        if (header != null) {
            int headerHeight = header.getView().getMeasuredHeight();
            setHeaderMaxDragRate(height / headerHeight);
        }
        RefreshFooter footer = getRefreshFooter();
        if (footer != null) {
            int footerHeight = footer.getView().getMeasuredHeight();
            setFooterMaxDragRate(height / footerHeight);
        }
    }
}
