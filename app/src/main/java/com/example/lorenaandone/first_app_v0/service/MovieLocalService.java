package com.example.lorenaandone.first_app_v0.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.database.AppDatabase;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.model.MoviesResponse;
import com.example.lorenaandone.first_app_v0.model.TestMovieDataProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lorena.andone on 26.03.2018.
 */

public class MovieLocalService extends Service {

    private IBinder localIBinder = new LocalBinder();
    Disposable disposable;

    @Override
    public void onCreate() {
        super.onCreate();
        getRetrofitData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localIBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    public void getRetrofitData(){

        MovieService movieService = ApiFactory.getInstance().getMovieService();
        String api_key = getApplication().getResources().getString(R.string.api_key);
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());

        disposable = io.reactivex.Observable.interval(1,30*60*1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(integer -> {return  movieService.fetchTopRatedMovies(api_key);})
                .subscribe(new Consumer<MoviesResponse>() {
                    @Override
                    public void accept(MoviesResponse moviesResponse) throws Exception {

//                        List<Movie> resultList = moviesResponse.getResults();
//                        resultList.add(TestMovieDataProvider.generateTestMovie());

                        appDatabase.movieDao().insertMovieList(moviesResponse.getResults());
                        Log.i("INSERT_DB", "insert movies into db");
                        Log.i("RETROFIT", "get data from service");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("INSERT_DB", throwable.getMessage());
                    }
                });
    }

    public class LocalBinder extends Binder {

       public MovieLocalService getService(){
            return MovieLocalService.this;
        }

    }
}
