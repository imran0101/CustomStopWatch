package com.imran.customview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by imranmohammed on 29/09/13.
 */
public class CustomDrawable extends Drawable {

    Point mInPoint;
    Point mExtPoint;
    Point mMidPoint;
    BarShape shape;
    Paint mPaint;
    RectF mRectF;

    float mX;

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
    }

    float mY;

    CustomDrawable(Point inPoint,Point midPoint, Point extPoint){
        shape = new BarShape(inPoint.x, inPoint.y, extPoint.x, extPoint.y);

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    CustomDrawable(RectF rectF){
        mRectF = new RectF();

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

        this.mRectF = rectF;
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

        public BarShape(int left, int top, int right, int bottom) {
            path = new Path();
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
