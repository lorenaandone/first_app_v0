package com.example.lorenaandone.first_app_v0.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.MovieItemRowBinding;
import com.example.lorenaandone.first_app_v0.model.TestMovieDataProvider;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieViewModel;

import java.util.List;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private List<MovieViewModel> moviesList;

    public MovieAdapter(){
        moviesList = TestMovieDataProvider.getTestMoviesData();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MovieItemRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.movie_item_row, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
           holder.binding.setMovieVM(moviesList.get(position));
           holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder{

        MovieItemRowBinding binding;

        public MovieViewHolder(MovieItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
