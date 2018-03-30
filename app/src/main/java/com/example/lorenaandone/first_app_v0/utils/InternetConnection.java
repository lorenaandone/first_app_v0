package com.example.lorenaandone.first_app_v0.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;

/**
 * Created by lorena.andone on 16.03.2018.
 */

public class InternetConnection {

    private static InternetConnection instance;

    public static final InternetConnection getInstance(){
        if (instance == null) {
            instance = new InternetConnection();
        }
        return instance;
    }

    public Observable<Boolean> observeConnection(final Context context){

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {

                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        e.onNext(netInfo != null && netInfo.isConnected());
                    }
                };

                context.registerReceiver(receiver, filter);

                final Disposable disposable = disposeInUiThread(new Action() {
                    @Override public void run() throws Exception {
                        tryToUnregisterReceiver(receiver, context);
                    }
                });

                e.setDisposable(disposable);
            }
        });
    }


    protected void tryToUnregisterReceiver(final BroadcastReceiver receiver, final Context context) {
        try {
            context.unregisterReceiver(receiver);
        } catch (Exception exception) {
            Log.e("INTERNET_CHECK", exception.getMessage());
        }
    }

    private Disposable disposeInUiThread(final Action dispose) {

        return Disposables.fromAction(new Action() {
            @Override public void run() throws Exception {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    dispose.run();
                } else {
                    final Scheduler.Worker inner = AndroidSchedulers.mainThread().createWorker();
                    inner.schedule(new Runnable() {
                        @Override public void run() {
                            try {
                                dispose.run();
                            } catch (Exception exception) {
                            }
                            inner.dispose();
                        }
                    });
                }
            }
        });
    }
}
