package com.example.lorenaandone.first_app_v0.view.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieViewModel;

public class MainActivity extends AppCompatActivity implements OnMovieSelectedListener{

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

    @Override
    public void onMovieSelected(String movieName) {

        Toast.makeText(this, "on click : " + movieName,
                Toast.LENGTH_LONG).show();


        MovieDetailsFragment testDetailsFr = new MovieDetailsFragment();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, testDetailsFr,null).commit();
    }
}
