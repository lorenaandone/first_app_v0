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
import com.example.lorenaandone.first_app_v0.service.LocalService;

public class MainActivity extends AppCompatActivity implements OnMovieSelectedListener{

    private LocalService localService;
    boolean isServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            MovieListFragment fragment = new MovieListFragment();

            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_container, fragment, MovieListFragment.TAG).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, LocalService.class);
        startService(intent);
        boolean isBound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        System.out.println("<<<<<<<<<<Service bounded: " + isBound);

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound = false;
        }
        Log.i("SERVICE", "stopping service");
        Intent intent = new Intent(this, LocalService.class);
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

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            System.out.println("<<<<<<<<<<Reached onServiceConnected ");
            LocalService.LocalBinder localBinder = (LocalService.LocalBinder) iBinder;
            localService = localBinder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound = false;
        }
    };
}
