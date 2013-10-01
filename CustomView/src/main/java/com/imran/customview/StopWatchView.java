package com.imran.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imranmohammed on 28/09/13.
 */
public class StopWatchView extends View implements ValueAnimator.AnimatorUpdateListener, Animation.AnimationListener {


    // inner circle radius and circumference.
    float innerCircleRadius, innerCircleCircumference;
    // outer circle radius and circumference
    float outerCircleRadius, outerCircleCircumference;
    // middle circle radius and circumference
    float middleCircleRadius, middleCircleCircumference;
    ScaleAnimation mScaleAnimation;
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
    //Point on the middle circle
    Point mMidPoint;

    RectF rectF;

    /**
     * List of custom shape drawables.
     * 2 points are stored.
     * <p/>
     * Animation to be done on these drawables.
     */
    List<AnimateDrawable> mAnimateDrawables;
    Animation mAnimation;
    boolean isAnimating = false;
    CustomDrawable custom;
    ProxyDrawable proxy;
    AnimateDrawable a;
    private int mCurrentPosition = 0;

    private ObjectAnimator mObjectAnimator;

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
     * <p/>
     * Initialize all lists and resources.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void init() {
        if (!isInEditMode()) {
            innerCircleRadius = getResources().getDimension(R.dimen.inner_circle_radius);
            innerCircleCircumference = (float) Math.PI * innerCircleRadius * 2;
            middleCircleRadius = getResources().getDimension(R.dimen.middle_circle_radius);
            middleCircleCircumference = (float) Math.PI * middleCircleRadius * 2;
            outerCircleRadius = getResources().getDimension(R.dimen.outer_circle_radius);
            outerCircleCircumference = (float) Math.PI * outerCircleRadius * 2;

            mRotateTransform = new Matrix();

            Point leftPoint = new Point(100, 10);
            Point midPoint = new Point(125, 10);
            Point rightPoint = new Point(150, 10);

            custom = new CustomDrawable(leftPoint, midPoint, rightPoint);

            Animation an =  new ScaleAnimation(0.9f,
                    1.0f,
                    1.0f,
                    1.0f,
                    midPoint.x,
                    midPoint.y);
            an.setDuration(2000);
            an.setRepeatCount(-1);

            proxy = new ProxyDrawable(custom);
            proxy.setX(leftPoint.x);
            proxy.setY(leftPoint.y);
            mObjectAnimator = ObjectAnimator.ofFloat(proxy, "x", midPoint.x, rightPoint.x);

            mObjectAnimator.addUpdateListener(this);
            mObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mObjectAnimator.setRepeatCount(20);
            mObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mObjectAnimator.setDuration(400);
            mObjectAnimator.start();

            a = new AnimateDrawable(custom, an);
            an.start();
            mAnimateDrawables = new ArrayList<AnimateDrawable>();
        }
    }

    /**
     * Set initial x and y to start rotate and transform.
     * Initially x = x
     * y = y1 - innerCircleRadius
     *
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
     *
     * @param x
     * @param y
     */
    private void setmExtPoint(int x, int y) {
        mExtPoint = new Point(x, y);
    }

    public void setmMidPoint(int x, int y) {
        this.mMidPoint = new Point(x, y);
    }

    /**
     * Method to rotate the with the circle center and get the points on the circle
     * Once for inner circle and once for outer circle
     *
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

    private RectF rotateRect(float centerX, float centerY){
        mRotateTransform.setRotate(6, centerX, centerY);
        RectF r = new RectF(0,0, 5,10);
       /* float[] pts = new float[2];

        pts[0] = point.x;
        pts[1] = point.y;

        mRotateTransform.mapPoints(pts);
*/
        mRotateTransform.mapRect(r, rectF);
        return rectF;

    }

    private void setUpRect(Point leftTop, Point bottomRight){

        rectF = new RectF(leftTop.x + 3f, leftTop.y + 3f, bottomRight.x + 3f, bottomRight.y + 3f);
    }

    /**
     * Setup the points on circle and shape drawables for once on onDraw method.
     */
    private void setup() {

        setmInPoint((int) (cX), (int) (cY - innerCircleRadius));
        setmExtPoint((int) (cX), (int) (cY - outerCircleRadius));
        setmMidPoint((int) (cX), (int) (cY - middleCircleRadius));

        setUpRect(mInPoint, mExtPoint);

        for (int i = 0; i < 60; i++) {

            CustomDrawable c = new CustomDrawable(mInPoint, mMidPoint, mExtPoint);
            //CustomDrawable c = new CustomDrawable(rectF);

            mScaleAnimation = new ScaleAnimation(0.9f,
                    1.9f,
                    0.9f,
                    1.9f,
                    mMidPoint.x,
                    mMidPoint.y);

            mScaleAnimation.setDuration(400);
            mScaleAnimation.setRepeatMode(Animation.REVERSE);
            mScaleAnimation.setRepeatCount(1);
            mScaleAnimation.setFillEnabled(true);
            mScaleAnimation.setFillAfter(false);
            mScaleAnimation.setStartOffset(300);

            mScaleAnimation.setAnimationListener(this);
            AnimateDrawable ad = new AnimateDrawable(c, mScaleAnimation);
            mAnimateDrawables.add(ad);

            mInPoint = rotate(mInPoint, cX, cY);
            mExtPoint = rotate(mExtPoint, cX, cY);
            mMidPoint = rotate(mMidPoint, cX, cY);
            //rectF.offsetTo(mInPoint.x, mInPoint.y);
            //setUpRect(mInPoint, mExtPoint);
            //rectF = rotateRect(cX, cY);
        }
    }

    /**
     * Get the center of the circle. Center is the same for inner and outer circle. (Concentric.)
     *
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
        isAnimating = true;
        mAnimateDrawables.get(mCurrentPosition).startAnimation();
    }

    Matrix mMatrix = new Matrix();
    /**
     * Draw the list of custom shape drawables.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.concat(mMatrix);
        if (once && !isInEditMode()) {
            setup();
            once = false;
        }

        for (int i = 0; i < mAnimateDrawables.size(); i++) {
            if (i == mCurrentPosition) {
                mAnimateDrawables.get(i).draw(canvas);
            } else {
                mAnimateDrawables.get(i).getProxy().draw(canvas);
            }
        }

        canvas.translate(proxy.getX(), proxy.getY());
        proxy.draw(canvas);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (mCurrentPosition < 60 && mCurrentPosition >= 0)
            mCurrentPosition++;

        if (mCurrentPosition == 60)
            mCurrentPosition = 0;
        if (mCurrentPosition >= 0 && mCurrentPosition < 60)
            mAnimateDrawables.get(mCurrentPosition).startAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
