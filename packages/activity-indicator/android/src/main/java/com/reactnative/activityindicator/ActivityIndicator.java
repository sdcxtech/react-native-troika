package com.reactnative.activityindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.PixelUtil;


public class ActivityIndicator extends View {

    private int mSize;
    private int mPaintColor;

    private boolean mAnimating;

    private int mAnimateValue = 0;
    private ValueAnimator mAnimator;
    private Paint mPaint;
    private static final int LINE_COUNT = 8;
    private static final int DEGREE_PER_LINE = 360 / LINE_COUNT;

    public ActivityIndicator(Context context) {
        this(context, null);
    }

    public ActivityIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public ActivityIndicator(Context context, int size, int color) {
        super(context);
        mSize = size;
        mPaintColor = color;
        initPaint();
    }

    private void initPaint() {
        mSize = (int) (PixelUtil.toPixelFromDIP(20) + 0.5);
        mPaintColor = Color.parseColor("#999999");
        mAnimating = true;

        mPaint = new Paint();
        mPaint.setColor(mPaintColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setColor(int color) {
        mPaintColor = color;
        mPaint.setColor(color);
        invalidate();
    }

    public void setSize(int size) {
        mSize = size;
        requestLayout();
    }

    public void setAnimating(boolean animating) {
        mAnimating = animating;
        if (mAnimating) {
            start();
        } else {
            stop();
        }
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = animation -> {
        mAnimateValue = (int) animation.getAnimatedValue();
        invalidate();
    };

    public void start() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, LINE_COUNT - 1);
            mAnimator.addUpdateListener(mUpdateListener);
            mAnimator.setDuration(1000);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        } else if (!mAnimator.isStarted()) {
            mAnimator.start();
        }
    }

    public void stop() {
        if (mAnimator != null) {
            mAnimator.removeUpdateListener(mUpdateListener);
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private void drawLoading(Canvas canvas, int rotateDegrees) {
        int width = mSize / 7, height = mSize / 5;
        mPaint.setStrokeWidth(width);
        float radius = mSize / 2.0f;
        canvas.rotate(rotateDegrees, radius, radius);
        canvas.translate(radius, radius);
        for (int i = 0; i < LINE_COUNT; i++) {
            canvas.rotate(DEGREE_PER_LINE);
            float factor = (i + 1f) / LINE_COUNT;
            mPaint.setAlpha(Math.max((int) (255f * (factor * factor)), 64));
            canvas.translate(0, -radius + width * 0.5f);
            canvas.drawLine(0, 0, 0, height, mPaint);
            canvas.translate(0, radius - width * 0.5f);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        drawLoading(canvas, mAnimateValue * DEGREE_PER_LINE);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAnimating) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && mAnimating) {
            start();
        } else {
            stop();
        }
    }

}
