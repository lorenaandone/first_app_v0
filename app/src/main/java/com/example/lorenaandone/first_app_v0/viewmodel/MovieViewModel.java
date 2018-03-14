package com.example.lorenaandone.first_app_v0.viewmodel;

import android.databinding.ObservableField;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class MovieViewModel {

    public final ObservableField<String> movieName = new ObservableField<>();
    public final ObservableField<String> movieRating = new ObservableField<>();
}
