package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.model.MoviesResponse;
import com.example.lorenaandone.first_app_v0.service.ApiFactory;
import com.example.lorenaandone.first_app_v0.service.MoviesService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class MovieListViewModel extends AndroidViewModel{

    private List<Movie> movieList;

    private static final String API_KEY = "af9f5802e8ea91da04ef4f18a41ebeeb";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public MovieListViewModel(@NonNull Application application) {
        super(application);
        movieList = new ArrayList<>();
    }

    public void fetchMoviesList(){

        MoviesService moviesService = ApiFactory.create();
        Disposable disposable = moviesService.fetchTopRatedMovies(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoviesResponse>() {
                    @Override
                    public void accept(MoviesResponse moviesResponse) throws Exception {
                        System.out.println("<<<<<<<<<<<<<< inside accept ");
                        System.out.println("<<<<<<<<<<<<<< inside accept test data " + moviesResponse.getTotalPages());

                        updateUserDataList(moviesResponse.getResults());
                    }
                }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i("GET DATA", "DOES NOT reached accept");
            }
        });


        compositeDisposable.add(disposable);
    }

    private void updateUserDataList(List<Movie> movies){
//        movieList.clear();
        movieList.addAll(movies);
        if(movies != null && movies.size() > 00)
            System.out.println("<<<<<<<<<<<<<< view model test --- " + movies.get(0).getOriginalTitle());
        else
            System.out.println("<<<<<<<<<<<<<< view model test --- no data hoere");
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}
