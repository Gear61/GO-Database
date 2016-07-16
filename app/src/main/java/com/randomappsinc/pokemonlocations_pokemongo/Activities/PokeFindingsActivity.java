package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;

import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class PokeFindingsActivity extends StandardActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poke_findings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
