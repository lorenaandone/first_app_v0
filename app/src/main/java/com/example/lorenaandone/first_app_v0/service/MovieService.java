package com.example.lorenaandone.first_app_v0.service;


import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lorena.andone on 15.03.2018.
 */

public interface MovieService {

    @GET("movie/top_rated")
    Observable<MoviesResponse> fetchTopRatedMovies(@Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Observable<GenderResponse> fetchMovieGenres(@Query("api_key") String apiKey);
}
