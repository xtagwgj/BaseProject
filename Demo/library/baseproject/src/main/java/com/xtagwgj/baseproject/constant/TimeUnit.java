package com.xtagwgj.baseproject.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 时间单位 相关常量
 * Created by xtagwgj on 2017/6/11.
 */

@IntDef({TimeUnit.MILLI, TimeUnit.SEC, TimeUnit.MIN, TimeUnit.HOUR, TimeUnit.DAY})
@Retention(RetentionPolicy.SOURCE)
public @interface TimeUnit {

    /**
     * 秒与毫秒的倍数
     */
    int MILLI = 1;
    /**
     * 秒与毫秒的倍数
     */
    int SEC = 1000;
    /**
     * 分与毫秒的倍数
     */
    int MIN = 60000;
    /**
     * 时与毫秒的倍数
     */
    int HOUR = 3600000;
    /**
     * 天与毫秒的倍数
     */
    int DAY = 86400000;
}