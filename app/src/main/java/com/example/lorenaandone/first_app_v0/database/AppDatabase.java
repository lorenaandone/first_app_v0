package com.example.lorenaandone.first_app_v0.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.dao.MovieDao;
import com.example.lorenaandone.first_app_v0.model.Movie;

/**
 * Created by lorena.andone on 22.03.2018.
 */
@Database(entities = {Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, AppDatabase.class, "movie_db").build();
        }
        return instance;
    }

    public abstract MovieDao movieDao();

}
