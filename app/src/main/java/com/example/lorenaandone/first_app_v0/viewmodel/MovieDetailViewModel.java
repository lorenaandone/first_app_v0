package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.database.AppDatabase;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.service.ApiFactory;
import com.example.lorenaandone.first_app_v0.service.MovieService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

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

    private int currentMovieId;

    PublishSubject<Boolean> onFavoriteMovie = PublishSubject.create();

    private Disposable fetchMovie;
    private CompositeDisposable disposables = new CompositeDisposable();

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
        disposables.add(fetchMovie);
    }

    public void getMovieByIdFromDb(int movieId){
        AppDatabase appDB = AppDatabase.getInstance(getApplication().getApplicationContext());

        Disposable disposable = appDB.movieDao().getMovieById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie movie) throws Exception {
                        currentMovieId = movieId;
                        setupDetailInfo(movie);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("GET_MOVIE_BY_ID", throwable.getMessage());
                    }
                });

        disposables.add(disposable);
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

        if(disposables != null && !disposables.isDisposed())
            disposables.dispose();
    }

    public void onFavoriteClicked(){
    }


}
