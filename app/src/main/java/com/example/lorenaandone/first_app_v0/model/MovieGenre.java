package com.example.lorenaandone.first_app_v0.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by lorena.andone on 04.04.2018.
 */

@Entity
public class MovieGenre {

    @PrimaryKey
    private int id;

    private String name;

    public MovieGenre() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
