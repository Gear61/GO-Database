package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.AddPokemonCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.SingleLocationCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.PokemonPosting;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.SyncLocationsRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokeLocationViewHolder;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private PokeLocation place;
    private MaterialDialog progressDialog;
    private boolean notInitialLoad;
    private PokemonAdapter commonAdapter;
    private PokemonAdapter uncommonAdapter;
    private PokemonAdapter rareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poke_location);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place = getIntent().getParcelableExtra(PokeLocation.KEY);
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.submitting_finding)
                .progress(true, 0)
                .cancelable(false)
                .build();

        PokeLocationViewHolder viewHolder = new PokeLocationViewHolder(findViewById(R.id.pokelocation_parent));
        viewHolder.loadItem(place);

        commonAdapter = new PokemonAdapter(this, place.getCommonPokemon());
        uncommonAdapter = new PokemonAdapter(this, place.getUncommonPokemon());
        rareAdapter = new PokemonAdapter(this, place.getRarePokemon());

        commonPokemon.setAdapter(commonAdapter);
        uncommonPokemon.setAdapter(uncommonAdapter);
        rarePokemon.setAdapter(rareAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (notInitialLoad) {
            List<String> placeId = new ArrayList<>();
            placeId.add(place.getPlaceId());
            SyncLocationsRequest request = new SyncLocationsRequest();
            request.setPlaceIds(placeId);
            RestClient.get().getPokemonService()
                    .syncLocations(request)
                    .enqueue(new SingleLocationCallback());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        notInitialLoad = true;
    }

    @OnClick({R.id.add_common_pokemon, R.id.add_uncommon_pokemon, R.id.add_rare_pokemon})
    public void addPokemon(View view) {
        Intent intent = new Intent(this, AddListingActivity.class);
        intent.putExtra(AddListingActivity.LOCATION_KEY, place);
        switch (view.getId()) {
            case R.id.add_uncommon_pokemon:
                intent.putExtra(AddListingActivity.FREQUENCY_INDEX_KEY, 1);
                break;
            case R.id.add_rare_pokemon:
                intent.putExtra(AddListingActivity.FREQUENCY_INDEX_KEY, 2);
                break;
        }
        startActivity(intent);
    }

    public PokeLocation getPlace() {
        return place;
    }

    public void submitPokefinding(Pokemon pokemon, int frequencyIndex) {
        progressDialog.show();
        float frequencyScore = PokemonUtils.getFrequency(frequencyIndex);
        AddPokemonRequest addPokemonRequest = new AddPokemonRequest();
        addPokemonRequest.setLocation(place);

        List<PokemonPosting> postings = new ArrayList<>();
        PokemonPosting posting = new PokemonPosting();
        posting.setPokemonId(pokemon.getId());
        posting.setRarity(frequencyScore);
        postings.add(posting);
        addPokemonRequest.setPostings(postings);

        RestClient.get().getPokemonService()
                .addPokemon(addPokemonRequest)
                .enqueue(new AddPokemonCallback(place, postings));
    }

    @OnClick(R.id.start_navigation)
    public void startHeadingHere() {
            String mapUri = "google.navigation:q=" + place.getDisplayName()
                    + ", " + place.getAddress();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
    }

    @Subscribe
    public void onEvent(String event) {
        switch (event) {
            case AddPokemonCallback.ADD_POKEMON_SUCCESS:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.share_pokemon_success));
                break;
            case AddPokemonCallback.ADD_POKEMON_FAILURE:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.add_pokemon_fail));
                break;
        }
    }

    @Subscribe
    public void onEvent(PokeLocation updatedLocation) {
        if (place.getPlaceId().equals(updatedLocation.getPlaceId())) {
            place = updatedLocation;
            commonAdapter.setPokemonList(place.getCommonPokemon());
            uncommonAdapter.setPokemonList(place.getUncommonPokemon());
            rareAdapter.setPokemonList(place.getRarePokemon());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                    DatabaseManager.get().addOrUpdateLocation(place);
                }
                invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
