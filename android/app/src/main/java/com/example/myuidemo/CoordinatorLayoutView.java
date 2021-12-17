package com.example.myuidemo;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;


public class CoordinatorLayoutView extends CoordinatorLayout {

    public CoordinatorLayoutView(@NonNull Context context) {
        super(context);
//        int width  = ViewGroup.LayoutParams.MATCH_PARENT;
//        int height = ViewGroup.LayoutParams.MATCH_PARENT;
//        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(width, height);
//        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
//        this.setLayoutParams(params);
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }
}
