package com.example.lorenaandone.first_app_v0.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.lorenaandone.first_app_v0.model.Movie;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by lorena.andone on 22.03.2018.
 */

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieList(List<Movie> movies);

    @Query("SELECT * FROM Movie")
    Flowable<List<Movie>> getMovies();

    @Query("SELECT * FROM Movie WHERE Movie.id = :movieId")
    Maybe<Movie> getMovieById(int movieId);


}
