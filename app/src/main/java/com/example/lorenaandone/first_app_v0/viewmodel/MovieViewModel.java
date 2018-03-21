package com.example.lorenaandone.first_app_v0.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class MovieViewModel {

    public final ObservableInt movieId = new ObservableInt();
    public final ObservableField<String> movieName = new ObservableField<>();
    public final ObservableField<String> movieRating = new ObservableField<>();
    public final ObservableField<String> moviePosterUrl = new ObservableField<>();
    public final ObservableField<String> movieOverview = new ObservableField<>();

    public void onTestClicked(){
        Log.i("TEST_CLICK_DOTS", "clicked on dots");
    }

}
