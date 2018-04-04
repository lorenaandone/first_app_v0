package com.example.lorenaandone.first_app_v0.view.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.databinding.FragmentFavouritesBinding;
import com.example.lorenaandone.first_app_v0.utils.MovieActionType;
import com.example.lorenaandone.first_app_v0.view.adapter.FavouritesAdapter;
import com.example.lorenaandone.first_app_v0.view.interactor.DeleteFromDBUseCase;
import com.example.lorenaandone.first_app_v0.viewmodel.FavouriteItemViewModel;
import com.example.lorenaandone.first_app_v0.viewmodel.FavouritesViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by lorena.andone on 29.03.2018.
 */

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    private FavouritesAdapter favouritesAdapter;
    private FavouritesViewModel favouritesViewModel;

    private MovieNavigator callback;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (MovieNavigator) getActivity();

    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(view -> getActivity().getSupportFragmentManager().popBackStack());
    }

    private void setupAdapter() {
        favouritesAdapter = new FavouritesAdapter(getActivity());
        binding.favouritesList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.favouritesList.setHasFixedSize(true);
        binding.favouritesList.setAdapter(favouritesAdapter);

        subscribeForClickEvents();
    }

    private void subscribeForListData() {

        favouritesViewModel.getFavouritesListToObserve().subscribe(new Consumer<List<FavouriteItemViewModel>>() {
            @Override
            public void accept(List<FavouriteItemViewModel> favouriteItemViewModels) throws Exception {
                favouritesAdapter.setFavouritesList(favouriteItemViewModels);
            }
        });
    }

    private void subscribeForClickEvents() {
        if (favouritesAdapter != null) {
            favouritesAdapter.getOnLongClicked()
                    .subscribe(new Consumer<Pair<MovieActionType, Integer>>() {
                        @Override
                        public void accept(Pair<MovieActionType, Integer> movieActionTypeIntegerPair) throws Exception {
                            switch (movieActionTypeIntegerPair.first) {
                                case DELETE_FROM_FAVOURITES:
                                    // startDelete
                                    new DeleteFromDBUseCase(getActivity().getApplicationContext())
                                            .deleteFromDb(movieActionTypeIntegerPair.second).observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<Boolean>() {
                                                @Override
                                                public void accept(Boolean deleteDBVaue) throws Exception {
                                                    Toast.makeText(getContext(), "Delete status : " + deleteDBVaue, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    break;
                                case OPEN_DETAILS:
                                    // Main thread
                                    callback.onMovieSelected(movieActionTypeIntegerPair.second);
                                    break;
                                case DO_SOMETHING_ELSE:

                                    break;
                                default:
                                    // Please log this event... we don't have this implementation
                            }
                        }
                    });
        }
    }
}
