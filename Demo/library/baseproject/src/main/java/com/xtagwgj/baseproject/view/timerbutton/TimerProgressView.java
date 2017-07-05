package com.xtagwgj.baseproject.view.timerbutton;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.xtagwgj.baseproject.utils.LogUtils;

/**
 * 带时间限制的进度条视图
 * Created by xtagwgj on 2017/7/1
 */
public class TimerProgressView extends View {

    public static final String TAG = TimerProgressView.class.getSimpleName();

    private boolean isDebug = true;

    //最开始的 最小的进度数
    private float mStartingProgress;

    //总的进度数
    private float mTotalProgress;

    //当前的进度数
    private float mCurrentProgress;

    //当前要运动到的进度数
    private float mTargetProgress;

    //动画的时间
    private int mAnimationDuration;

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

    //是否显示虚拟的路径
    private boolean showVirtualPath;

    private OnProgressListener mListener;

    public TimerProgressView(Context context) {
        this(context, null);
    }

    public TimerProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mStartingProgress = 0;
        mCurrentProgress = 0;
        mTargetProgress = 0;
        mProgressSize = 8;
        mTotalProgress = 100;
        mAnimationDuration = 640;

        if (isInEditMode()) {
            mCurrentProgress = 20;
            mTargetProgress = 100;
        }

        followProgress = false;

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(ThemeUtils.getThemePrimaryColor(context));
        mProgressPaint.setStrokeWidth(mProgressSize * 2);

        mSplitPaint = new Paint();
        mSplitPaint.setAntiAlias(true);
        mSplitPaint.setStyle(Paint.Style.FILL);
        mSplitPaint.setColor(Color.parseColor("#666666"));

        mLinePathPaint = new Paint();
        mLinePathPaint.setAntiAlias(true);
        mLinePathPaint.setStyle(Paint.Style.FILL);
        mLinePathPaint.setColor(Color.parseColor("#dcdcdc"));
        mProgressPaint.setStrokeWidth(mProgressSize * 2);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSizePx = MeasureSpec.getSize(widthMeasureSpec);
        int heightSizePx = MeasureSpec.getSize(heightMeasureSpec);

        if (isDebug) {
            LogUtils.e(TAG, "width=" + widthSizePx);
            LogUtils.e(TAG, "height=" + heightSizePx);
        }

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

            if (showVirtualPath)
                //画虚拟的进度条的背景
                canvas.drawRoundRect(roundProgressBounds, roundRadius, roundRadius, mLinePathPaint);

            if (isHorizontal) {
                drawHorizontalProgress(canvas);
            } else {
                drawVerticalProgress(canvas);
            }

            if (!isDebug)
                //画分割线
                canvas.drawRoundRect(roundSplitBounds,
                        roundRadius - mProgressSize, roundRadius - mProgressSize, mSplitPaint);
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
            canvas.drawRect(minX, 0,
                    minX + currentAngel / lineAngel * longLineHeight, roundRadius, mProgressPaint);

        } else {
            canvas.drawRect(minX, 0, minX + longLineHeight / 2, roundRadius, mProgressPaint);

        }

        //未超过顶部右边的线段
        if (currentAngel <= lineAngel / 2)
            return;

        //画右方圆弧
        if (currentAngel <= 180 - lineAngel / 2) {

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

            canvas.drawRect(maxX - (currentAngel - circleAngel - lineAngel / 2) / lineAngel * longLineHeight,
                    roundRadius,
                    maxX,
                    roundRadius * 2, mProgressPaint);

        } else {
            canvas.drawRect(roundRadius, roundRadius , maxX, roundRadius* 2, mProgressPaint);
        }

        //未超过底部的线段
        if (currentAngel <= 360 - lineAngel / 2 - circleAngel) {
            return;
        }

        //画左方的圆弧
        if (currentAngel <= 360 - lineAngel / 2) {
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
        canvas.drawRect(roundRadius, 0,
                roundRadius + (lineAngel / 2 - 360 + currentAngel) / lineAngel * longLineHeight,
                roundRadius, mProgressPaint);


    }

    /**
     * 画垂直的进度条
     */
    private void drawVerticalProgress(Canvas canvas) {
        float currentAngel = mCurrentProgress * 360 / mTotalProgress;

        //顶部1／4圆
        if (currentAngel < circleAngel / 2) {
            canvas.drawArc(progressCircleBounds,
                    -90,
                    currentAngel / circleAngel * 180,
                    true, mProgressPaint);

        } else {
            canvas.drawArc(progressCircleBounds,
                    -90,
                    90,
                    true, mProgressPaint);
        }

        //未超过顶部1／4圆
        if (currentAngel <= circleAngel / 2)
            return;

        //右部直线
        if (currentAngel < lineAngel + circleAngel / 2) {
            canvas.drawRect(roundRadius, roundRadius, 2 * roundRadius,
                    roundRadius + (currentAngel - circleAngel / 2) / lineAngel * longLineHeight, mProgressPaint);
        } else {
            canvas.drawRect(roundRadius, roundRadius, 2 * roundRadius, roundRadius + longLineHeight, mProgressPaint);
        }

        //未超过右部直线
        if (currentAngel <= lineAngel + circleAngel / 2)
            return;

        //底部圆弧
        if (currentAngel < 360 - lineAngel - circleAngel / 2) {
            canvas.drawArc(otherCircleProgressBounds,
                    0,
                    (currentAngel - lineAngel - circleAngel / 2) / circleAngel * 180,
                    true, mProgressPaint);

        } else {
            canvas.drawArc(otherCircleProgressBounds,
                    0,
                    180,
                    true, mProgressPaint);
        }

        //未超过底部圆弧
        if (currentAngel <= 360 - lineAngel - circleAngel / 2)
            return;

        //左部直线
        if (currentAngel < 360 - circleAngel / 2) {
            canvas.drawRect(0, roundRadius + longLineHeight, roundRadius,
                    longLineHeight + roundRadius -
                            (currentAngel - lineAngel - circleAngel / 2 * 3) / lineAngel * longLineHeight,
                    mProgressPaint);


        } else {
            canvas.drawRect(0, roundRadius + longLineHeight, roundRadius,
                    roundRadius, mProgressPaint);


        }

        //未超过左部直线
        if (currentAngel <= 360 - circleAngel / 2)
            return;


        //画顶部剩下的半圆
        canvas.drawArc(progressCircleBounds,
                180,
                90 - (360 - currentAngel) / circleAngel * 180,
                true, mProgressPaint);
    }

    /**
     * 画圆形进度条
     */
    private void drawCircleProgress(Canvas canvas) {

        if (showVirtualPath)
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


    /**
     * 不需要重绘视图的初始化
     *
     * @param minProgress   最小进度
     * @param totalProgress 最大进度
     * @param progressTime  时间 毫秒
     */
    public void initProgressView(float minProgress, float totalProgress, int progressTime) {
        setStartingProgress(minProgress);
        setTotalProgress(totalProgress);
        setAnimTime(progressTime);
    }

    /**
     * 要重绘视图的初始化
     *
     * @param progressSizePx   进度条大小
     * @param progressColor    进度条颜色
     * @param splitColor       分割线颜色
     * @param virtualPathColor 虚拟路径颜色
     * @param showPath         是否显示虚拟路径
     */
    public void initProgressInvalid(int progressSizePx, int progressColor,
                                    int splitColor, int virtualPathColor, boolean showPath) {

        mProgressPaint.setColor(progressColor);
        mSplitPaint.setColor(splitColor);
        mLinePathPaint.setColor(virtualPathColor);

        this.mProgressSize = progressSizePx;
        this.showVirtualPath = showPath;
        invalidate();
    }

    public void setShowVirtualPath(boolean showVirtualPath) {
        this.showVirtualPath = showVirtualPath;
    }

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
        // 重新初始化动画
        if (isAnimating) {
            return;
        }

        if (valueAnim == null) {
            mCurrentProgress = mStartingProgress;
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
                    TimerProgressView.this.invalidate();
                    if (mListener != null) {
                        if (mTargetProgress == mCurrentProgress) {
                            mListener.onProgressCompleted();
                        } else
                            mListener.onProgressUpdate(mCurrentProgress / mTotalProgress * 100F);
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
                    valueAnim = null;
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

    /**
     * 设置进度条的颜色
     *
     * @param mColor 颜色值 res
     */
    public void setProgressColor(int mColor) {
        mProgressPaint.setColor(mColor);
        invalidate();
    }

    /**
     * 设置分割线的颜色
     *
     * @param mColor res
     */
    public void setSplitColorRes(int mColor) {
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

    public void setPathLineColor(int mPathLineColor) {
        mLinePathPaint.setColor(mPathLineColor);
        invalidate();
    }

    public void setStartingProgress(float mStartingProgress) {
        this.mStartingProgress = mStartingProgress;
    }

    public void setTotalProgress(float mTotalProgress) {
        this.mTotalProgress = mTotalProgress;
    }

    public float getmStartingProgress() {
        return mStartingProgress;
    }

    public float getmTotalProgress() {
        return mTotalProgress;
    }

    public float getmCurrentProgress() {
        return mCurrentProgress;
    }

    /*
                * Interface callbacks
                * */
    public void setProgressListener(OnProgressListener mListener) {
        this.mListener = mListener;
    }

    public interface OnProgressListener {
        void onProgressStart();

        void onProgressUpdate(@FloatRange(from = 0F, to = 100F) float progressPercent);

        void onProgressCancel();

        void onProgressCompleted();
    }
}