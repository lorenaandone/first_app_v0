package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.database.AppDatabase;
import com.example.lorenaandone.first_app_v0.model.Favourite;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.model.MovieDetail;
import com.example.lorenaandone.first_app_v0.model.MovieGenre;
import com.example.lorenaandone.first_app_v0.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    public final ObservableField<Integer> favouriteButtonRes = new ObservableField<>();
    public final ObservableField<String> genres = new ObservableField<>();

    PublishSubject<Integer> onFavoriteMovie = PublishSubject.create();

    private CompositeDisposable disposables = new CompositeDisposable();

    AppDatabase appDB;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        appDB = AppDatabase.getInstance(getApplication().getApplicationContext());
    }

    public void getMovieByIdFromDb(int movieId){

        Disposable disposable = appDB.movieDao().getMovieById(movieId)
                .map(new Function<Movie, MovieDetail>() {

                    @Override
                    public MovieDetail apply(Movie movie) throws Exception {

                        int isFavourite = appDB.favouriteDao().isFavourite(movieId);
                        List<String> movieGenres = getGenresForMovie(movie);
                        for (int i = 0; i< movieGenres.size(); i++){
                            Log.i("GENRES", "movie genres: " + movieGenres.get(i));
                        }
                        return createMovieDetail(movie, isFavourite, movieGenres);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieDetail>() {
                    @Override
                    public void accept(MovieDetail movieDetail) throws Exception {
                            setupDetailInfo(movieDetail);
                    }
                });

        disposables.add(disposable);
    }

    private List<String> getGenresForMovie(Movie movie){

        List<String> genreResult = new ArrayList<>();
        List<Integer> movieGenres = movie.getGenreIds();

        for(int i=0; i< movieGenres.size(); i++){
            String name = appDB.genreDao().getGenreNameById(movieGenres.get(i));
            genreResult.add(name);
        }

        return genreResult;
    }

    private MovieDetail createMovieDetail(Movie movie, int isFavourite, List<String> genres){

        MovieDetail movieDetail = new MovieDetail();

        movieDetail.setTitle(movie.getTitle());
        int screenSize = getApplication().getResources().getDisplayMetrics().widthPixels;
        movieDetail.setBackdropUrl(movie.getBackdropUrl(screenSize));
        movieDetail.setPosterUrl(movie.getPosterUrl(screenSize));
        movieDetail.setOverview(movie.getOverview());
        movieDetail.setRating(movie.getVoteAverage().toString());
        movieDetail.setReleaseDate(movie.getReleaseDate());
        movieDetail.setIsFavourite(isFavourite);
        movieDetail.setMovieGenres(genres);

        return movieDetail;
    }

    private void setupDetailInfo(MovieDetail movieDetail){

        movieTitle.set(movieDetail.getTitle());
        overview.set(movieDetail.getOverview());
        imageUrl.set(movieDetail.getBackdropUrl());
        posterUrl.set(movieDetail.getPosterUrl());
        rating.set(movieDetail.getRating());
        releaseDate.set(movieDetail.getReleaseDate());

        if(movieDetail.getIsFavourite() == 0)
            favouriteButtonRes.set(R.drawable.ic_heart_outline);
        else
            favouriteButtonRes.set(R.drawable.ic_heart);

        genres.set(StringUtils.formatStringsList(movieDetail.getMovieGenres()));
    }

    public void unsubscribeFromObservable(){

        if(disposables != null && !disposables.isDisposed())
            disposables.dispose();
    }

    public void onFavoriteClicked(){

        if(favouriteButtonRes.get() == R.drawable.ic_heart_outline)
            favouriteButtonRes.set(R.drawable.ic_heart);
        else if (favouriteButtonRes.get() == R.drawable.ic_heart)
            favouriteButtonRes.set(R.drawable.ic_heart_outline);

        onFavoriteMovie.onNext(favouriteButtonRes.get());
    }

    public PublishSubject<Integer> getOnFavouriteObservable(){
        return onFavoriteMovie;
    }

    public void updateFavouriteStatus(int movieId, int isFavourite){

        Favourite favourite = new Favourite(movieId, isFavourite);
        appDB.favouriteDao().insert(favourite);
        Log.i("UPDATE", "Movie updated");

    }
}
