package com.example.lorenaandone.first_app_v0.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.MovieDetailsBinding;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieDetailViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lorena.andone on 19.03.2018.
 */

public class MovieDetailsFragment extends Fragment {

    public final static String TAG = "MovieDetailsFragment";
    public final static String MOVIE_ID_KEY = "movie_id_key";

    private MovieDetailViewModel detailViewModel;
    private MovieDetailsBinding binding;
    private int movieId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.movie_details, container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        detailViewModel = new MovieDetailViewModel(getActivity().getApplication());
        binding.setMovieDetailVM(detailViewModel);

        if(getArguments() != null){
             movieId = getArguments().getInt(MOVIE_ID_KEY);
        }

        detailViewModel.getMovieByIdFromDb(movieId);

        detailViewModel.getOnFavouriteObservable()
                .debounce(2000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if(integer == R.drawable.ic_heart){
                    detailViewModel.updateFavouriteStatus(movieId, 1);
                } else if(integer == R.drawable.ic_heart_outline)
                    detailViewModel.updateFavouriteStatus(movieId, 0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detailViewModel.unsubscribeFromObservable();
    }
}
