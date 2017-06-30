package com.xtagwgj.baseprojectdemo.fabprogress;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.xtagwgj.baseprojectdemo.R;

import static com.xtagwgj.baseprojectdemo.fabprogress.ThemeUtils.getThemeAccentColor;
import static com.xtagwgj.baseprojectdemo.fabprogress.ThemeUtils.getThemePrimaryColor;

/**
 * fabButton样式的进度条
 * Created by xtagwgj on 2017/6/30.
 */

public class ProgressFabButton extends FrameLayout {

    private FloatingActionButton mFab;
    private ProgressView mProgressView;

    private Drawable mFinalIcon;
    private OnClickListener mListener;

    private int mPrimaryColor;
    private int mAccentColor;
    private int mAccentColorLight;

    public ProgressFabButton(Context context) {
        this(context, null, 0);
    }

    public ProgressFabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressFabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = inflate(getContext(), R.layout.view_progress_fab, this);

        // View injection (ButterKnife has issues with libraries apparently)
        mFab = (FloatingActionButton) view.findViewById(R.id.pfFab);
        mProgressView = (ProgressView) view.findViewById(R.id.pfProgress);

        // Initializing color values
        mPrimaryColor = getThemePrimaryColor(context);
        mAccentColor = getThemeAccentColor(context);
        mAccentColorLight = ColorUtils.lighten(mAccentColor, 0.5f);

        // Loading attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ProgressFabButton, defStyleAttr, 0);

        if (a.hasValue(R.styleable.ProgressFabButton_pFabProgressIcon))
            setIcon(a.getDrawable(R.styleable.ProgressFabButton_pFabProgressIcon));

        if (a.hasValue(R.styleable.ProgressFabButton_pFabFinalIcon))
            setFinalIcon(a.getDrawable(R.styleable.ProgressFabButton_pFabFinalIcon));

        // Numeric values

        setStartingProgress(
                a.getFloat(R.styleable.ProgressFabButton_pFabStartingProgress, 0), false
        );

        setCurrentProgress(
                a.getFloat(R.styleable.ProgressFabButton_pFabCurrentProgress,
                        mProgressView.mStartingProgress),
                false
        );

        setTotalProgress(
                a.getFloat(R.styleable.ProgressFabButton_pFabTotalProgress, 100)
        );

        setStepSize(
                a.getFloat(R.styleable.ProgressFabButton_pFabStepSize, 10)
        );

        // Colors

        setProgressColor(
                a.getColor(R.styleable.ProgressFabButton_pFabCircleColor,
                        mPrimaryColor
                )
        );

        setFabColor(
                a.getColor(
                        R.styleable.ProgressFabButton_pFabRippleColor,
                        mAccentColor
                )
        );

        setRippleColor(
                a.getColor(
                        R.styleable.ProgressFabButton_pFabRippleColor,
                        mAccentColorLight
                )
        );

        a.recycle();


    }

//    @Override
//    public void onProgressStart() {
//
//    }
//
//    @Override
//    public void onProgressCancel() {
//
//    }
//
//    /*
//        * ({@link ProgressView}) will notify the ({@link ProgressFloatingActionButton}) once the
//        * progress has been completed through the ({@link ProgressView.OnProgressListener}) interface.
//        * */
//    @Override
//    public void onProgressCompleted() {
//        // If the progress is completed the FAB will change
//        if (mFinalIcon != null)
//            mFab.setImageDrawable(mFinalIcon);
//
//        mFab.setOnClickListener(mListener);
//    }

    /*
    * Getters and Setters
    * */

    public FloatingActionButton getFab() {
        return mFab;
    }

    public void setStartingProgress(float start, boolean isTargetProgress) {
        mProgressView.mStartingProgress = start;
        if (isTargetProgress)
            mProgressView.mCurrentProgress = start;
    }

    public void setCurrentProgress(float current, boolean animate) {
        mProgressView.setCurrentProgress(current, animate);
    }

    public void setTotalProgress(float total) {
        mProgressView.mTotalProgress = total;
    }

    public void setStepSize(float size) {
        mProgressView.mStepSize = size;
    }

    public void setIcon(Drawable icon) {
        if (icon != null)
            mFab.setImageDrawable(icon);
    }

    public void setFinalIcon(Drawable mFinalIcon) {
        this.mFinalIcon = mFinalIcon;
    }

    public void setProgressColor(int color) {
        mProgressView.setProgressColor(color);
    }

    public void setFabColor(int color) {
        mFab.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setRippleColor(int color) {
        mFab.setRippleColor(color);
    }

    public void setCompletedListener(ProgressView.OnProgressListener mListener) {
        mProgressView.setProgressListener(mListener);
    }

    public void setAnimTime(int millis) {
        mProgressView.setAnimTime(millis);
    }

    public void setDefaultAnimTime() {
        mProgressView.setDefaultAnimTime();
    }

    public void startAnim(float fromProgress, float toProgress, int millisSeconds, boolean animate) {
        setStartingProgress(fromProgress, true);
        setAnimTime(millisSeconds);
        setCurrentProgress(toProgress, animate);
    }

}
