package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;

import com.xtagwgj.baseproject.base._BaseActivity;
import com.xtagwgj.baseproject.utils.EmptyUtils;
import com.xtagwgj.baseproject.utils.LogUtils;
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
        timerButton.setmProgressSizePx(12);
        timerButton.setProgressColor(getResources().getColor(R.color.colorAccent));
        timerButton.setProgressListener(new TimerProgressView.OnProgressListener() {
            @Override
            public void onProgressStart() {
                LogUtils.e("onProgressStart");
            }

            @Override
            public void onProgressUpdate(float progress) {
                LogUtils.e("onProgressUpdate:" + progress);
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