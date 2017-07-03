package com.xtagwgj.baseprojectdemo.timerview;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

/**
 * 自定义的圆角矩形，当宽高一致时变成圆形
 * Created by xtagwgj on 2017/7/3.
 *
 * 参考：android.graphics.drawable.shapes.RoundRectShape
 */

public class CircleArcRectangleShape extends Shape {
    private RectF mRect = new RectF();

    private float[] mOuterRadii;
    private RectF   mInset;
    private float[] mInnerRadii;

    private RectF mInnerRect;
    private Path mPath;    // this is what we actually draw

    public CircleArcRectangleShape() {
        mPath = new Path();
    }

    public CircleArcRectangleShape(float[] outerRadii, RectF inset,
                          float[] innerRadii) {
        if (outerRadii != null && outerRadii.length < 8) {
            throw new ArrayIndexOutOfBoundsException("outer radii must have >= 8 values");
        }
        if (innerRadii != null && innerRadii.length < 8) {
            throw new ArrayIndexOutOfBoundsException("inner radii must have >= 8 values");
        }
        mOuterRadii = outerRadii;
        mInset = inset;
        mInnerRadii = innerRadii;

        if (inset != null) {
            mInnerRect = new RectF();
        }
        mPath = new Path();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPath(mPath, paint);
    }

    @Override
    public void getOutline(Outline outline) {
        if (mInnerRect != null) return; // have a hole, can't produce valid outline

        float radius = 0;
        if (mOuterRadii != null) {
            radius = mOuterRadii[0];
            for (int i = 1; i < 8; i++) {
                if (mOuterRadii[i] != radius) {
                    // can't call simple constructors, use path
                    outline.setConvexPath(mPath);
                    return;
                }
            }
        }

        final RectF rect = rect();
        outline.setRoundRect((int) Math.ceil(rect.left), (int) Math.ceil(rect.top),
                (int) Math.floor(rect.right), (int) Math.floor(rect.bottom),
                radius);
    }

    @Override
    protected void onResize(float w, float h) {
        mRect.set(0, 0, w, h);
        initCircleArcRectangleRadius();

        RectF r = rect();
        mPath.reset();

        if (mOuterRadii != null) {
            mPath.addRoundRect(r, mOuterRadii, Path.Direction.CW);
        } else {
            mPath.addRect(r, Path.Direction.CW);
        }
        if (mInnerRect != null) {
            mInnerRect.set(r.left + mInset.left, r.top + mInset.top,
                    r.right - mInset.right, r.bottom - mInset.bottom);
            if (mInnerRect.width() < w && mInnerRect.height() < h) {
                if (mInnerRadii != null) {
                    mPath.addRoundRect(mInnerRect, mInnerRadii, Path.Direction.CCW);
                } else {
                    mPath.addRect(mInnerRect, Path.Direction.CCW);
                }
            }
        }
    }

    protected final RectF rect() {
        return mRect;
    }

    @Override
    public CircleArcRectangleShape clone() throws CloneNotSupportedException {
        CircleArcRectangleShape shape = (CircleArcRectangleShape) super.clone();
        shape.mOuterRadii = mOuterRadii != null ? mOuterRadii.clone() : null;
        shape.mInnerRadii = mInnerRadii != null ? mInnerRadii.clone() : null;
        shape.mInset = new RectF(mInset);
        shape.mInnerRect = new RectF(mInnerRect);
        shape.mPath = new Path(mPath);
        return shape;
    }

    private void initCircleArcRectangleRadius() {
        float outRadius = rect().height();
        float innerRadius = outRadius;//建议设置成与outRadius一致,可防止内外矩形覆盖异常
        float spaceBetOutAndInner = rect().height() / 2 - 0;//内外环的间距,这样设置会使得中间存在一根白线区域

        float[] outerRadii = {outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius};//左上x2,右上x2,右下x2,左下x2，注意顺序（顺时针依次设置）
        RectF inset = new RectF(spaceBetOutAndInner, spaceBetOutAndInner, spaceBetOutAndInner, spaceBetOutAndInner);
        float[] innerRadii = {innerRadius, innerRadius, innerRadius, innerRadius, innerRadius, innerRadius, innerRadius, innerRadius};//内矩形圆角半径

        mOuterRadii = outerRadii;
        mInset = inset;
        mInnerRadii = innerRadii;

        if (inset != null) {
            mInnerRect = new RectF();
        }
    }

}
