package com.example.lorenaandone.first_app_v0.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.example.lorenaandone.first_app_v0.database.AppDatabase;
import com.example.lorenaandone.first_app_v0.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lorena.andone on 29.03.2018.
 */

public class FavouritesViewModel extends AndroidViewModel{

    Disposable favouritesDisposable;
    AppDatabase appDatabase;

    PublishSubject<List<FavouriteItemViewModel>> favouritesListSubject = PublishSubject.create();

    public FavouritesViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application.getApplicationContext());
    }

    public void getFavouritesList(){

        favouritesDisposable = appDatabase.movieDao().getFavourites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) throws Exception {
                         updateFavouritesVMList(movies);
                    }
                });
    }

    private void updateFavouritesVMList(List<Movie> movies){
            List<FavouriteItemViewModel> favourites = createFavouritesVMList(movies);
            favouritesListSubject.onNext(favourites);
    }

    public PublishSubject<List<FavouriteItemViewModel>> getFavouritesListToObserve(){
        return favouritesListSubject;
    }

    private List<FavouriteItemViewModel> createFavouritesVMList(List<Movie> favourites){

        List<FavouriteItemViewModel> list = new ArrayList<>();

        for(int i = 0; i < favourites.size(); i++){

            FavouriteItemViewModel item = new FavouriteItemViewModel();

            item.id.set(favourites.get(i).getId());
            item.title.set(favourites.get(i).getTitle());
            int screenWidth = getApplication().getResources().getDisplayMetrics().widthPixels;
            item.poster.set(favourites.get(i).getPosterUrl(screenWidth));

            list.add(item);
        }

        return  list;
    }
}
