package com.example.lorenaandone.first_app_v0.model;

import com.example.lorenaandone.first_app_v0.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class TestMovieDataProvider {

    public static List<MovieViewModel> getTestMoviesData() {
        List<MovieViewModel> movies = new ArrayList<>();

        for (int i = 0; i < 10; i++) {


            MovieViewModel movieVM = new MovieViewModel();
            movieVM.movieName.set("Red Sparrow");
            movieVM.movieRating.set("4");

            MovieViewModel movieVM2 = new MovieViewModel();
            movieVM2.movieName.set("The Avengers");
            movieVM2.movieRating.set("5");

            movies.add(movieVM);
            movies.add(movieVM2);
        }

        return movies;
    }
}
