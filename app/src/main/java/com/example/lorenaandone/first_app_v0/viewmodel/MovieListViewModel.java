package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.database.AppDatabase;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.model.MoviesResponse;
import com.example.lorenaandone.first_app_v0.service.ApiFactory;
import com.example.lorenaandone.first_app_v0.service.MovieService;
import com.example.lorenaandone.first_app_v0.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class MovieListViewModel extends AndroidViewModel{

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    PublishSubject<List<MovieViewModel>> movieListSubject = PublishSubject.create();
    PublishSubject<Boolean> isInternetOn = PublishSubject.create();

    AppDatabase appDatabase;

    private final MutableLiveData<MovieListEventType> taskCommand = new MutableLiveData<>();

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application.getApplicationContext());
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


    public void  getMoviesFromService(){

        MovieService movieService = ApiFactory.getInstance().getMovieService();
        String api_key = getApplication().getResources().getString(R.string.api_key);

        Disposable disposable = io.reactivex.Observable.interval(1,30*60*1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .flatMap(integer -> {return  movieService.fetchTopRatedMovies(api_key);})
                    .subscribe(new Consumer<MoviesResponse>() {
                        @Override
                        public void accept(MoviesResponse moviesResponse) throws Exception {

                            appDatabase.movieDao().insertMovieList(moviesResponse.getResults());
                             taskCommand.postValue(MovieListEventType.UPDATE_DATA);
                             Log.i("INSERT_DB", "insert movies into db");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("INSERT_DB", throwable.getMessage());
                        }
                    });

        compositeDisposable.add(disposable);
    }

    public void getMovieDataFromDb(){

        Disposable disposable = appDatabase.movieDao().getMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Movie>>() {
                        @Override
                        public void accept(List<Movie> movies) throws Exception {
                            updateMovieVMList(movies);
                            Log.i("GET_DB", "get data from db");
                            System.out.println("<<<<<<<Test from getMovieFromDb " + movies.size());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("GET_DB", throwable.getMessage());
                        }
                    });
        compositeDisposable.add(disposable);
    }

    private void updateMovieVMList(List<Movie> movies){

        List<MovieViewModel> movieVMList = createMovieViewModelList(movies);

        movieListSubject.onNext(movieVMList);
        movieListSubject.onComplete();
    }

    private List<MovieViewModel> createMovieViewModelList(List<Movie> movies){

        List<MovieViewModel> list = new ArrayList<>();

        for(int i=0; i< movies.size(); i++){

            MovieViewModel movieVM = new MovieViewModel();
            Movie movie = movies.get(i);
            movieVM.movieId.set(movie.getId());
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
        NO_INTERNET, UPDATE_DATA
    }

    public void testDeleteFromDb() {
        AsyncTask.execute(new Runnable() {
                              @Override
                              public void run() {
                                  appDatabase.movieDao().testDelete();
                              }
                          }
        );
    }
}
