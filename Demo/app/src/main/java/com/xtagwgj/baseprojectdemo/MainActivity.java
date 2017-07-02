package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;
import android.view.View;

import com.xtagwgj.baseproject.base._BaseActivity;
import com.xtagwgj.baseproject.utils.EmptyUtils;
import com.xtagwgj.baseproject.utils.LogUtils;
import com.xtagwgj.baseprojectdemo.fabprogress.ProgressFabButton;
import com.xtagwgj.baseprojectdemo.fabprogress.ProgressView;
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

        final ProgressFabButton fab = (ProgressFabButton) findViewById(R.id.fab);

        fab.setCompletedListener(new ProgressView.OnProgressListener() {
            @Override
            public void onProgressStart() {
                LogUtils.e("onProgressStart");
            }

            @Override
            public void onProgressUpdate(float progress) {
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.startAnim(1, 100, 2000, true);
            }
        });

        final TimerProgressView progressView = (TimerProgressView) findViewById(R.id.progressButton);

        progressView.setProgressListener(new TimerProgressView.OnProgressListener() {
            @Override
            public void onProgressStart() {
                LogUtils.e("onProgressStart");
            }

            @Override
            public void onProgressUpdate(float progress) {
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

//        progressView.mTotalProgress = 100;
//
//        progressView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressView.mCurrentProgress = 0;
//                progressView.setAnimTime(10000);
//                progressView.setCurrentProgress(100, true);
//            }
//        });

    }

    @Override
    public void initEventListener() {
        Map<String, String> map = new HashMap<>();
        EmptyUtils.isEmpty(map);

    }

}