package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.database.AppDatabase;
import com.example.lorenaandone.first_app_v0.model.Favourite;
import com.example.lorenaandone.first_app_v0.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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

    public void getMovieDataFromDb(){

        Disposable disposable = appDatabase.movieDao().getMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Movie>>() {
                        @Override
                        public void accept(List<Movie> movies) throws Exception {
                            updateMovieVMList(movies);
                            Log.i("GET_DB", "get data from db - in list view model");
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

    public void onShowFavouritesClicked(){
          taskCommand.setValue(MovieListEventType.GO_TO_FAVOURITES);
    }

    public enum MovieListEventType{
        GO_TO_FAVOURITES
    }

    public Observable<Integer> updateFavouriteStatus(int movieId){

        return  Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                Favourite favourite = new Favourite(movieId, 1);
                int  insertResult = (int) appDatabase.favouriteDao().insert(favourite);
                Log.i("UPDATE", "Movie updated - add fav");
                Log.i("UPDATE", "update result: " + insertResult);


                e.onNext(insertResult);
            }
        }).subscribeOn(Schedulers.io());

    }

}
