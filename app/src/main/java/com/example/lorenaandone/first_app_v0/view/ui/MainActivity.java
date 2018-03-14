package com.example.lorenaandone.first_app_v0.view.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            MovieListFragment fragment = new MovieListFragment();

            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_container, fragment, MovieListFragment.TAG).commit();
        }
    }
}
