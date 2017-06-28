package com.xtagwgj.baseprojectdemo;

import com.xtagwgj.baseproject.utils.EmptyUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkEmpty() throws Exception {
        assertTrue(EmptyUtils.isEmpty(new ArrayList()));
        assertTrue(EmptyUtils.isEmpty(new LinkedList()));

        assertTrue(EmptyUtils.isEmpty(new HashSet()));
        assertTrue(EmptyUtils.isEmpty(new LinkedHashSet()));
        assertTrue(EmptyUtils.isEmpty(new ConcurrentSkipListSet()));

        Map<String, Object> map = new HashMap();
        assertTrue(EmptyUtils.isEmpty(map));

        assertFalse(EmptyUtils.isEmpty("12"));
        assertFalse(EmptyUtils.isEmpty(new String("12")));
        assertTrue(EmptyUtils.isEmpty(new String("")));
        StringBuffer stringBuffer = new StringBuffer();
        assertTrue(EmptyUtils.isEmpty(stringBuffer));
        StringBuilder stringBuilder = new StringBuilder();
        assertTrue(EmptyUtils.isEmpty(stringBuilder));
    }
}