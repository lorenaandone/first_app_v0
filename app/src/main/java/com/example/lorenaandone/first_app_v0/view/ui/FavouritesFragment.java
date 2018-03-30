package com.example.lorenaandone.first_app_v0.view.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.FragmentFavouritesBinding;
import com.example.lorenaandone.first_app_v0.view.adapter.FavouritesAdapter;
import com.example.lorenaandone.first_app_v0.viewmodel.FavouriteItemViewModel;
import com.example.lorenaandone.first_app_v0.viewmodel.FavouritesViewModel;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by lorena.andone on 29.03.2018.
 */

public class FavouritesFragment extends Fragment{

    private FragmentFavouritesBinding binding;
    private FavouritesAdapter favouritesAdapter;
    private FavouritesViewModel favouritesViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupToolbar();
        setupAdapter();

        favouritesViewModel = new FavouritesViewModel(getActivity().getApplication());

        favouritesViewModel.getFavouritesList();
        subscribeForListData();
    }

    private void setupToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(view -> getActivity().getSupportFragmentManager().popBackStack());
    }

    private void setupAdapter(){
        favouritesAdapter = new FavouritesAdapter();
        binding.favouritesList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.favouritesList.setHasFixedSize(true);
        binding.favouritesList.setAdapter(favouritesAdapter);
    }

    private void subscribeForListData(){

        favouritesViewModel.getFavouritesListToObserve().subscribe(new Consumer<List<FavouriteItemViewModel>>() {
            @Override
            public void accept(List<FavouriteItemViewModel> favouriteItemViewModels) throws Exception {
                    favouritesAdapter.setFavouritesList(favouriteItemViewModels);
            }
        });
    }
}
