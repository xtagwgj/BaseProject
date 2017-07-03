package com.xtagwgj.baseprojectdemo.timerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
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

    //进度条运行的时间
    private int progressTime;

    //最小进度值
    private float minProgress;

    //最大进度值
    private float maxProgress;

    //进度条的颜色
    private int progressColor;

    //分割线的颜色
    private int splitColor;

    //虚拟路径的颜色
    private int virtualPathColor;

    //是否显示虚拟路径
    private boolean showVirtualPath;

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

        progressColor = typedArray.getColor(
                R.styleable.TimerButton_timerProgressColor,
                ThemeUtils.getThemePrimaryColor(getContext())
        );

        splitColor = typedArray.getColor(
                R.styleable.TimerButton_timerSplitColor,
                Color.parseColor("#666666")
        );

        virtualPathColor = typedArray.getColor(
                R.styleable.TimerButton_timerVirtualProgressColor,
                Color.parseColor("#dcdcdc")
        );

        showVirtualPath = typedArray.getBoolean(
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

        progressTime = typedArray.getInteger(
                R.styleable.TimerButton_timerProgressTime,
                1640
        );

        typedArray.recycle();


        progressView = (TimerProgressView) view.findViewById(R.id.progressView);
        progressView.initProgressView(minProgress, maxProgress, progressTime);
        progressView.initProgressInvalid(mProgressSizePx, progressColor, splitColor, virtualPathColor, showVirtualPath);

        rectButton = (RectButton) view.findViewById(R.id.normalButton);
        rectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setCurrentProgress(maxProgress, true);
            }
        });

        progressView.setProgressSize(mProgressSizePx);

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

    public void setmProgressSizePx(int mProgressSizePx) {
        this.mProgressSizePx = mProgressSizePx;
        progressView.setProgressSize(mProgressSizePx);
        initButton();
    }

    public void setmSplitSizePx(int mSplitSizePx) {
        this.mSplitSizePx = mSplitSizePx;
        initButton();
    }

    public void setProgressTime(int progressTime) {
        this.progressTime = progressTime;
        progressView.setAnimTime(progressTime);
    }

    public void setMinProgress(float minProgress) {
        this.minProgress = minProgress;
        progressView.setStartingProgress(minProgress);
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
        progressView.setTotalProgress(maxProgress);
    }

    public void setProgressColor( int progressColor) {
        this.progressColor = progressColor;
        progressView.setProgressColor(progressColor);
    }

    public void setSplitColor( int splitColor) {
        this.splitColor = splitColor;
        progressView.setSplitColorRes(splitColor);
    }

    public void setVirtualPathColor( int virtualPathColor) {
        this.virtualPathColor = virtualPathColor;
        progressView.setPathLineColor(virtualPathColor);
    }

    public void setShowVirtualPath(boolean showVirtualPath) {
        this.showVirtualPath = showVirtualPath;
    }

    public void setProgressListener(TimerProgressView.OnProgressListener onProgressListener) {
        progressView.setProgressListener(onProgressListener);
    }
}
