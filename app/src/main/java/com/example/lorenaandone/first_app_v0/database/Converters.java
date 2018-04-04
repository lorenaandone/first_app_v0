package com.example.lorenaandone.first_app_v0.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by lorena.andone on 04.04.2018.
 */

public class Converters {

    @TypeConverter
    public static ArrayList<Integer> fromStringToArrayList(String value){

        Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayListToString(ArrayList<Integer> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
