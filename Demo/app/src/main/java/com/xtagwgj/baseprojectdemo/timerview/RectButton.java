package com.xtagwgj.baseprojectdemo.timerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xtagwgj.baseproject.utils.MeasureUtil;

/**
 * 圆角的按钮
 * Created by xtagwgj on 2017/7/1.
 */

public class RectButton extends android.support.v7.widget.AppCompatButton {

    //控件的样式
    private final GradientDrawable gradientDrawable = new GradientDrawable();

    private int focusColor = ThemeUtils.getThemePrimaryColor(getContext());

    private int solidColor = Color.WHITE;

    private int focusTextColor = Color.WHITE;

    private int solidTextColor = focusColor;

    //圆角半径
    private int radius = 24;

    public RectButton(Context context) {
        this(context, null);
    }

    public RectButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextColor(solidTextColor);
        //将Button的默认背景色改为白色
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setCornerRadius(MeasureUtil.dp2px(getContext(), radius));
        setBackground(gradientDrawable);
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width < height) {
            radius = height / 2;
        } else
            radius = width / 2;

        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                gradientDrawable.setColor(focusColor);
                setBackground(gradientDrawable);
                setTextColor(focusTextColor);
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:

                setTextColor(solidTextColor);
                gradientDrawable.setColor(solidColor);
                setBackground(gradientDrawable);

                break;

            default:
                break;

        }

        return super.onTouchEvent(event);
    }

}
