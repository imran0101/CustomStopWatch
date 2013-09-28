package com.imran.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imranmohammed on 28/09/13.
 */
public class StopWatchView extends View {


    // inner circle radius and circumference.
    float innerCircleRadius, innerCircleCircumference;
    // outer circle radius and circumference
    float outerCircleRadius, outerCircleCircumference;

    // Center cX and cY
    float cX = 0;
    float cY = 0;

    //To set the drawables once.
    boolean once = true;

    //matrix used to transform and get the points on circle.
    Matrix mRotateTransform;
    //Point on inner circle
    Point mInPoint;
    //Point on ext circle
    Point mExtPoint;

    /**
     * List of custom shape drawables.
     * 2 points are stored.
     *
     * Animation to be done on these drawables.
     */
    List<BarShapeDrawable> mBarShapeDrawables;


    public StopWatchView(Context context) {
        super(context);
        init();
    }

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StopWatchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Initialize method. Get circle dimens from dimens.xml
     * Inner and outer circle basing on which the points are drawn.
     *
     * Initialize all lists and resources.
     */
    void init() {
        if (!isInEditMode()) {
            innerCircleRadius = getResources().getDimension(R.dimen.inner_circle_radius);
            innerCircleCircumference = (float) Math.PI * innerCircleRadius * 2;
            outerCircleRadius = getResources().getDimension(R.dimen.outer_circle_radius);
            outerCircleCircumference = (float) Math.PI * outerCircleRadius * 2;

            mRotateTransform = new Matrix();

            mBarShapeDrawables = new ArrayList<BarShapeDrawable>();
        }
    }

    /**
     * Set initial x and y to start rotate and transform.
     * Initially x = x
     * y = y1 - innerCircleRadius
     * @param x
     * @param y
     */
    private void setmInPoint(int x, int y) {
        mInPoint = new Point(x, y);
    }

    /**
     * Set inital x and y on ext Circle
     * x = x
     * y = y1 - outerCircleRadius
     * @param x
     * @param y
     */
    private void setmExtPoint(int x, int y) {
        mExtPoint = new Point(x, y);
    }

    /**
     * Method to rotate the with the circle center and get the points on the circle
     * Once for inner circle and once for outer circle
     * @param point
     * @param centerX
     * @param centerY
     * @return
     */
    private Point rotate(Point point, float centerX, float centerY) {
        mRotateTransform.setRotate(6, centerX, centerY);
        float[] pts = new float[2];

        pts[0] = point.x;
        pts[1] = point.y;

        mRotateTransform.mapPoints(pts);
        return new Point((int) pts[0], (int) pts[1]);
    }

    /**
     * Setup the points on circle and shape drawables for once on onDraw method.
     */
    private void setup() {

        setmInPoint((int) (cX - innerCircleRadius), (int) (cY - innerCircleRadius));
        setmExtPoint((int) (cX - outerCircleRadius), (int) (cY - outerCircleRadius));

        for (int i = 0; i < 60; i++) {

            BarShapeDrawable d = new BarShapeDrawable(mInPoint, mExtPoint);
            mBarShapeDrawables.add(d);
            mInPoint = rotate(mInPoint, cX, cY);
            mExtPoint = rotate(mExtPoint, cX, cY);
        }
    }

    /**
     * Get the center of the circle. Center is the same for inner and outer circle. (Concentric.)
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        cX = right / 2;
        cY = bottom / 2;
    }

    /**
     * working on it.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startAnimation() {

        for (BarShapeDrawable b : mBarShapeDrawables) {
            b.startAnimation();
        }
    }

    /**
     * Draw the list of custom shape drawables.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (once && !isInEditMode()) {
            setup();
            once = false;
        }

        for (BarShapeDrawable b : mBarShapeDrawables) {
            b.draw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
