package com.example.lorenaandone.first_app_v0.service;


import android.databinding.ObservableField;

import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.model.MoviesResponse;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lorena.andone on 15.03.2018.
 */

public interface MovieService {

    @GET("movie/top_rated")
    Observable<MoviesResponse> fetchTopRatedMovies(@Query("api_key") String apiKey);
}
