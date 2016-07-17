package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocationActivity extends StandardActivity {
    @Bind(R.id.score) TextView score;
    @Bind(R.id.display_name) TextView displayName;
    @Bind(R.id.common_pokemon) RecyclerView commonPokemon;
    @Bind(R.id.uncommon_pokemon) RecyclerView uncommonPokemon;
    @Bind(R.id.rare_pokemon) RecyclerView rarePokemon;

    @Bind(R.id.no_common_pokemon) View noCommon;
    @Bind(R.id.no_uncommon_pokemon) View noUncommon;
    @Bind(R.id.no_rare_pokemon) View noRare;

    private PokeLocation place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokelocation);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place = getIntent().getParcelableExtra(PokeLocation.KEY);

        score.setText(String.valueOf(place.getScore()));
        displayName.setText(place.getDisplayName());

        if (place.getCommonPokemon().isEmpty()) {
            noCommon.setVisibility(View.VISIBLE);
        } else {
            commonPokemon.setVisibility(View.VISIBLE);
            commonPokemon.setAdapter(new PokemonAdapter(this, place.getCommonPokemon()));
        }

        if (place.getUncommonPokemon().isEmpty()) {
            noUncommon.setVisibility(View.VISIBLE);
        } else {
            uncommonPokemon.setVisibility(View.VISIBLE);
            uncommonPokemon.setAdapter(new PokemonAdapter(this, place.getUncommonPokemon()));
        }

        if (place.getRarePokemon().isEmpty()) {
            noRare.setVisibility(View.VISIBLE);
        } else {
            rarePokemon.setVisibility(View.VISIBLE);
            rarePokemon.setAdapter(new PokemonAdapter(this, place.getRarePokemon()));
        }
    }
}
