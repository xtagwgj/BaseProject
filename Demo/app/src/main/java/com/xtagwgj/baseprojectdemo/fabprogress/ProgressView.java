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
 * Progress indicator behind the Floating Action Button.
 */
public class ProgressView extends View {

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

    //动画的时间
    public int mAnimationDuration;

    //动画是否正在执行
    private boolean isAnimating;

    //进度条paint
    private final Paint mProgressPaint;

    //分割线的paint
    private final Paint mSplitPaint;

    //进度条的矩形
    private final RectF progressBounds = new RectF();

    //分割线的矩形
    private final RectF splitBounds = new RectF();

    //进度条的大小，也就是分割线距离外边缘的大小
    private int mProgressSize;

    //分割线是否跟随进度条画
    private boolean followProgress;

    private OnProgressListener mListener;

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Every attribute is initialized with a default value
        mProgressColor = getThemePrimaryColor(context);
        mSplitColor = Color.WHITE;

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

        mSplitPaint = new Paint();
        mSplitPaint.setAntiAlias(true);
        mSplitPaint.setStyle(Paint.Style.FILL);
        mSplitPaint.setColor(mSplitColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        LogUtils.e("progress", "height=" + MeasureSpec.getSize(heightMeasureSpec) + ";width=" + MeasureSpec.getSize(widthMeasureSpec));

        progressBounds.top = 0;
        progressBounds.bottom = MeasureSpec.getSize(heightMeasureSpec);
        progressBounds.left = 0;
        progressBounds.right = MeasureSpec.getSize(widthMeasureSpec);

        splitBounds.top = mProgressSize;
        splitBounds.bottom = MeasureSpec.getSize(heightMeasureSpec) - mProgressSize;
        splitBounds.left = mProgressSize;
        splitBounds.right = MeasureSpec.getSize(widthMeasureSpec) - mProgressSize;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画进度条，从-90度开始画
        canvas.drawArc(progressBounds,
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
                    ProgressView.this.invalidate();
                    if (mTargetProgress == mCurrentProgress && mListener != null)
                        mListener.onProgressCompleted();
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

        void onProgressCancel();

        void onProgressCompleted();
    }
}