package com.xtagwgj.baseprojectdemo.progressbutton;

import java.util.ArrayList;

/**
 * 颜色
 * Created by xtagwgj on 2017/6/30.
 */

public class ColourUtil {
    static ArrayList<Integer> colourArray = new ArrayList<Integer>();

    public static ArrayList<Integer> getColourArray() {
        colourArray.add(android.R.color.holo_blue_bright);
        colourArray.add(android.R.color.holo_blue_dark);
        colourArray.add(android.R.color.holo_blue_light);
        colourArray.add(android.R.color.holo_green_dark);
        colourArray.add(android.R.color.holo_green_light);
        colourArray.add(android.R.color.holo_orange_dark);
        colourArray.add(android.R.color.holo_orange_light);
        colourArray.add(android.R.color.holo_purple);
        colourArray.add(android.R.color.holo_red_dark);
        colourArray.add(android.R.color.holo_red_light);
        return colourArray;
    }
}
