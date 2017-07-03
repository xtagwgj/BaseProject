package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xtagwgj.baseproject.base._BaseActivity;
import com.xtagwgj.baseproject.utils.EmptyUtils;
import com.xtagwgj.baseproject.widget.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends _BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

//        TimerButton timerButton = (TimerButton) findViewById(R.id.timerButton);
//        timerButton.setProgressSizePx(12);
//        timerButton.setProgressColor(getResources().getColor(R.color.colorAccent));
//
//        TimerTextView button = timerButton.getTimerText();
//        button.setText("331233");
//        button.setTextColor(getResources().getColor(android.R.color.black));
//
//        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
//        /// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        button.setCompoundDrawables(drawable, null, null, null);
//
//        button.setStateDrawableByColorRes(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
//
//
//        timerButton.setProgressListener(null);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_loading);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.loading).startAnimation(animation);
        findViewById(R.id.loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.cancel();
            }
        });

    }

    @Override
    protected void doBeforeSetContentView() {
        super.doBeforeSetContentView();
        StatusBarUtil.statusBarLightMode(this);
    }

    @Override
    public void initEventListener() {
        Map<String, String> map = new HashMap<>();
        EmptyUtils.isEmpty(map);

    }

}