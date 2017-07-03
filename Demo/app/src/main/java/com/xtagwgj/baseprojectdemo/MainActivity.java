package com.xtagwgj.baseprojectdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;

import com.xtagwgj.baseproject.base._BaseActivity;
import com.xtagwgj.baseproject.utils.EmptyUtils;
import com.xtagwgj.baseproject.utils.LogUtils;
import com.xtagwgj.baseprojectdemo.timerview.RectButton;
import com.xtagwgj.baseprojectdemo.timerview.TimerButton;
import com.xtagwgj.baseprojectdemo.timerview.TimerProgressView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends _BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        TimerButton timerButton = (TimerButton) findViewById(R.id.timerButton);
        timerButton.setProgressSizePx(12);
        timerButton.setProgressColor(getResources().getColor(R.color.colorAccent));

        RectButton button = timerButton.getRectButton();
//        button.setBackground(getResources().getDrawable(R.mipmap.ic_launcher));
        button.setText("233123");
        button.setGravity(Gravity.CENTER);
        button.setTextColor(getResources().getColor(android.R.color.white));

        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_menu_search);

        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        button.setCompoundDrawables(drawable, null, null, null);


//        button.setStateDrawableByColorRes(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);


        button.setStateDrawable(
                getResources().getDrawable(R.mipmap.ic_launcher),
                null,
                null);


        timerButton.setProgressListener(new TimerProgressView.OnProgressListener() {
            @Override
            public void onProgressStart() {
                LogUtils.e("onProgressStart");
            }

            @Override
            public void onProgressUpdate(float progress) {
//                LogUtils.e("onProgressUpdate:" + progress);
            }

            @Override
            public void onProgressCancel() {
                LogUtils.e("onProgressCancel");
            }

            @Override
            public void onProgressCompleted() {
                LogUtils.e("onProgressCompleted");
            }
        });


    }

    @Override
    public void initEventListener() {
        Map<String, String> map = new HashMap<>();
        EmptyUtils.isEmpty(map);

    }

}