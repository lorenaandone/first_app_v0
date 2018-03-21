package com.example.lorenaandone.first_app_v0.view.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    MovieAdapter movieAdapter;

    OnMovieSelectedListener callback;

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
            callback = (OnMovieSelectedListener) getActivity();
        }catch (ClassCastException ex){
            throw new ClassCastException(getActivity().toString() + "must implement OnMovieSelectedListener");
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
        subscribeToInternetConnectionChanges();
        subscribeForListData();
        setupOnItemClicked();

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listViewModel.unsubscribeFromObservables();
    }

    private void setupAdapter(){
        movieAdapter = new MovieAdapter();
        binding.movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.movieList.setAdapter(movieAdapter);
        binding.setIsLoading(true);
    }

    private void setupMovieListViewModel(){
        listViewModel = new MovieListViewModel(getActivity().getApplication());
        binding.setMovieListViewModel(listViewModel);
    }


    private void showNoInternetDialog(){

        AlertDialog dialog  = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle(getResources().getString(R.string.no_internet_dialog_title));
        dialog.setMessage(getResources().getString(R.string.no_internet_dialog_message));
        dialog.setIcon(R.drawable.ic_no_conection);
        dialog.setButton(getResources().getString(R.string.no_internet_dialog_button_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void subscribeToInternetConnectionChanges(){

        //check internet connection and fetch the movie list
        listViewModel.checkInternetConnection();
        listViewModel.getIsInternetOn().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(aBoolean == true){
                    if(binding.getIsLoading()){
                        listViewModel.fetchMoviesList();
                        Log.i("INTERNET CONNECTION", "Internet ON, !!! List fetched !!!");
                    }else{
                        Log.i("INTERNET CONNECTION", "Internet ON, !!! No list fetching!!!");
                    }
                }else{
                    if(binding.getIsLoading()){
                        showNoInternetDialog();
                        Log.i("INTERNET CONNECTION", "Internet OFF");
                    }else{
                        Log.i("INTERNET CONNECTION", "Internet OFF, !!! Not showing dialog!!!");
                    }
                }
            }
        });
    }

    private void subscribeForListData(){
        listViewModel.getMovieList().subscribe(new Consumer<List<MovieViewModel>>() {
            @Override
            public void accept(List<MovieViewModel> movies) throws Exception {
                movieAdapter.setMoviesList(movies);
                binding.setIsLoading(false);
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
}
