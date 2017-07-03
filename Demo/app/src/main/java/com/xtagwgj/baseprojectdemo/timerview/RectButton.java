package com.xtagwgj.baseprojectdemo.timerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * 圆角的按钮
 * Created by xtagwgj on 2017/7/1.
 */

public class RectButton extends android.support.v7.widget.AppCompatTextView {

    //控件的样式
    private final StateListDrawable stateListDrawable = new StateListDrawable();


    public RectButton(Context context) {
        this(context, null);
    }

    public RectButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setClickable(true);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 内容居中绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 获取TextView的Drawable对象，返回的数组长度应该是4，对应左上右下
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawable = drawables[0];
            if (drawable != null) {
                // 当左边Drawable的不为空时，测量要绘制文本的宽度
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawable.getIntrinsicWidth();
                // 计算总宽度（文本宽度 + drawablePadding + drawableWidth）
                float bodyWidth = textWidth + drawablePadding + drawableWidth;
                // 移动画布开始绘制的X轴
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            } else if ((drawable = drawables[1]) != null) {
                // 否则如果上边的Drawable不为空时，获取文本的高度
                Rect rect = new Rect();
                getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), rect);
                float textHeight = rect.height();
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawable.getIntrinsicHeight();
                // 计算总高度（文本高度 + drawablePadding + drawableHeight）
                float bodyHeight = textHeight + drawablePadding + drawableHeight;
                // 移动画布开始绘制的Y轴
                canvas.translate(0, (getHeight() - bodyHeight) / 2);
            }
        }
        super.onDraw(canvas);

    }

    public void setStateDrawable(int idNormalDrawable, int idPressedDrawable, int idFocusedDrawable) {
        Drawable normal = idNormalDrawable == -1 ? null : getContext().getResources().getDrawable(idNormalDrawable);
        Drawable pressed = idPressedDrawable == -1 ? null : getContext().getResources().getDrawable(idPressedDrawable);
        Drawable focus = idFocusedDrawable == -1 ? null : getContext().getResources().getDrawable(idFocusedDrawable);

        setStateDrawable(normal, pressed, focus);
    }

    public void setStateDrawable(Drawable normal, Drawable pressed, Drawable focus) {

        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, focus);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normal);
        stateListDrawable.addState(new int[]{}, normal);

        setBackground(stateListDrawable);
    }


    public void setStateDrawableByColorRes(int normalColorRes, int pressColorRes, int focusedColorRes) {
        setStateDrawable(
                getDrawableByColor(normalColorRes),
                getDrawableByColor(pressColorRes),
                getDrawableByColor(focusedColorRes)
        );
    }

    private Drawable getDrawableByColor(int colorRes) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(new CircleArcRectangleShape());
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(colorRes));
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);

        return shapeDrawable;
    }


}
