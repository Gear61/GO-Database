package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.JournalAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class JournalActivity extends StandardActivity {
    @Bind(R.id.journal) ListView journal;
    @Bind(R.id.no_content) TextView noContent;

    private JournalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new JournalAdapter(this, noContent);
        journal.setAdapter(adapter);
    }

    @OnItemClick(R.id.journal)
    public void onEntryClick(int position) {
        String placeId = adapter.getItem(position).getPlaceId();
        PokeLocation pokeLocation = DatabaseManager.get().getFavorite(placeId);
        if (pokeLocation != null) {
            Intent intent = new Intent(this, PokeLocationActivity.class);
            intent.putExtra(PokeLocation.KEY, pokeLocation);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PokeLocationActivity.class);
            intent.putExtra(ApiConstants.ID_KEY, placeId);
            startActivity(intent);
        }
    }
}
