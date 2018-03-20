package com.example.lorenaandone.first_app_v0.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorenaandone.first_app_v0.R;

/**
 * Created by lorena.andone on 19.03.2018.
 */

public class MovieDetailsFragment extends Fragment {

    public final static String TAG = "MovieDetailsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.movie_details, container, false);
        return root;
    }


}
