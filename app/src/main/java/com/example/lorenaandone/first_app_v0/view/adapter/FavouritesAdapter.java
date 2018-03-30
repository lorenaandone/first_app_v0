package com.example.lorenaandone.first_app_v0.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.FavouriteItemBinding;
import com.example.lorenaandone.first_app_v0.viewmodel.FavouriteItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorena.andone on 29.03.2018.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>{

    List<FavouriteItemViewModel> favouritesList = new ArrayList<>();

    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FavouriteItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.favourite_item, parent, false);
        return new FavouritesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FavouritesViewHolder holder, int position) {

        final FavouriteItemViewModel favouriteItem = favouritesList.get(position);

        holder.binding.setFavouriteItem(favouriteItem);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    static class FavouritesViewHolder extends RecyclerView.ViewHolder{

        FavouriteItemBinding binding;

        public FavouritesViewHolder(FavouriteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public void setFavouritesList(List<FavouriteItemViewModel> favouritesList){

        this.favouritesList.clear();
        this.favouritesList.addAll(favouritesList);
        notifyDataSetChanged();
    }
}
