package com.example.eric.pokedex;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by eric on 2/25/17.
 */

public class Utils {

    public static int FILTER_STATS_CONSTANT = 1;
    public static int FILTER_RANDOM_CONSTANT = 2;

    public static void loadImage(Context context, String url, String pokeName, ImageView pokeImage){

        if (pokeName.equalsIgnoreCase("Flabébé")){
            Glide.with(context).load("http://img.pokemondb.net/artwork/flabebe.jpg").into(pokeImage);

        } else if (pokeName.equalsIgnoreCase("Farfetch'd")){
            Glide.with(context).load("http://img.pokemondb.net/artwork/farfetchd.jpg").into(pokeImage);

        } else {
            Glide.with(context).load(url).into(pokeImage);
        }

    }
}
