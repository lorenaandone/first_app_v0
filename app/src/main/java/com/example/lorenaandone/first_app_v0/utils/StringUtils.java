package com.example.lorenaandone.first_app_v0.utils;

import java.util.List;

/**
 * Created by lorena.andone on 04.04.2018.
 */

public class StringUtils {

    public static String formatStringsList(List<String> list){

        String result = "";

        if(list != null && list.size() > 0) {
            result = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                result += ", " + list.get(i);
            }
        }
        return result;
    }
}
