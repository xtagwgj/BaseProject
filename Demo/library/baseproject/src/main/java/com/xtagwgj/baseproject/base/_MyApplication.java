package com.xtagwgj.baseproject.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by xtagwgj on 2017/4/10.
 */

public abstract class _MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initService();
    }

    protected abstract void initService();


    /**
     * 分包
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
