package com.xtagwgj.baseprojectdemo;

import android.os.Bundle;

import com.xtagwgj.baseproject.base._BaseActivity;
import com.xtagwgj.baseproject.utils.EmptyUtils;
import com.xtagwgj.baseproject.view.LoadingLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends _BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        LoadingLayout viewById = (LoadingLayout) findViewById(R.id.loadingLayout);

        viewById.setStatus(LoadingLayout.Empty);
    }

    @Override
    public void initEventListener() {
        Map<String, String> map = new HashMap<>();
        EmptyUtils.isEmpty(map);
    }

}