package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.widget.ListView;

import com.randomappsinc.pokemonlocations_pokemongo.Adapters.ArtistsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 7/24/16.
 */
public class CreditsActivity extends StandardActivity {
    @Bind(R.id.artists) ListView artists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sprite_credits);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        artists.setAdapter(new ArtistsAdapter(this));
    }

    @OnItemClick(R.id.artists)
    public void artistChosen(int position) {

    }
}
