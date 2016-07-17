package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

        commonPokemon.setAdapter(new PokemonAdapter(this, place.getCommonPokemon()));
        uncommonPokemon.setAdapter(new PokemonAdapter(this, place.getUncommonPokemon()));
        rarePokemon.setAdapter(new PokemonAdapter(this, place.getRarePokemon()));
    }
}
