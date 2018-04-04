package com.example.lorenaandone.first_app_v0.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.lorenaandone.first_app_v0.model.Favourite;

/**
 * Created by lorena.andone on 30.03.2018.
 */

@Dao
public interface FavouriteDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(Favourite favourite);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Favourite favourite);

    @Query("SELECT isFavourite FROM favourite WHERE favourite.id = :id")
    int isFavourite(int id);

    @Query("DELETE FROM favourite WHERE favourite.id = :id")
    void deleteFavourite(int id);
}
