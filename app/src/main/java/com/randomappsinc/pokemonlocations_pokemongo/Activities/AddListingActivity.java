package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.AddPokemonCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.PokemonPosting;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.AddPokemonAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class AddListingActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.location_input) EditText locationInput;
    @Bind(R.id.pokemon_to_add) RecyclerView pokemonToAdd;

    private PokeLocation location;
    private MaterialDialog progressDialog;
    private AddPokemonAdapter addPokemonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_listing);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        location = new PokeLocation();

        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.submitting_finding)
                .progress(true, 0)
                .cancelable(false)
                .build();

        addPokemonAdapter = new AddPokemonAdapter(this);
        pokemonToAdd.setAdapter(addPokemonAdapter);
    }

    @OnClick(R.id.location_input)
    public void chooseLocation() {
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            UIUtils.showSnackbar(parent, getString(R.string.google_locations_down));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();
            }
        }, 250);
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            location.setPlaceId(place.getId());
            location.setDisplayName(place.getName().toString());
            location.setAddress(place.getAddress().toString());
            location.setLatitude(place.getLatLng().latitude);
            location.setLongitude(place.getLatLng().longitude);
            String locationDisplay = place.getName() + "\n" + place.getAddress().toString();
            locationInput.setText(locationDisplay);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            UIUtils.showSnackbar(parent, getString(R.string.google_locations_down));
        }
    }

    private void hideKeyboard() {
        UIUtils.hideKeyboard(this);
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

    @OnClick(R.id.add_pokemon_listing)
    public void addListing() {
        UIUtils.hideKeyboard(this);

        progressDialog.show();
        AddPokemonRequest request = new AddPokemonRequest();
        List<PokemonPosting> postingList = addPokemonAdapter.getPostings();

        request.setLocation(location);
        request.setPostings(postingList);
        RestClient.get().getPokemonService()
                .addPokemon(request)
                .enqueue(new AddPokemonCallback(location, postingList));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
