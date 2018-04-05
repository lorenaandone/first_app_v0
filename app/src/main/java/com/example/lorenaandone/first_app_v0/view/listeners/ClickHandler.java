package com.example.lorenaandone.first_app_v0.view.listeners;

import android.content.Context;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.lorenaandone.first_app_v0.R;

/**
 * Created by lorena.andone on 05.04.2018.
 */

public class ClickHandler {

    private Context mContext;

    public ClickHandler(Context context){
        mContext = context;
    }

    public void onFilterClicked(View view) {
//        Toast.makeText(mContext, "This is click handler", Toast.LENGTH_SHORT).show();
        showFilterMenu(view);
    }

    private void showFilterMenu(View view){

        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, popupMenu.getMenu());
        popupMenu.show();
    }

}
