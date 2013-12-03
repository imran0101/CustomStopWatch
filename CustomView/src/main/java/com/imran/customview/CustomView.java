package com.imran.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by imranmohammed on 04/10/13.
 */
public class CustomView extends View {

    Paint mPaint;
    float left = 100, top = 100;
    int width = 200, height = 200;
    RectF mRectF;

    String[] mColorSet = {"Yellow", "Blue", "Red", "Dark Gray"};

    int mColors[] = {Color.YELLOW, Color.BLUE, Color.RED, Color.DKGRAY};
    int mColorPosition = 0;
    private OnColorChangeListener mColorChangedListener;

    public static interface OnColorChangeListener {
        public void onColorChanged(String color);
    }

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(10);
        mRectF = new RectF(left, top, left + width, top + height);
    }

    public void setOnColorChangedListener(OnColorChangeListener listener) {
        this.mColorChangedListener = listener;
    }

    /**
     * Attachment *
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * Drawing - draw anything*
     */
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mColors[mColorPosition]);
        canvas.drawRoundRect(mRectF, 6, 6, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * Touch Events *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x, y;

        // I could have used this .. mRectF.contains(x,y) ..
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                if (x >= left && x <= left + width) {
                    if (y >= top && y <= top + height) {
                        setBackgroundColor(mColors[mColorPosition]);
                        if (mColorChangedListener != null)
                            mColorChangedListener.onColorChanged(mColorSet[mColorPosition]);
                        mColorPosition++;
                        if (mColorPosition == mColors.length) mColorPosition = 0;
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                break;
        }

        return true;

    }
}
