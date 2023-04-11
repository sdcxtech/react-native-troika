package com.example.myuidemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.MeasureSpecAssertions;
import com.facebook.react.uimanager.ReactOverflowView;
import com.facebook.react.uimanager.UIManagerModule;

public class NestedScrollView extends androidx.core.widget.NestedScrollView implements ReactOverflowView {
    private final NestedScrollViewLocalData mNestedScrollViewLocalData = new NestedScrollViewLocalData();
    private String mOverflow = "hidden";
    private final Rect mRect;

    public NestedScrollView(@NonNull Context context) {
        super(context);
        mRect = new Rect();
    }

    public void setOverflow(String overflow) {
        mOverflow = overflow;
        this.invalidate();
    }

    @Nullable
    @Override
    public String getOverflow() {
        return mOverflow;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(target, dx, dy, consumed, type);
        int dyUnconsumed = dy - consumed[1];
        if (dyUnconsumed > 0) {
            final int oldScrollY = getScrollY();
            scrollBy(0, dyUnconsumed);
            final int myConsumed = getScrollY() - oldScrollY;
            consumed[1] += myConsumed;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MeasureSpecAssertions.assertExplicitMeasureSpec(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec)
        );
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Context context = getContext();
        if (context instanceof ReactContext) {
            UIManagerModule uiManagerModule = ((ReactContext) context).getNativeModule(UIManagerModule.class);
            if (uiManagerModule != null) {
                ViewGroup viewGroup = (ViewGroup) getChildAt(0);
                int headerFixedHeight = 0;
                float headerHeight = 0;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    if (child instanceof NestedScrollViewHeader) {
                        headerFixedHeight = ((NestedScrollViewHeader) child).getFixedHeight();
                        headerHeight = child.getHeight();
                    }
                }
                int nestedScrollViewH = getHeight();
                float contentHeight = nestedScrollViewH - headerFixedHeight;
                if (contentHeight != mNestedScrollViewLocalData.contentNodeH || headerHeight != mNestedScrollViewLocalData.headerNodeH) {
                    mNestedScrollViewLocalData.contentNodeH = contentHeight;
                    mNestedScrollViewLocalData.headerNodeH = headerHeight;
                    uiManagerModule.setViewLocalData(getId(), mNestedScrollViewLocalData);
                }
                int maxScrollRange = (int) (headerHeight - headerFixedHeight);
                if (getScrollY() > maxScrollRange) {
                    scrollTo(0, maxScrollRange);
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.getDrawingRect(mRect);
        if (!"visible".equals(mOverflow)) {
            canvas.clipRect(mRect);
        }
        super.draw(canvas);
    }
}
