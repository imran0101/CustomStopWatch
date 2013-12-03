package com.imran.customview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * Created by imranmohammed on 29/09/13.
 */
public class CustomDrawable extends Drawable {

    BarShape shape;
    Paint mPaint;
    ScaleAnimation mScaleAnimation;
    PointF mInPoint;
    PointF mMidPoint;
    PointF mExtPoint;
    PointF mScalePoint;
    boolean mLarge;

    CustomDrawable(boolean large, PointF inPoint, PointF midPoint, PointF extPoint, PointF scalePoint) {
        if (large) {
            shape = new BarShape(inPoint.x, inPoint.y, extPoint.x, extPoint.y);
            this.mScalePoint = midPoint;
        } else {
            shape = new BarShape(inPoint.x, inPoint.y, midPoint.x, midPoint.y);
            this.mScalePoint = scalePoint;
        }

        mInPoint = inPoint;

        this.mMidPoint = midPoint;
        this.mExtPoint = extPoint;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);

    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setAnimation() {

        mScaleAnimation = new ScaleAnimation(1.0f,
                2.5f,
                1.0f,
                2.5f,
                Animation.ABSOLUTE,
                mScalePoint.x,
                Animation.ABSOLUTE,
                mScalePoint.y);

        mScaleAnimation.setDuration(1000);
        mScaleAnimation.setRepeatMode(Animation.REVERSE);
        mScaleAnimation.setRepeatCount(1);
        mScaleAnimation.setFillEnabled(true);
        mScaleAnimation.setFillAfter(false);
        mScaleAnimation.setInterpolator(new LinearInterpolator());
    }

    public ScaleAnimation getAnimation() {
        return mScaleAnimation;
    }

    @Override
    public void draw(Canvas canvas) {
        shape.draw(canvas, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    /**
     * Custom shape to draw on canvas
     */
    private class BarShape extends Shape {

        Path path;

        public BarShape(float left, float top, float right, float bottom) {
            path = new Path();
            path.setFillType(Path.FillType.WINDING);
            path.moveTo(left, top);
            path.lineTo(right, bottom);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            if (path != null) {
                canvas.drawPath(path, paint);
            }
        }
    }
}
