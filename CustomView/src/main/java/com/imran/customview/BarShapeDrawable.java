package com.imran.customview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

/**
 * Created by imranmohammed on 28/09/13.
 */
public class BarShapeDrawable extends ShapeDrawable {

    Animation mAnimation;
    private Transformation mTransformation = new Transformation();
    Point mInPoint;
    Point mExtPoint;

    public BarShapeDrawable(Point inPoint, Point extPoint) {
        BarShape b = new BarShape(inPoint.x, inPoint.y, extPoint.x, extPoint.y);

        getPaint().setColor(Color.GRAY);
        getPaint().setStrokeWidth(10);
        getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        setShape(b);
        this.mInPoint = inPoint;
        this.mExtPoint = extPoint;
    }

    public void setAnimation(Animation anim) {
        this.mAnimation = anim;

        mAnimation.setRepeatCount(1);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatMode(Animation.REVERSE);
    }

    public void startAnimation(){
        mAnimation.startNow();
        invalidateSelf();
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {

        int sc = canvas.save();
        Animation anim = mAnimation;
        if (anim != null) {
            anim.getTransformation(
                    AnimationUtils.currentAnimationTimeMillis(),
                    mTransformation);
            canvas.concat(mTransformation.getMatrix());
        }
        getShape().draw(canvas, paint);
        canvas.restoreToCount(sc);
    }

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
