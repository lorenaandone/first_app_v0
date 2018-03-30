package com.example.lorenaandone.first_app_v0.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.lorenaandone.first_app_v0.dao.FavouriteDao;
import com.example.lorenaandone.first_app_v0.dao.MovieDao;
import com.example.lorenaandone.first_app_v0.model.Favourite;
import com.example.lorenaandone.first_app_v0.model.Movie;

/**
 * Created by lorena.andone on 22.03.2018.
 */
@Database(entities = {Movie.class, Favourite.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, AppDatabase.class, "movie_db")
                            .addMigrations(FROM_1_TO_2)
                            .build();
        }
        return instance;
    }

    static final Migration FROM_1_TO_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Favourite (id INTEGER PRIMARY KEY NOT NULL, isFavourite INTEGER NOT NULL DEFAULT 0)");
        }
    };

    public abstract MovieDao movieDao();

    public abstract FavouriteDao favouriteDao();

}
