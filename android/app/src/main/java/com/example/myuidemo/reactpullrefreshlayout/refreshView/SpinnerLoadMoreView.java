package com.example.myuidemo.reactpullrefreshlayout.refreshView;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;


public class SpinnerLoadMoreView extends AppCompatImageView implements ILoadMoreView {
    private static final String TAG = "SpinnerLoadMoreView";

    private static final int MAX_ALPHA = 255;
    private static final float TRIM_RATE = 0.85f;
    private static final float TRIM_OFFSET = 0.4f;
    static final int CIRCLE_DIAMETER = 70;

    private final CircularProgressDrawable mProgress;
    private final int mCircleDiameter;

    public SpinnerLoadMoreView(Context context) {
        super(context);
        mProgress = new CircularProgressDrawable(context);
        mProgress.setStyle(CircularProgressDrawable.LARGE);
        mProgress.setAlpha(MAX_ALPHA);
        mProgress.setArrowScale(0.8f);
        setImageDrawable(mProgress);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleDiameter = (int) (CIRCLE_DIAMETER * metrics.density);
        setVisibility(VISIBLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mCircleDiameter, mCircleDiameter);
    }

    @Override
    public void onLoadMore() {
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        if (mProgress.isRunning()) {
            return;
        }
        mProgress.setArrowEnabled(false);
        mProgress.setStartEndTrim(0, TRIM_RATE);
        mProgress.setProgressRotation(TRIM_OFFSET);
        mProgress.start();
    }

    @Override
    public void onPull(int offset, int total) {
        if (mProgress.isRunning()) {
            return;
        }
        setVisibility(offset != 0 ? VISIBLE : GONE);
        float end = Math.min(TRIM_RATE, TRIM_RATE * offset / total);
        float rotate = TRIM_OFFSET * offset / total;

        int overPull = Math.abs(offset) - Math.abs(total);
        if (overPull > 0) {
            rotate += TRIM_OFFSET * overPull / total;
        }
        mProgress.setArrowEnabled(true);
        mProgress.setStartEndTrim(0, end);
        mProgress.setProgressRotation(rotate);
    }

    @Override
    public void onStop() {
        setVisibility(GONE);
        mProgress.stop();
    }
}
