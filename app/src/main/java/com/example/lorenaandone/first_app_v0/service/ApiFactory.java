package com.example.lorenaandone.first_app_v0.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lorena.andone on 15.03.2018.
 */

public class ApiFactory {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static ApiFactory instance = null;

    private MovieService movieService;

    private ApiFactory(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();



        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    public static final ApiFactory getInstance(){
        if(instance == null){
            instance = new ApiFactory();
        }
        return instance;
    }

    public MovieService getMovieService() {
        return movieService;
    }
}
