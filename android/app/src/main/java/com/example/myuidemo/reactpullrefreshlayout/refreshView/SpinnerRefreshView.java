package com.example.myuidemo.reactpullrefreshlayout.refreshView;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;


public class SpinnerRefreshView extends AppCompatImageView implements IRefreshView {
    private static final String TAG = "SpinnerRefreshView";

    private static final int MAX_ALPHA = 255;
    private static final float TRIM_RATE = 0.85f;
    private static final float TRIM_OFFSET = 0.4f;
    static final int CIRCLE_DIAMETER = 40;

    private CircularProgressDrawable mProgress;
    private int mCircleDiameter;


    public SpinnerRefreshView(Context context) {
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
    public void onRefresh() {
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
    public void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset) {
        if (mProgress.isRunning()) {
            return;
        }
        setVisibility(currentRefreshViewOffset > 0 ? VISIBLE : GONE);
        float end = Math.min(TRIM_RATE * currentRefreshViewOffset / totalRefreshViewOffset, TRIM_RATE);
        float rotate = TRIM_OFFSET * currentRefreshViewOffset / totalRefreshViewOffset;
        int overPull = currentTargetViewOffset - totalTargetViewOffset;
        if (overPull > 0) {
            rotate += TRIM_OFFSET * overPull / totalRefreshViewOffset;
        }

        mProgress.setArrowEnabled(true);
        mProgress.setStartEndTrim(0, end);
        mProgress.setProgressRotation(rotate);
    }

    @Override
    public void onStop() {
        mProgress.stop();
        setVisibility(GONE);
    }
}
