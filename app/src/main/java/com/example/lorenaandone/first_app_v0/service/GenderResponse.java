package com.example.lorenaandone.first_app_v0.service;

import com.example.lorenaandone.first_app_v0.model.MovieGenre;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lorena.andone on 04.04.2018.
 */

public class GenderResponse {

    @SerializedName("genres")
    List<MovieGenre> genres;

    public List<MovieGenre> getResponses() {
        return genres;
    }

    public void setResponses(List<MovieGenre> responses) {
        this.genres = responses;
    }
}
