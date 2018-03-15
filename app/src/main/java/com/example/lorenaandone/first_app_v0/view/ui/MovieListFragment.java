package com.example.lorenaandone.first_app_v0.view.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.FragmentMovieListBinding;
import com.example.lorenaandone.first_app_v0.model.Movie;
import com.example.lorenaandone.first_app_v0.view.adapter.MovieAdapter;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieListViewModel;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lorena.andone on 13.03.2018.
 */

public class MovieListFragment extends Fragment {

    public static final String TAG = "MovieListFragment";

    FragmentMovieListBinding binding;
    MovieListViewModel listViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MovieAdapter movieAdapter = new MovieAdapter();
        binding.movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.movieList.setAdapter(movieAdapter);

        listViewModel = new MovieListViewModel(getActivity().getApplication());
        listViewModel.fetchMoviesList();

        List<Movie> testList = listViewModel.getMovieList();


        if (testList != null && testList.size() > 0) {
            for (int i = 0; i < 5; i++) {
                System.out.println("<<<<<<<<<<Tihs is a test" + testList.get(i).getOriginalTitle());
            }
        } else {
            Toast.makeText(getActivity(), "Not getting data", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listViewModel.reset();
    }

}
