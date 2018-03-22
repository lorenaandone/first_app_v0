package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.service.ApiFactory;
import com.example.lorenaandone.first_app_v0.service.MovieService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lorena.andone on 21.03.2018.
 */

public class MovieDetailViewModel extends AndroidViewModel{

    public final ObservableField<String> movieTitle = new ObservableField<>();
    public final ObservableField<String> overview = new ObservableField<>();
    public final ObservableField<String> imageUrl = new ObservableField<>();
    public final ObservableField<String> posterUrl = new ObservableField<>();
    public final ObservableField<String> rating = new ObservableField<>();
    public final ObservableField<String> releaseDate = new ObservableField<>();

    private Disposable fetchMovie;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchMovieById(int movieId){

        MovieService movieService = ApiFactory.getInstance().getMovieService();

        fetchMovie = movieService
                .fetchMovie(movieId, getApplication().getResources().getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie movie) throws Exception {
                        setupDetailInfo(movie);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("FETCH_MOVIE", throwable.getMessage());
                    }
                });
    }

    private void setupDetailInfo(Movie movie){

        movieTitle.set(movie.getTitle());
        overview.set(movie.getOverview());
        int screenWidth = getApplication().getResources().getDisplayMetrics().widthPixels;
        imageUrl.set(movie.getBackdropUrl(screenWidth));
        posterUrl.set(movie.getPosterUrl(screenWidth));
        rating.set(movie.getVoteAverage().toString());
        releaseDate.set(movie.getReleaseDate());
    }

    public void unsubscribeFromObservable(){

        if(fetchMovie != null && !fetchMovie.isDisposed())
            fetchMovie.dispose();
    }
}
