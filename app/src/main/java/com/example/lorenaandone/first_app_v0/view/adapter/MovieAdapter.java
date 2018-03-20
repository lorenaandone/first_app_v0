package com.example.lorenaandone.first_app_v0.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.MovieItemRowBinding;
import com.example.lorenaandone.first_app_v0.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by lorena.andone on 14.03.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private List<MovieViewModel> moviesList = new ArrayList<>();

    private final PublishSubject<MovieViewModel> onItemClick = PublishSubject.create();

    public MovieAdapter(){
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MovieItemRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.movie_item_row, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        final MovieViewModel movie = moviesList.get(position);

        holder.binding.setMovieVM(movie);
        holder.binding.executePendingBindings();

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onNext(movie);
            }
        });
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

    public void setMoviesList(List<MovieViewModel> movies){
        moviesList.clear();
        moviesList.addAll(movies);
        notifyDataSetChanged();
    }

    public PublishSubject<MovieViewModel> getOnItemClickSubject() {
        return onItemClick;
    }
}
