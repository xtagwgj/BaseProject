package com.xtagwgj.baseproject.base;

import android.app.Application;

/**
 * initService指的是初始化继承字_InitializeService的服务
 * 必须要在application中初始化的服务就在Application中，不必须的就在service中初始化
 * Created by xtagwgj on 2017/4/10.
 */

public abstract class _MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initService();
    }

    protected abstract void initService();

//    /**
//     * 分包
//     *
//     * @param base
//     */
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

}