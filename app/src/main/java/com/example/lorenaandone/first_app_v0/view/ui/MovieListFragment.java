package com.example.lorenaandone.first_app_v0.view.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.FragmentMovieListBinding;
import com.example.lorenaandone.first_app_v0.view.listeners.ClickHandler;
import com.example.lorenaandone.first_app_v0.view.adapter.MovieAdapter;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieListViewModel;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lorena.andone on 13.03.2018.
 */

public class MovieListFragment extends Fragment{

    public static final String TAG = "MovieListFragment";

    FragmentMovieListBinding binding;
    MovieListViewModel listViewModel;
    MovieAdapter movieAdapter;

    MovieNavigator callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callback = (MovieNavigator) getActivity();
        }catch (ClassCastException ex){
            throw new ClassCastException(getActivity().toString() + "must implement MovieNavigator");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupAdapter();
        setupMovieListViewModel();
        setupClickHandler();

        getDataFromDbAndSetAdapter();

        setupOnItemClicked();
        setupOnFavouriteClicked();

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

        subscribeToNavigationChanges();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        listViewModel.unsubscribeFromObservables();
    }

    private void setupAdapter(){
        movieAdapter = new MovieAdapter(getActivity());
        binding.movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.movieList.setAdapter(movieAdapter);
        binding.setIsLoading(true);
    }

    private void getDataFromDbAndSetAdapter(){
        listViewModel.getMovieDataFromDb();
        subscribeForListData();
    }

    private void setupMovieListViewModel(){
        listViewModel = new MovieListViewModel(getActivity().getApplication());
        binding.setMovieListViewModel(listViewModel);
    }

    private void setupClickHandler(){
        ClickHandler clickHandler = new ClickHandler(getActivity());
        binding.setClickHandler(clickHandler);
    }

    private void subscribeForListData(){
        listViewModel.getMovieList().subscribe(new Consumer<List<MovieViewModel>>() {
            @Override
            public void accept(List<MovieViewModel> movies) throws Exception {
                if(movies != null && movies.size() > 0){
                    movieAdapter.setMoviesList(movies);
                    binding.setIsLoading(false);
                }
            }
        });

    }

    private void subscribeToNavigationChanges(){
        listViewModel.getTaskCommand().observe(this, movieListEventType -> {
            if(movieListEventType.equals(MovieListViewModel.MovieListEventType.GO_TO_FAVOURITES)){
                    callback.goToFavourites();
            }
        });
    }

    private void setupOnItemClicked(){
        if(movieAdapter != null){
            movieAdapter.getOnItemClickSubject().subscribe(new Consumer<MovieViewModel>() {
                @Override
                public void accept(MovieViewModel movieViewModel) throws Exception {
                    callback.onMovieSelected(movieViewModel.movieId.get());
                }
            });
        }
    }

    private void setupOnFavouriteClicked(){
        if(movieAdapter != null){
            movieAdapter.getOnAddToFavouriteClick()
                    .flatMap(movieViewModel -> {return listViewModel.updateFavouriteStatus(movieViewModel.movieId.get());})
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            if(integer > 0){
                                Toast.makeText(getActivity(), "Added to favs", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }
    }
}
