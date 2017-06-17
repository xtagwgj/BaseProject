package com.xtagwgj.baseproject.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * 吐司相关工具类
 * Created by xtagwgj on 2017/4/10.
 */

public class ToastUtils {

    private static Toast sToast;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static boolean isJumpWhenMore = false;

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 吐司初始化
     *
     * @param isJumpWhenMore 当连续弹出吐司时，是要弹出新吐司还是只修改文本内容
     *                       <p>{@code true}: 弹出新吐司<br>{@code false}: 只修改文本内容</p>
     *                       <p>如果为{@code false}的话可用来做显示任意时长的吐司</p>
     */
    public static void init(boolean isJumpWhenMore) {
        ToastUtils.isJumpWhenMore = isJumpWhenMore;
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortToastSafe(final Context mContext, final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(mContext, text, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShortToastSafe(final Context mContext, final @StringRes int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(mContext, resId, Toast.LENGTH_SHORT);
            }
        });
    }


    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongToastSafe(final Context mContext, final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(mContext, text, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongToastSafe(final Context mContext, final @StringRes int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(mContext, resId, Toast.LENGTH_LONG);
            }
        });
    }


    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortToast(Context mContext, CharSequence text) {
        showToast(mContext, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShortToast(Context mContext, @StringRes int resId) {
        showToast(mContext, resId, Toast.LENGTH_SHORT);
    }


    /**
     * 显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongToast(Context mContext, CharSequence text) {
        showToast(mContext, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongToast(Context mContext, @StringRes int resId) {
        showToast(mContext, resId, Toast.LENGTH_LONG);
    }


    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void showToast(Context mContext, @StringRes int resId, int duration) {
        showToast(mContext, mContext.getString(resId), duration);
    }


    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void showToast(Context mContext, CharSequence text, int duration) {
        if (isJumpWhenMore) cancelToast();
        if (sToast == null) {
            sToast = Toast.makeText(mContext, text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}