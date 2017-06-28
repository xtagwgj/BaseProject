package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;
import android.view.View;

import com.xtagwgj.baseproject.base.AppManager;
import com.xtagwgj.baseproject.base._BaseActivity;
import com.xtagwgj.baseproject.utils.EmptyUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends _BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        findViewById(R.id.loadingLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().AppExit(MainActivity.this, true);
            }
        });

    }

    @Override
    public void initEventListener() {
        Map<String, String> map = new HashMap<>();
        EmptyUtils.isEmpty(map);

    }

}