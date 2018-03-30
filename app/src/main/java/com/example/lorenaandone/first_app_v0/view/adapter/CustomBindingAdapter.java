package com.example.lorenaandone.first_app_v0.view.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by lorena.andone on 19.03.2018.
 */

public class CustomBindingAdapter {

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("app:gladeUrl")
    public static void loadImage(ImageView view, String url){
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter("app:drawableRes")
    public static void setFavouriteButtonResource(FloatingActionButton button, int drawableResource){
        button.setImageResource(drawableResource);
    }
}
