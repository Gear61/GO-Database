package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.randomappsinc.pokemonlocations_pokemongo.Adapters.RankingsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.JSONUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

/**
 * Created by alexanderchiou on 10/4/16.
 */

public class RankingsActivity extends StandardActivity {
    @Bind(R.id.pokemon_list) ListView pokemonList;

    private RankingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new RankingsAdapter(this);
        pokemonList.setAdapter(adapter);
    }

    @OnItemSelected(R.id.sort_options)
    public void onRankOptionChosen(int position) {
        adapter.setSortOption(position);
        pokemonList.setSelection(0);
    }

    @OnItemClick(R.id.pokemon_list)
    public void onPokemonClicked(int position) {
        Pokemon pokemon = adapter.getItem(position);
        Intent intent = new Intent(this, PokemonActivity.class);
        intent.putExtra(JSONUtils.POKEMON_KEY, pokemon);
        startActivity(intent);
    }
}
