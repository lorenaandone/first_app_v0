package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.service.ApiFactory;
import com.example.lorenaandone.first_app_v0.service.MovieService;
import com.example.lorenaandone.first_app_v0.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class MovieListViewModel extends AndroidViewModel{

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    PublishSubject<List<MovieViewModel>> movieListSubject = PublishSubject.create();
    PublishSubject<Boolean> isInternetOn = PublishSubject.create();

    private final MutableLiveData<MovieListEventType> taskCommand = new MutableLiveData<>();

    public MovieListViewModel(@NonNull Application application) {
        super(application);
    }

    public void checkInternetConnection(){

        Disposable checkInternetConnection = InternetConnection.getInstance()
                .observe(getApplication().getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    isInternetOn.onNext(aBoolean);
                },throwable -> Log.e("CHECK INTERNET", throwable.getMessage()));

        compositeDisposable.add(checkInternetConnection);

    }

    public PublishSubject<Boolean> getIsInternetOn() {
        return isInternetOn;
    }

    public void fetchMoviesList(){

        MovieService movieService = ApiFactory.getInstance().getMovieService();

        Disposable disposable = movieService.fetchTopRatedMovies(getApplication().getResources().getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesResponse -> updateMovieVMList(moviesResponse.getResults()),
                        throwable -> Log.e("GET DATA", throwable.getMessage()));


        compositeDisposable.add(disposable);
    }


    private void updateMovieVMList(List<Movie> movies){

        List<MovieViewModel> movieVMList = createModelViewModelList(movies);

        movieListSubject.onNext(movieVMList);
        movieListSubject.onComplete();
    }

    private List<MovieViewModel> createModelViewModelList(List<Movie> movies){

        List<MovieViewModel> list = new ArrayList<>();

        for(int i=0; i< movies.size(); i++){

            MovieViewModel movieVM = new MovieViewModel();
            Movie movie = movies.get(i);
            movieVM.movieName.set(movie.getTitle());
            movieVM.movieRating.set(String.valueOf(movie.getVoteAverage()));
            movieVM.movieOverview.set(movie.getOverview());

            int screenWidth = getApplication().getResources().getDisplayMetrics().widthPixels;
            String posterUrl = movie.getPosterUrl(screenWidth);
            movieVM.moviePosterUrl.set(posterUrl);

            list.add(movieVM);
        }
        return list;
    }

    public void unsubscribeFromObservables() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        compositeDisposable = null;
    }

    public PublishSubject<List<MovieViewModel>> getMovieList() {
        return movieListSubject;
    }

    public MutableLiveData<MovieListEventType> getTaskCommand() {
        return taskCommand;
    }

    public enum MovieListEventType{
        NO_INTERNET
    }
}
