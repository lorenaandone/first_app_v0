package com.example.lorenaandone.first_app_v0.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.lorenaandone.first_app_v0.model.MovieGenre;

import java.util.List;

/**
 * Created by lorena.andone on 04.04.2018.
 */

@Dao
public interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovieGenre> genresList);

    @Query("SELECT name FROM MovieGenre WHERE MovieGenre.id = :id")
    String getGenreNameById(int id);

}
