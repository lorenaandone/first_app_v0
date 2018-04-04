package com.example.lorenaandone.first_app_v0.view.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.database.AppDatabase;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lorena.andone on 03.04.2018.
 */

public class DeleteFromDBUseCase {

    private AppDatabase appDatabase;

    public DeleteFromDBUseCase(Context context){
        appDatabase = AppDatabase.getInstance(context);
    }

    public Observable<Boolean> deleteFromDb(@NonNull final int id) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                appDatabase.favouriteDao().deleteFavourite(id);
                e.onNext(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }
}
