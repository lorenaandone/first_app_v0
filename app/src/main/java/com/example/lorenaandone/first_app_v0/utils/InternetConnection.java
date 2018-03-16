package com.example.lorenaandone.first_app_v0.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Single;

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


    public static Single<Boolean> isInternetOn(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return Single.just(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }
}
