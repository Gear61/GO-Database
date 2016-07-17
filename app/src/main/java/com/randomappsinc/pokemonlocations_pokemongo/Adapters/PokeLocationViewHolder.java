package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.view.View;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocationViewHolder {
    @Bind(R.id.score) TextView score;
    @Bind(R.id.display_name) TextView displayName;

    public PokeLocationViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void loadLocation(PokeLocation restaurant) {
        score.setText(String.valueOf(restaurant.getScore()));
        displayName.setText(restaurant.getDisplayName());
    }
}

