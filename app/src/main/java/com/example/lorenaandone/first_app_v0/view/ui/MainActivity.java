package com.example.lorenaandone.first_app_v0.view.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.R;

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
    public void onMovieSelected(int movieId) {

        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MovieDetailsFragment.MOVIE_ID_KEY, movieId);
        detailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, detailsFragment,null).commit();
    }
}
