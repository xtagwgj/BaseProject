package com.xtagwgj.baseprojectdemo;

import android.content.Intent;
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

                startActivity(new Intent(MainActivity.this, TestActivity.class));

            }
        });

    }

    @Override
    protected void initStatusBar() {
        StatusBarUtil.statusBarLightMode(this);
    }

    @Override
    public void initEventListener() {
        Map<String, String> map = new HashMap<>();
        EmptyUtils.isEmpty(map);

    }

}