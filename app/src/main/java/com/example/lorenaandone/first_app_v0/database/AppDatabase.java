package com.example.lorenaandone.first_app_v0.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.lorenaandone.first_app_v0.dao.FavouriteDao;
import com.example.lorenaandone.first_app_v0.dao.GenreDao;
import com.example.lorenaandone.first_app_v0.dao.MovieDao;
import com.example.lorenaandone.first_app_v0.model.Favourite;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.model.MovieGenre;

/**
 * Created by lorena.andone on 22.03.2018.
 */
@Database(entities = {Movie.class, Favourite.class, MovieGenre.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, AppDatabase.class, "movie_db")
                            .addMigrations(FROM_1_TO_2)
                            .addMigrations(FROM_2_TO_3)
                            .addMigrations(FROM_3_TO_4)
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

    static final Migration FROM_2_TO_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
              database.execSQL("ALTER TABLE Movie ADD COLUMN genreIds TEXT");
        }
    };

    static final Migration FROM_3_TO_4 = new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE MovieGenre (id INTEGER PRIMARY KEY NOT NULL, name TEXT)");
        }
    };

    public abstract MovieDao movieDao();

    public abstract FavouriteDao favouriteDao();

    public abstract GenreDao genreDao();

}
