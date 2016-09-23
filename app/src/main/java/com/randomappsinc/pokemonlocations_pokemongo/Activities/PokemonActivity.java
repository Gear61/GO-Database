package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.JSONUtils;

import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 9/22/16.
 */

public class PokemonActivity extends StandardActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_view);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Pokemon pokemon = getIntent().getParcelableExtra(JSONUtils.POKEMON_KEY);
        setTitle(pokemon.getName());
    }
}
