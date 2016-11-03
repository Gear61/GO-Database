package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonCardsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;

/**
 * Created by alexanderchiou on 9/22/16.
 */

public class PokemonActivity extends StandardActivity {
    public static final String CURRENT_POSITION_KEY = "currentPosition";

    @Bind(R.id.pokemon_pager) ViewPager pokemonPager;

    private PokemonCardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_view);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int currentPosition = getIntent().getIntExtra(CURRENT_POSITION_KEY, 0);
        adapter = new PokemonCardsAdapter(getFragmentManager());
        pokemonPager.setAdapter(adapter);
        pokemonPager.setCurrentItem(currentPosition);
    }

    @OnPageChange(value = R.id.pokemon_pager, callback = OnPageChange.Callback.PAGE_SELECTED)
    public void afterTextChanged(int position) {
        setTitle(adapter.getPokemon(position).getName());
    }
}
