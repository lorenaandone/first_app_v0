package com.example.lorenaandone.first_app_v0.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by lorena.andone on 30.03.2018.
 */

@Entity
public class Favourite {

    @PrimaryKey
    private int id;

    private int isFavourite;

    public Favourite(int id, int isFavourite) {
        this.id = id;
        this.isFavourite = isFavourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }
}
