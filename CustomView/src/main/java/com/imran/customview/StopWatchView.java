package com.imran.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by imranmohammed on 28/09/13.
 */
public class StopWatchView extends View implements Animation.AnimationListener {

    private final static int DURATION_REFRESH_OFFSET = 100;
    // inner circle radius and circumference.
    float innerCircleRadius, innerCircleCircumference;
    // outer circle radius and circumference
    float outerCircleRadius, outerCircleCircumference;
    // middle circle radius and circumference
    float middleCircleRadius, middleCircleCircumference;
    float scalePointRadius;
    // Center cX and cY
    float cX = 0;
    float cY = 0;
    //To set the drawables once.
    boolean once = true;
    //matrix used to transform and get the points on circle.
    Matrix mRotateTransform;
    //Point on inner circle
    PointF mInPoint;
    //Point on ext circle
    PointF mExtPoint;
    //Point on the middle circle
    PointF mMidPoint;
    PointF mScalePoint;
    /**
     * List of custom shape drawables.
     * 2 points are stored.
     * <p/>
     * Animation to be done on these drawables.
     */
    List<AnimateDrawable> mAnimateDrawables;
    CustomDrawable custom;
    ProxyDrawable proxy;
    AnimateDrawable a;
    Matrix mMatrix = new Matrix();
    boolean mPaused = false, mStarted = false;
    long mStartTime;
    long mElapsedTime;
    String mMinutes = "00", mSeconds = "00", mMilliSeconds = ".00";
    long mMinutesL, mSecondsL, mMilliSecondsL;
    String mTime = "00:00";
    Handler mHandler = new Handler();
    Paint mTextPaint;
    ArrayDeque<Integer> mAnimatePositions;
    Rect rect = new Rect();

    private int mCurrentPosition = 0;
    private Runnable mStartTimer = new Runnable() {
        @Override
        public void run() {
            mElapsedTime = System.currentTimeMillis() - mStartTime;
            startTicking(mElapsedTime);
            mHandler.postDelayed(this, DURATION_REFRESH_OFFSET);
        }
    };

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

            scalePointRadius = getResources().getDimension(R.dimen.scale_point_radius);

            mRotateTransform = new Matrix();

            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
            mTextPaint.setColor(getResources().getColor(android.R.color.holo_blue_dark));
            mTextPaint.setStrokeWidth(1);
            mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Thin.ttf"));

            mAnimateDrawables = new ArrayList<AnimateDrawable>();
            mAnimatePositions = new ArrayDeque<Integer>(2);
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
    private void setmInPoint(float x, float y) {
        this.mInPoint = new PointF(x, y);
    }

    /**
     * Set inital x and y on ext Circle
     * x = x
     * y = y1 - outerCircleRadius
     *
     * @param x
     * @param y
     */
    private void setmExtPoint(float x, float y) {
        this.mExtPoint = new PointF(x, y);
    }

    public void setmMidPoint(float x, float y) {
        this.mMidPoint = new PointF(x, y);
    }

    public void setScalePoint(float x, float y) {
        this.mScalePoint = new PointF(x, y);
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
    private PointF rotate(PointF point, float centerX, float centerY) {
        mRotateTransform.setRotate(6, centerX, centerY);
        float[] pts = new float[2];

        pts[0] = point.x;
        pts[1] = point.y;

        mRotateTransform.mapPoints(pts);
        return new PointF(pts[0], pts[1]);
    }

    /**
     * Setup the points on circle and shape drawables for once on onDraw method.
     */
    private void setup() {

        setmInPoint(cX, (cY - innerCircleRadius));
        setmExtPoint(cX, (cY - outerCircleRadius));
        setmMidPoint(cX, (cY - middleCircleRadius));
        setScalePoint(cX, cY - scalePointRadius);

        for (int i = 0; i < 60; i++) {

            CustomDrawable c;
            if (i == 0 || i == 15 || i == 30 || i == 45) {
                c = new CustomDrawable(true, mInPoint, mMidPoint, mExtPoint, mScalePoint);
            } else {
                c = new CustomDrawable(false, mInPoint, mMidPoint, mExtPoint, mScalePoint);
            }

            c.setColor(getResources().getColor(android.R.color.holo_blue_bright));
            AnimateDrawable ad = new AnimateDrawable(c);
            ad.getAnimation().setAnimationListener(this);
            mAnimateDrawables.add(ad);

            mInPoint = rotate(mInPoint, cX, cY);
            mExtPoint = rotate(mExtPoint, cX, cY);
            mMidPoint = rotate(mMidPoint, cX, cY);
            mScalePoint = rotate(mScalePoint, cX, cY);
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

        cX = getMeasuredWidth() / 2;
        cY = getMeasuredHeight() / 2;

        if (once && !isInEditMode()) {
            setup();
            once = false;
        }

    }

    /**
     * working on it.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start() {
        if (mPaused) {
            mStartTime = System.currentTimeMillis() - mElapsedTime;
            mPaused = false;
        } else {
            mStartTime = System.currentTimeMillis();
        }
        //mCurrentPosition = 0;
        //mAnimatePositions.add(mCurrentPosition);


        mStarted = true;
        mHandler.removeCallbacks(mStartTimer);
        mHandler.postDelayed(mStartTimer, 0);
    }

    public void pause() {
        mPaused = true;
        mStarted = false;
        mHandler.removeCallbacks(mStartTimer);
    }

    public void reset() {
        mPaused = false;
        mStarted = false;
        mHandler.removeCallbacks(mStartTimer);
        mTime = "00:00";
        mMilliSeconds = ".00";
        if (mAnimatePositions.size() > 0) {
            for (Integer i : mAnimatePositions) {
                mAnimateDrawables.get(i).reset(this);
            }
        }
        mAnimatePositions = new ArrayDeque<Integer>(2);
        invalidate();
    }

    public boolean isRunning() {
        return mStarted;
    }

    /**
     * Draw the list of custom shape drawables.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (!isInEditMode()) {
            for (int i = 0; i < mAnimateDrawables.size(); i++) {
                AnimateDrawable aa = mAnimateDrawables.get(i);

                if (mAnimatePositions.contains(i)) {
                    if (aa.hasEnded()) {
                        aa.reset(this);
                    }
                    aa.draw(canvas);
                } else {
                    aa.getProxy().draw(canvas);
                }
            }

            mTextPaint.setTextSize(140);

            float tX = cX - mTextPaint.descent();
            float tY = cY - ((mTextPaint.descent() + mTextPaint.ascent()) / 2);

            mTextPaint.getTextBounds(mTime, 0, 3, rect);

            canvas.drawText(mTime, tX, tY, mTextPaint);
            mTextPaint.setTextSize(40);
            canvas.drawText(mMilliSeconds, rect.right + tX + 20, rect.bottom + tY, mTextPaint);

            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private void startTicking(float time) {
        mMilliSecondsL = (long) time;
        mSecondsL = (long) ((time / 1000) % 60);
        mMinutesL = (long) (((time / 1000) / 60) % 60);

        mMilliSeconds = (mMilliSecondsL == 0) ? "00" : String.valueOf(mMilliSecondsL);
        mSeconds = (mSecondsL == 0) ? "00" : String.valueOf(mSecondsL);
        mMinutes = String.valueOf(mMinutesL);

        if (mMilliSeconds.length() <= 2)
            mMilliSeconds = "0" + mMilliSeconds;
        else if (mMilliSeconds.length() <= 1)
            mMilliSeconds = "00";

        if (mMilliSeconds.length() >= 3)
            mMilliSeconds = mMilliSeconds.substring(mMilliSeconds.length() -
                    3, mMilliSeconds.length() - 1);

        if (mSecondsL > 0 && mSecondsL < 10)
            mSeconds = "0" + mSeconds;

        if (mMinutesL >= 0 && mMinutesL < 10)
            mMinutes = "0" + mMinutes;

        mTime = mMinutes + ":" + mSeconds; // + mMilliSeconds
        mMilliSeconds = "." + mMilliSeconds;

        int index = (int) mSecondsL;
        mCurrentPosition = index;

        if (!mAnimatePositions.contains(index))
            mAnimatePositions.add(index);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (!mPaused)
            mAnimatePositions.poll();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
