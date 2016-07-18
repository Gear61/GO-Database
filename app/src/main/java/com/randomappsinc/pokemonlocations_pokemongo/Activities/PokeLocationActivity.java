package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokeLocationViewHolder;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocationActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.score) TextView score;
    @Bind(R.id.display_name) TextView displayName;
    @Bind(R.id.common_pokemon) RecyclerView commonPokemon;
    @Bind(R.id.uncommon_pokemon) RecyclerView uncommonPokemon;
    @Bind(R.id.rare_pokemon) RecyclerView rarePokemon;

    @Bind(R.id.no_common_pokemon) View noCommon;
    @Bind(R.id.no_uncommon_pokemon) View noUncommon;
    @Bind(R.id.no_rare_pokemon) View noRare;

    private PokeLocation place;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokelocation);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place = getIntent().getParcelableExtra(PokeLocation.KEY);
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.submitting_finding)
                .progress(true, 0)
                .cancelable(false)
                .build();

        PokeLocationViewHolder viewHolder = new PokeLocationViewHolder(findViewById(R.id.pokelocation_parent));
        viewHolder.loadItem(place);

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

    public PokeLocation getPlace() {
        return place;
    }

    public void submitPokefinding(Pokemon pokemon, float frequency) {
        // TODO: Make API call
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        if (DatabaseManager.get().isLocationFavorited(place)) {
            menu.findItem(R.id.favorite_location).setTitle(R.string.unfavorite_location);
            UIUtils.loadMenuIcon(menu, R.id.favorite_location, IoniconsIcons.ion_android_star);
        } else {
            UIUtils.loadMenuIcon(menu, R.id.favorite_location, IoniconsIcons.ion_android_star_outline);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite_location:
                if (DatabaseManager.get().isLocationFavorited(place)) {
                    DatabaseManager.get().unfavoriteLocation(place);
                } else {
                    DatabaseManager.get().favoriteLocation(place);
                }
                invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
