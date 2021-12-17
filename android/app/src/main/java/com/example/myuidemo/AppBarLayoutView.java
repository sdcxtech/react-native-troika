package com.example.myuidemo;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;

public class AppBarLayoutView extends AppBarLayout {
    public AppBarLayoutView(@NonNull Context context) {
        super(context);
        int width  = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(width, height);
        this.setLayoutParams(params);
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);

        // Notify to parent CoordinatorLayoutView to update if exist
        ViewParent parentView = this.getParent();
        while (parentView != null) {
            if (parentView instanceof CoordinatorLayoutView) {
                parentView.requestLayout();
                break;
            }
            parentView = parentView.getParent();
        }
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

}
