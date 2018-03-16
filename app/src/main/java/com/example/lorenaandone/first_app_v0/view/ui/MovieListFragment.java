package com.example.lorenaandone.first_app_v0.view.ui;

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
import com.example.lorenaandone.first_app_v0.view.adapter.MovieAdapter;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieListViewModel;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieViewModel;

import java.util.List;

import io.reactivex.functions.Consumer;

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

        final MovieAdapter movieAdapter = new MovieAdapter();
        binding.movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.movieList.setAdapter(movieAdapter);

        listViewModel = new MovieListViewModel(getActivity().getApplication());
        binding.setMovieListViewModel(listViewModel);
//        listViewModel.startFetchingMoviesList();

        listViewModel.getTaskCommand().observe(this, new android.arch.lifecycle.Observer<MovieListViewModel.MovieListEventType>() {
            @Override
            public void onChanged(@Nullable MovieListViewModel.MovieListEventType movieListEventType) {
                if(movieListEventType == MovieListViewModel.MovieListEventType.NO_INTERNET){
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        listViewModel.getMovieList().subscribe(new Consumer<List<MovieViewModel>>() {
            @Override
            public void accept(List<MovieViewModel> movies) throws Exception {
                    movieAdapter.setMoviesList(movies);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listViewModel.unsubscribeFromObservables();
    }

}
