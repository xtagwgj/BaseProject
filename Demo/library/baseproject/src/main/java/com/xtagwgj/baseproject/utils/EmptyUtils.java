package com.xtagwgj.baseproject.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 检查为空的工具类
 * Created by xtagwgj on 2017/6/24.
 */

public class EmptyUtils {
    private EmptyUtils() {
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

}