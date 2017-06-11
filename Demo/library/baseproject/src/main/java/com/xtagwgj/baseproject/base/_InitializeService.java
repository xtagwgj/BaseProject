package com.xtagwgj.baseproject.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * 初始化服务
 * Created by xtagwgj on 2017/2/6.
 * 记得在Manifest中进行注册
 */
public abstract class _InitializeService extends IntentService {

    public static final String TAG = _InitializeService.class.getSimpleName();

    protected static final String ACTION_INIT_WHEN_APP_CREATE = "com.xtagwgj.service.action.INIT";

    public _InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context,Class intentServiceClass) {
        Intent intent = new Intent(context, intentServiceClass);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        initLog();

        initNet();

        initPush();

        initHttp();

        initLocalDataBase();

        initLoadingLayout();

        initOther();
    }

    protected abstract void initHttp();

    protected abstract void initLocalDataBase();

    protected abstract void initLog();

    protected abstract void initPush();

    protected abstract void initNet();

    protected abstract void initLoadingLayout();

    protected abstract void initOther();

}