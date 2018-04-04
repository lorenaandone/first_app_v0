package com.example.lorenaandone.first_app_v0.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.dao.GenreDao;
import com.example.lorenaandone.first_app_v0.database.AppDatabase;
import com.example.lorenaandone.first_app_v0.model.MovieGenre;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lorena.andone on 26.03.2018.
 */

public class MovieLocalService extends Service {

    private IBinder localIBinder = new LocalBinder();
    private AppDatabase appDatabase;
    private MovieService movieService;
    private String api_key;

    private int initialInterval = 0;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PublishSubject<Integer> countPublish = PublishSubject.create();


    @Override
    public void onCreate() {
        super.onCreate();

        appDatabase = AppDatabase.getInstance(getApplicationContext());
        movieService = ApiFactory.getInstance().getMovieService();
        api_key = getApplication().getResources().getString(R.string.api_key);

        getMovieCountFromDb();
        getMovieGenres();


        countPublish.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if(integer > 0)
                    initialInterval = 5*60*1000;
                else
                    initialInterval = 0;
                Log.i("SERVICE", "initial interval from subscribe to count: " + initialInterval);
                getRetrofitData(initialInterval);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localIBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    private void getRetrofitData(int initialInterval){

        Disposable disposable = io.reactivex.Observable.interval(initialInterval,5*60*1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(integer -> {
                    Log.i("SERVICE", "initial interval " + initialInterval);
                    return movieService.fetchTopRatedMovies(api_key);})
                .subscribe(new Consumer<MoviesResponse>() {
                    @Override
                    public void accept(MoviesResponse moviesResponse) throws Exception {

                        appDatabase.movieDao().insertMovieList(moviesResponse.getResults());
//                        appDatabase.movieDao().insertMovieList(resultList);
                        Log.i("SERVICE", "insert movies into db");
                        Log.i("SERVICE", "get data from service");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("INSERT_DB", throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void getMovieCountFromDb(){

        Disposable getCount = appDatabase.movieDao().getMovieCount()
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        countPublish.onNext(integer);
//                        countPublish.onComplete();
                        Log.i("SERVICE", "movie nr in db: " + integer);
                    }
                });

        compositeDisposable.add(getCount);
    }

    private void getMovieGenres(){
        Disposable disposable = movieService.fetchMovieGenres(api_key)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<GenderResponse>() {
                    @Override
                    public void accept(GenderResponse genreResponse) throws Exception {

                        List<MovieGenre> genres = genreResponse.getResponses();
                        appDatabase.genreDao().insertAll(genres);

                        for (int i = 0; i < genres.size(); i++) {
                            Log.i("SERVCE", "movie genre: " + genres.get(i).getName());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("GET_GENRES", throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    public class LocalBinder extends Binder {

       public MovieLocalService getService(){
            return MovieLocalService.this;
        }

    }
}
