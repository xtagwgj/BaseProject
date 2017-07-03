package com.xtagwgj.baseprojectdemo.timerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.xtagwgj.baseprojectdemo.R;

/**
 * 计时的按钮
 * Created by xtagwgj on 2017/7/1.
 */

public class TimerButton extends FrameLayout {

    //按钮
    private RectButton rectButton;

    //进度条的视图
    private TimerProgressView progressView;

    //进度条尺寸
    private int mProgressSizePx;

    //分割线大小
    private int mSplitSizePx;

    //最小进度值
    private float minProgress;

    //最大进度值
    private float maxProgress;


    public TimerButton(@NonNull Context context) {
        this(context, null);
    }

    public TimerButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = inflate(getContext(), R.layout.view_timer, this);


        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.TimerButton, defStyleAttr, 0);

        int progressColor = typedArray.getColor(
                R.styleable.TimerButton_timerProgressColor,
                ThemeUtils.getThemePrimaryColor(getContext())
        );

        int splitColor = typedArray.getColor(
                R.styleable.TimerButton_timerSplitColor,
                Color.parseColor("#666666")
        );

        int virtualPathColor = typedArray.getColor(
                R.styleable.TimerButton_timerVirtualProgressColor,
                Color.parseColor("#dcdcdc")
        );

        Boolean showVirtualPath = typedArray.getBoolean(
                R.styleable.TimerButton_timerShowVirtual,
                true
        );

        mProgressSizePx = typedArray.getDimensionPixelSize(
                R.styleable.TimerButton_timerProgressSize,
                8
        );

        mSplitSizePx = typedArray.getDimensionPixelSize(
                R.styleable.TimerButton_timerSplitSize,
                8
        );

        minProgress = typedArray.getFloat(
                R.styleable.TimerButton_timerMinProgress,
                0
        );

        maxProgress = typedArray.getFloat(
                R.styleable.TimerButton_timerTotalProgress,
                100
        );

        Integer progressTime = typedArray.getInteger(
                R.styleable.TimerButton_timerProgressTime,
                1640
        );

        Integer drawablePadding = typedArray.getInteger(R.styleable.TimerButton_timerDrawablePadding, 16);

        typedArray.recycle();


        progressView = (TimerProgressView) view.findViewById(R.id.progressView);
        progressView.initProgressView(minProgress, maxProgress, progressTime);
        progressView.initProgressInvalid(mProgressSizePx, progressColor, splitColor, virtualPathColor, showVirtualPath);

        rectButton = (RectButton) view.findViewById(R.id.normalButton);
        rectButton.setCompoundDrawablePadding(drawablePadding);
        rectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setCurrentProgress(maxProgress, true);
            }
        });

        initButton();
    }

    private void initButton() {

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rectButton.getLayoutParams();
        int margins = mProgressSizePx + mSplitSizePx;
        layoutParams.setMargins(margins, margins, margins, margins);
        rectButton.setLayoutParams(layoutParams);

    }

    //获取button
    public RectButton getRectButton() {
        return rectButton;
    }

    /**
     * 设置drawable的外边距
     */
    public void setCompoundDrawablePadding(int paddingPx) {
        rectButton.setCompoundDrawablePadding(paddingPx);
        rectButton.invalidate();
    }

    /**
     * 设置进度的的尺寸
     *
     * @param mProgressSizePx px值
     */
    public void setProgressSizePx(@IntRange(from = 0) int mProgressSizePx) {
        this.mProgressSizePx = mProgressSizePx;
        progressView.setProgressSize(mProgressSizePx);
        initButton();
    }

    /**
     * 设置分割线的尺寸
     *
     * @param mSplitSizePx px 值
     */
    public void setSplitSizePx(@IntRange(from = 0) int mSplitSizePx) {
        this.mSplitSizePx = mSplitSizePx;
        initButton();
    }

    /**
     * 设置进度条运行的时间
     *
     * @param progressTime 时间，毫秒
     */
    public void setProgressTime(@IntRange(from = 0) int progressTime) {
        progressView.setAnimTime(progressTime);
    }

    /**
     * 最小的进度值
     *
     * @param minProgress 最小进度值
     */
    public void setMinProgress(@FloatRange(from = 0) float minProgress) {
        progressView.setStartingProgress(minProgress);
    }

    /**
     * 最大进度
     *
     * @param maxProgress 最大进度
     */
    public void setMaxProgress(float maxProgress) {
        progressView.setTotalProgress(maxProgress);
    }

    /**
     * 进度条的颜色
     *
     * @param progressColor 颜色
     */
    public void setProgressColor(int progressColor) {
        progressView.setProgressColor(progressColor);
    }

    /**
     * 分割线的颜色
     *
     * @param splitColor 颜色
     */
    public void setSplitColor(int splitColor) {
        progressView.setSplitColorRes(splitColor);
    }

    /**
     * 虚拟进度条的颜色
     *
     * @param virtualPathColor 颜色
     */
    public void setVirtualPathColor(int virtualPathColor) {
        progressView.setPathLineColor(virtualPathColor);
    }

    /**
     * 是否显示虚拟进度条
     *
     * @param show 是否显示
     */

    public void setShowVirtualPath(boolean show) {
        progressView.setShowVirtualPath(show);
    }

    /**
     * 回调
     */
    public void setProgressListener(TimerProgressView.OnProgressListener onProgressListener) {
        progressView.setProgressListener(onProgressListener);

    }
}
