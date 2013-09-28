package com.imran.customview;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ShapeDrawable;

/**
 * Created by imranmohammed on 28/09/13.
 */
public class ShapeHolder {
    Point inPoint;
    Point extPoint;
    boolean animate = false;
    private float x = 0, y = 0;
    private ShapeDrawable sDrawable;
    private AnimationDrawable aDrawable;
    private Paint paint;

    ShapeHolder(ShapeDrawable sDrawable, Point inPoint, Point extPoint) {
        this.sDrawable = sDrawable;
        this.inPoint = inPoint;
        this.extPoint = extPoint;
        setX(inPoint.x);
        setY(inPoint.y);
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Point getInPoint() {
        return inPoint;
    }

    public void setInPoint(Point inPoint) {
        this.inPoint = inPoint;
    }

    public Point getExtPoint() {
        return extPoint;
    }

    public void setExtPoint(Point extPoint) {
        this.extPoint = extPoint;
    }

    public ShapeDrawable getsDrawable() {
        return sDrawable;
    }

    public void setsDrawable(ShapeDrawable sDrawable) {
        this.sDrawable = sDrawable;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getWidth() {
        return sDrawable.getShape().getWidth();
    }

    public float getHeight() {
        return sDrawable.getShape().getHeight();
    }
}
