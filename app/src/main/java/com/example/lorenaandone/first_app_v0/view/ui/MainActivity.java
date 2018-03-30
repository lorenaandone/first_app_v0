package com.example.lorenaandone.first_app_v0.view.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lorenaandone.first_app_v0.R;
import com.example.lorenaandone.first_app_v0.service.MovieLocalService;
import com.example.lorenaandone.first_app_v0.utils.InternetConnection;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MovieNavigator {

    private MovieLocalService movieLocalService;
    boolean isServiceBound = false;

    private InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internetConnection = InternetConnection.getInstance();

        if(savedInstanceState == null){
            MovieListFragment fragment = new MovieListFragment();

            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_container, fragment, MovieListFragment.TAG).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        internetConnection.observeConnection(getApplicationContext())
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    if(aBoolean == true){
                        startAndBindService();
                    }
                });


    }

    private void startAndBindService(){
        Intent intent = new Intent(this, MovieLocalService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound = false;
        }
        Log.i("SERVICE", "stopping service");
        Intent intent = new Intent(this, MovieLocalService.class);
        stopService(intent);
    }

    @Override
    public void onMovieSelected(int movieId) {

        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MovieDetailsFragment.MOVIE_ID_KEY, movieId);
        detailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, detailsFragment,null).commit();
    }

    @Override
    public void goToFavourites() {
        FavouritesFragment favouritesFragment = new FavouritesFragment();

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, favouritesFragment, null).commit();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            MovieLocalService.LocalBinder localBinder = (MovieLocalService.LocalBinder) iBinder;
            movieLocalService = localBinder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound = false;
        }
    };
}
