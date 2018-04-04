package com.example.lorenaandone.first_app_v0.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.FavouriteItemBinding;
import com.example.lorenaandone.first_app_v0.utils.MovieActionType;
import com.example.lorenaandone.first_app_v0.viewmodel.FavouriteItemViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by lorena.andone on 29.03.2018.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>{

    List<FavouriteItemViewModel> favouritesList = new ArrayList<>();

    PublishSubject<Pair<MovieActionType,Integer>> onLongClicked = PublishSubject.create();

    private Context mContext;

    public FavouritesAdapter(Context context){
        mContext = context;
    }

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

        holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPopupMenu(view, favouriteItem.id.get());
                return true;
            }
        });
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

    private void showPopupMenu(View view, int movieId){

        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_favourite_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenuItemClick(movieId));
        popupMenu.show();
    }

    public PublishSubject<Pair<MovieActionType, Integer>> getOnLongClicked() {
        return onLongClicked;
    }

    private class PopupMenuItemClick implements PopupMenu.OnMenuItemClickListener{

        private int mCurrentMovieId;

        PopupMenuItemClick(Integer movieId){
            mCurrentMovieId = movieId;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()){
                case R.id.action_delete_favourite:
                    Pair<MovieActionType, Integer> deletePair = Pair.create(MovieActionType.DELETE_FROM_FAVOURITES, mCurrentMovieId);
                    onLongClicked.onNext(deletePair);
                    break;
                case R.id.action_open_favourite:
                    Pair<MovieActionType, Integer> openPair = Pair.create(MovieActionType.OPEN_DETAILS, mCurrentMovieId);
                    onLongClicked.onNext(openPair);
                    break;

                case R.id.action_test_favourite:
                    Pair<MovieActionType, Integer> doSmthPair = Pair.create(MovieActionType.DO_SOMETHING_ELSE, mCurrentMovieId);
                    onLongClicked.onNext(doSmthPair);
                    break;
            }
            return false;
        }
    }

}
