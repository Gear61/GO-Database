package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.AddPokemonCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.NearbySuggestionsCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.PokemonPosting;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.NearbyRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.PokeLocationsEvent;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.AddPokemonAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokemonFormViewHolder;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class AddListingActivity extends StandardActivity {
    public static final String SCREEN_NAME = "Add Listing";
    public static final String LOCATION_KEY = "location";
    public static final String FREQUENCY_INDEX_KEY = "frequencyIndex";

    @Bind(R.id.parent) View parent;
    @Bind(R.id.location_input) EditText locationInput;
    @Bind(R.id.pokemon_to_add) RecyclerView pokemonToAdd;
    @BindString(R.string.share_and_favorite) String favTemplate;

    private PokeLocation location;
    private MaterialDialog progressDialog;
    private MaterialDialog pokeFormDialog;
    private AddPokemonAdapter addPokemonAdapter;
    private PokemonFormViewHolder pokemonFormHolder;
    private ArrayList<PokeLocation> nearbySuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_listing);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (PreferencesManager.get().getCurrentLocation().equals(getString(R.string.automatic))) {
            if (SmartLocation.with(this).location().state().locationServicesEnabled()) {
                SmartLocation.with(this).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                NearbyRequest request = new NearbyRequest();
                                request.setLocation(location.getLatitude(), location.getLongitude());
                                request.setRange(0.0725);
                                RestClient.get().getPokemonService().searchNearby(request).enqueue(new NearbySuggestionsCallback());
                            }
                        });
            }
        } else {
            // Easy-mode case - They haven't seen their location to automatic
            String currentLocation = PreferencesManager.get().getCurrentLocation();
            SavedLocationDO locationDO = DatabaseManager.get().getLocation(currentLocation);

            NearbyRequest request = new NearbyRequest();
            request.setLocation(locationDO.getLatitude(), locationDO.getLongitude());
            request.setRange(0.0725);
            RestClient.get().getPokemonService().searchNearby(request).enqueue(new NearbySuggestionsCallback());
        }

        location = new PokeLocation();

        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.submitting_finding)
                .progress(true, 0)
                .cancelable(false)
                .build();

        pokeFormDialog = new MaterialDialog.Builder(this)
                .title(R.string.pokemon_form)
                .customView(R.layout.pokemon_form, true)
                .cancelable(false)
                .positiveText(R.string.add)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PokemonPosting posting = pokemonFormHolder.getPosting();
                        addPokemonAdapter.addPokemonPosting(posting);
                        pokemonToAdd.scrollToPosition(addPokemonAdapter.getItemCount() - 1);
                    }
                })
                .build();
        pokeFormDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        View addButton = pokeFormDialog.getActionButton(DialogAction.POSITIVE);
        pokemonFormHolder = new PokemonFormViewHolder(this, pokeFormDialog.getCustomView(), addButton);

        addPokemonAdapter = new AddPokemonAdapter(this);
        pokemonToAdd.setAdapter(addPokemonAdapter);

        loadForm();
    }

    public void loadForm() {
        PokeLocation preFill = getIntent().getParcelableExtra(LOCATION_KEY);
        if (preFill != null) {
            location = preFill;
            String locationDisplay = location.getDisplayName() + "\n" + location.getAddress();
            locationInput.setText(locationDisplay);
            int frequencyIndex = getIntent().getIntExtra(FREQUENCY_INDEX_KEY, 0);
            pokemonFormHolder.setFrequency(frequencyIndex);
            pokemonFormHolder.setPrefillMode(true);
            pokemonFormHolder.setAlreadyChosen(new HashSet<String>());
            pokeFormDialog.show();
            pokemonFormHolder.requestFocus();
        }
    }

    @OnClick(R.id.add_pokemon)
    public void addPokemon() {
        pokemonFormHolder.setAlreadyChosen(addPokemonAdapter.getAlreadyAdded());
        pokemonFormHolder.clearForm();
        pokeFormDialog.show();
        pokemonFormHolder.requestFocus();
    }

    @OnClick(R.id.location_input)
    public void chooseLocation() {
        Intent intent = new Intent(this, SelectLocationActivity.class);
        intent.putParcelableArrayListExtra(PokeLocation.KEY, nearbySuggestions);
        startActivityForResult(intent, 1);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            location = data.getParcelableExtra(PokeLocation.KEY);
            String locationDisplay = location.getDisplayName() + "\n" + location.getAddress();
            locationInput.setText(locationDisplay);
        }
    }

    @Subscribe
    public void onEvent(PokeLocationsEvent event) {
        if (event.getScreen().equals(SCREEN_NAME)) {
            nearbySuggestions = (ArrayList<PokeLocation>) event.getLocations();
        }
    }

    @Subscribe
    public void onEvent(String event) {
        switch (event) {
            case AddPokemonCallback.ADD_POKEMON_SUCCESS:
                addPokemonAdapter.clearPostings();
                locationInput.setText("");
                progressDialog.dismiss();
                if (DatabaseManager.get().isLocationFavorited(location)) {
                    UIUtils.showSnackbar(parent, getString(R.string.share_pokemon_success));
                } else {
                    PokeLocation locationHolder = location;
                    String message = String.format(favTemplate, locationHolder.getDisplayName());
                    UIUtils.addFavoriteSnackbar(parent, message, locationHolder);
                }
                location = new PokeLocation();
                break;
            case AddPokemonCallback.ADD_POKEMON_FAILURE:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.add_pokemon_fail));
                break;
        }
    }

    @OnClick(R.id.add_pokemon_listing)
    public void addListing() {
        UIUtils.hideKeyboard(this);
        if (addPokemonAdapter.getItemCount() == 0) {
            UIUtils.showSnackbar(parent, getString(R.string.no_pokemon));
        } else if (location.getPlaceId() == null) {
            UIUtils.showSnackbar(parent, getString(R.string.no_place));
        } else {
            progressDialog.show();
            AddPokemonRequest request = new AddPokemonRequest();
            List<PokemonPosting> postingList = addPokemonAdapter.getPostings(location);

            request.setLocation(location);
            request.setPostings(postingList);
            RestClient.get().getPokemonService()
                    .addPokemon(request)
                    .enqueue(new AddPokemonCallback(location, postingList));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
