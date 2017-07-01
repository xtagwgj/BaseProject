package com.xtagwgj.baseprojectdemo.fabprogress;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.xtagwgj.baseproject.utils.LogUtils;

import static com.xtagwgj.baseprojectdemo.fabprogress.ThemeUtils.getThemePrimaryColor;

/**
 * 带进度条的按钮
 */
public class ProgressButtonView extends View {

    public static final String TAG = ProgressButtonView.class.getSimpleName();

    //最开始的 最小的进度数
    public float mStartingProgress;

    //中的进度数
    public float mTotalProgress;

    //当前的进度数
    public float mCurrentProgress;

    //当前要运动到的进度数
    private float mTargetProgress;

    //步长
    public float mStepSize;

    //进度条的颜色
    public int mProgressColor;

    //进度条里面的分割线的颜色
    public int mSplitColor;

    //虚拟的路径颜色
    public int mPathLineColor;

    //动画的时间
    public int mAnimationDuration;

    //动画是否正在执行
    private boolean isAnimating;

    //进度条paint
    private final Paint mProgressPaint;

    //分割线的paint
    private final Paint mSplitPaint;

    //路径的paint
    private final Paint mLinePathPaint;

    //进度条的矩形 圆：唯一 圆角矩形：左部或顶部
    private final RectF progressCircleBounds = new RectF();

    //进度条的矩形 圆角矩形 右部或底部
    private final RectF otherCircleProgressBounds = new RectF();

    //圆角矩形的进度条
    private final RectF roundProgressBounds = new RectF();

    //圆角矩形分割线
    private final RectF roundSplitBounds = new RectF();

    //分割线的矩形
    private final RectF splitBounds = new RectF();

    //进度条的大小，也就是分割线距离外边缘的大小
    private int mProgressSize;

    //分割线是否跟随进度条画
    private boolean followProgress;

    //是否是圆
    private boolean isCircle;

    //进度条是水平，只有非圆的情况下才有用
    private boolean isHorizontal;

    //圆角矩形的半径
    private int roundRadius;

    //长线的长度
    private int longLineHeight;

    //线的角度
    private float lineAngel;

    //圆的角度
    private float circleAngel;

    private OnProgressListener mListener;

    public ProgressButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Every attribute is initialized with a default value
        mProgressColor = getThemePrimaryColor(context);
        mSplitColor = Color.BLACK;
        mPathLineColor = Color.RED;

        mStartingProgress = 0;
        mCurrentProgress = 0;
        mTargetProgress = 0;
        mProgressSize = 8;
        mTotalProgress = 100;
        mStepSize = 10;
        mAnimationDuration = 640;

        if (isInEditMode()) {
            mCurrentProgress = 20;
            mTargetProgress = 60;
        }

        followProgress = false;

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeWidth(mProgressSize * 2);

        mSplitPaint = new Paint();
        mSplitPaint.setAntiAlias(true);
        mSplitPaint.setStyle(Paint.Style.FILL);
        mSplitPaint.setColor(mSplitColor);

        mLinePathPaint = new Paint();
        mLinePathPaint.setAntiAlias(true);
        mLinePathPaint.setStyle(Paint.Style.FILL);
        mLinePathPaint.setColor(mPathLineColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSizePx = MeasureSpec.getSize(widthMeasureSpec);
        int heightSizePx = MeasureSpec.getSize(heightMeasureSpec);

        LogUtils.e(TAG, "height=" + heightSizePx);
        LogUtils.e(TAG, "width=" + widthSizePx);

        isCircle = widthSizePx == heightSizePx;

        //圆半径
        roundRadius = (heightSizePx < widthSizePx ? heightSizePx : widthSizePx) / 2;

        if (isCircle) {
            //圆
            onMeasureCircleView(widthSizePx);
        } else {

            //进度条路径图
            roundProgressBounds.top = 0;
            roundProgressBounds.bottom = heightSizePx;
            roundProgressBounds.left = 0;
            roundProgressBounds.right = widthSizePx;

            roundSplitBounds.top = mProgressSize;
            roundSplitBounds.bottom = heightSizePx - mProgressSize;
            roundSplitBounds.left = mProgressSize;
            roundSplitBounds.right = widthSizePx - mProgressSize;

            if (widthSizePx > heightSizePx) {
                //横向圆角矩形 3条线段 2个半圆 一个圆角矩形
                isHorizontal = true;
                progressCircleBounds.top = 0;
                progressCircleBounds.bottom = heightSizePx;
                progressCircleBounds.left = 0;
                progressCircleBounds.right = roundRadius * 2;

                otherCircleProgressBounds.top = 0;
                otherCircleProgressBounds.bottom = heightSizePx;
                otherCircleProgressBounds.left = widthSizePx - heightSizePx;
                otherCircleProgressBounds.right = widthSizePx;

                longLineHeight = widthSizePx - 2 * roundRadius;

                double halfC = Math.PI * roundRadius + longLineHeight;
                //一个半圆占用的角度
                circleAngel = Double.valueOf(180F * Math.PI * roundRadius / halfC).floatValue();
                //一条长线占用的角度
                lineAngel = 180 - circleAngel;

            } else {
                isHorizontal = false;

                //纵向圆角矩形 2条线段 2个半圆
                progressCircleBounds.top = 0;
                progressCircleBounds.bottom = widthSizePx;
                progressCircleBounds.left = 0;
                progressCircleBounds.right = widthSizePx;

                otherCircleProgressBounds.top = heightSizePx - widthSizePx;
                otherCircleProgressBounds.bottom = heightSizePx;
                otherCircleProgressBounds.left = 0;
                otherCircleProgressBounds.right = widthSizePx;

                longLineHeight = heightSizePx - 2 * roundRadius;

                double halfC = Math.PI * roundRadius + longLineHeight;
                //一个半圆占用的角度
                circleAngel = Double.valueOf(180F * Math.PI * roundRadius / halfC).floatValue();
                //一条长线占用的角度
                lineAngel = 180 - circleAngel;
            }

            LogUtils.e(TAG, "lineAngel=" + lineAngel);

        }
    }

    /**
     * 计算圆需要的
     *
     * @param diameter 圆直径，就是控件的长或者宽，因为长宽相等，所有只需要一个值
     */
    private void onMeasureCircleView(int diameter) {
        progressCircleBounds.top = 0;
        progressCircleBounds.bottom = diameter;
        progressCircleBounds.left = 0;
        progressCircleBounds.right = diameter;

        //- mProgressSize是为了将分割的图形画在progress的内部
        splitBounds.top = mProgressSize;
        splitBounds.bottom = diameter - mProgressSize;
        splitBounds.left = mProgressSize;
        splitBounds.right = diameter - mProgressSize;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isCircle) {
            drawCircleProgress(canvas);
        } else {

            //画虚拟的进度条的背景
            canvas.drawRoundRect(roundProgressBounds, roundRadius, roundRadius, mLinePathPaint);


            if (isHorizontal) {
                drawHorizontalProgress(canvas);
            } else {
                drawVerticalProgress(canvas);
            }

            //画分割线
//            canvas.drawRoundRect(roundSplitBounds, roundRadius - mProgressSize, roundRadius - mProgressSize, mSplitPaint);
        }

    }

    /**
     * 画水平的进度条
     */
    private void drawHorizontalProgress(Canvas canvas) {
        float currentAngel = mCurrentProgress * 360 / mTotalProgress;

        //顶部右边线段的起始位置
        int minX = roundRadius + longLineHeight / 2;

        //画顶部右边的线段
        if (currentAngel < lineAngel / 2) {
            canvas.drawLine(minX, 0, minX + currentAngel / lineAngel * longLineHeight, 0, mProgressPaint);
        } else {
            canvas.drawLine(minX, 0, minX + longLineHeight / 2, 0, mProgressPaint);
        }

        //未超过顶部右边的线段
        if (currentAngel <= lineAngel / 2)
            return;

        //画右方圆弧
        if (currentAngel < 180 - lineAngel / 2) {

            canvas.drawArc(otherCircleProgressBounds,
                    -90,
                    ((currentAngel - lineAngel / 2) / circleAngel) * 180,
                    true, mProgressPaint);
        } else {
            canvas.drawArc(otherCircleProgressBounds,
                    -90,
                    180,
                    true, mProgressPaint);
        }

        //未超过右方圆弧
        if (currentAngel <= 180 - lineAngel / 2)
            return;

        //底部线段 从右忘左画
        int maxX = roundRadius + longLineHeight;

        //画底部线段
        if (currentAngel < 360 - lineAngel / 2 - circleAngel) {
            canvas.drawLine(maxX, roundRadius * 2, maxX - (currentAngel - circleAngel - lineAngel / 2) / lineAngel * longLineHeight, roundRadius * 2, mProgressPaint);

        } else {
            canvas.drawLine(maxX, roundRadius * 2, roundRadius, roundRadius * 2, mProgressPaint);
        }

        //未超过底部的线段
        if (currentAngel <= 360 - lineAngel / 2 - circleAngel) {
            return;
        }

        //画左方的圆弧
        if (currentAngel < 360 - lineAngel / 2) {
            canvas.drawArc(progressCircleBounds,
                    90,
                    (currentAngel - lineAngel / 2 - 180) / circleAngel * 180,
                    true, mProgressPaint);

        } else {
            canvas.drawArc(progressCircleBounds,
                    90,
                    180,
                    true, mProgressPaint);
        }

        //未超过左方的圆弧
        if (currentAngel <= 360 - lineAngel / 2)
            return;

        //顶部左边的线段
        canvas.drawLine(roundRadius, 0, roundRadius + (lineAngel / 2 - 360 + currentAngel) / lineAngel * longLineHeight, 0, mProgressPaint);


    }

    /**
     * 画垂直的进度条
     */
    private void drawVerticalProgress(Canvas canvas) {


    }

    /**
     * 画圆形进度条
     */
    private void drawCircleProgress(Canvas canvas) {

        //画虚拟的进度条的背景
        canvas.drawArc(progressCircleBounds,
                0,
                360,
                true, mLinePathPaint);

        //画进度条，从-90度开始画
        canvas.drawArc(progressCircleBounds,
                -90 + mStartingProgress * 360 / mTotalProgress,
                mCurrentProgress * 360 / mTotalProgress,
                true, mProgressPaint);

        //画中间的分割圆
        canvas.drawArc(splitBounds,
                followProgress ? -90 + mStartingProgress * 360 / mTotalProgress : 0,
                followProgress ? mCurrentProgress * 360 / mTotalProgress : 360,
                true, mSplitPaint);
    }

    /*
    * Getters and Setters
    * */

    /**
     * 设置动画的时间
     *
     * @param millis 毫秒
     */
    public void setAnimTime(int millis) {
        this.mAnimationDuration = millis;
    }

    /**
     * 设置默认的动画时间
     */
    public void setDefaultAnimTime() {
        setAnimTime(640);
    }

    //值动画
    private ValueAnimator valueAnim;

    public void setCurrentProgress(float toProgress, boolean animate) {
        // If the view is animating no further actions are allowed
        if (isAnimating && valueAnim != null) {
            valueAnim.cancel();
            valueAnim = null;
        }

        this.mTargetProgress = toProgress > mTotalProgress ? mTotalProgress : toProgress;

        if (animate && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Animations are only available from API 11 and forth
            valueAnim = ValueAnimator.ofFloat(mCurrentProgress, mTargetProgress);
            valueAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnim.setDuration(mAnimationDuration);
            valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentProgress = (Float) animation.getAnimatedValue();
                    ProgressButtonView.this.invalidate();

                    if (mListener != null) {
                        if (mTargetProgress == mCurrentProgress)
                            mListener.onProgressCompleted();
                        else
                            mListener.onProgressUpdate(mCurrentProgress / mTotalProgress);
                    }
                }
            });
            valueAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimating = true;
                    if (mListener != null)
                        mListener.onProgressStart();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimating = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAnimating = false;
                    if (mListener != null)
                        mListener.onProgressCancel();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    isAnimating = true;
                }
            });
            valueAnim.start();

        } else {
            mCurrentProgress = mTargetProgress;
            invalidate();
            if (mListener != null && mCurrentProgress == mTargetProgress)
                mListener.onProgressCompleted();
        }

    }

    public void next(boolean animate) {
        setCurrentProgress(mCurrentProgress + mStepSize, animate);
    }


    /**
     * 设置进度条的颜色
     *
     * @param mColor 颜色值（默认主题的primary颜色）
     */
    public void setProgressColor(int mColor) {
        this.mProgressColor = mColor;
        mProgressPaint.setColor(mColor);
        invalidate();
    }

    /**
     * 设置分割线的颜色
     *
     * @param mColor 颜色值（默认白色）
     */
    public void setSplitColor(int mColor) {
        this.mSplitColor = mColor;
        mSplitPaint.setColor(mColor);
        invalidate();
    }

    /**
     * 设置进度条的大小
     *
     * @param pxSize 像素值的大小
     */
    public void setProgressSize(int pxSize) {
        this.mProgressSize = pxSize;
        invalidate();
    }

    /*
    * Interface callbacks
    * */
    public void setProgressListener(OnProgressListener mListener) {
        this.mListener = mListener;
    }

    public interface OnProgressListener {
        void onProgressStart();

        void onProgressUpdate(float progress);

        void onProgressCancel();

        void onProgressCompleted();
    }
}