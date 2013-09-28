package com.imran.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imranmohammed on 28/09/13.
 */
public class StopWatchView extends View {


    float innerCircleRadius, innerCircleCircumference;
    float outerCircleRadius, outerCircleCircumference;
    float cX = 0;
    float cY = 0;
    float cInnerOffset = 0;
    float cOuterOffset = 0;
    boolean once = true;
    Matrix mRotateTransform;
    Point mInPoint;
    Point mExtPoint;
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

    void init() {
        if (!isInEditMode()) {
            innerCircleRadius = getResources().getDimension(R.dimen.inner_circle_radius);
            innerCircleCircumference = (float) Math.PI * innerCircleRadius * 2;
            outerCircleRadius = getResources().getDimension(R.dimen.outer_circle_radius);
            outerCircleCircumference = (float) Math.PI * outerCircleRadius * 2;

            cInnerOffset = innerCircleCircumference / 60;
            cOuterOffset = outerCircleCircumference / 60;

            mRotateTransform = new Matrix();

            mBarShapeDrawables = new ArrayList<BarShapeDrawable>();
        }
    }

    private void setmInPoint(int x, int y) {
        mInPoint = new Point(x, y);
    }

    private void setmExtPoint(int x, int y) {
        mExtPoint = new Point(x, y);
    }

    private Point rotate(Point point, float centerX, float centerY) {
        mRotateTransform.setRotate(6, centerX, centerY);
        float[] pts = new float[2];

        pts[0] = point.x;
        pts[1] = point.y;

        mRotateTransform.mapPoints(pts);
        return new Point((int) pts[0], (int) pts[1]);
    }

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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        cX = right / 2;
        cY = bottom / 2;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startAnimation() {

        for (BarShapeDrawable b : mBarShapeDrawables) {
            b.startAnimation();
        }
    }

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
