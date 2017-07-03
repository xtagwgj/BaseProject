package com.xtagwgj.baseprojectdemo.timerview;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

/**
 * 圆角的按钮
 * Created by xtagwgj on 2017/7/1.
 */

public class RectButton extends android.support.v7.widget.AppCompatButton {

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
        setClickable(true);
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
