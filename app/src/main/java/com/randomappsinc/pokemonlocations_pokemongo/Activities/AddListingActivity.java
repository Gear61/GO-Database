package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.AddPokemonCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonACAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class AddListingActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.pokemon_input) AutoCompleteTextView pokemonInput;
    @Bind(R.id.location_input) EditText locationInput;
    @Bind(R.id.frequency) EditText frequencyInput;

    private int currentFrequencyIndex;
    private PokeLocation location;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_listing);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        location = new PokeLocation();
        currentFrequencyIndex = -1;
        pokemonInput.setAdapter(new PokemonACAdapter(this, R.layout.pokemon_ac_item, new ArrayList<String>()));
        pokemonInput.requestFocus();
        UIUtils.showKeyboard(this);

        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.submitting_finding)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    @OnTextChanged(value = R.id.pokemon_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onPokemonChanged(Editable inputText) {
        if (PokemonServer.get().isValidPokemon(inputText.toString())) {
            parent.requestFocus();
            UIUtils.hideKeyboard(this);
        }
    }

    @OnClick(R.id.clear_pokemon)
    public void clearPokemon() {
        pokemonInput.setText("");
    }

    @OnClick(R.id.location_input)
    public void chooseLocation() {
        try {
            AutocompleteFilter establishmentFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build();

            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(establishmentFilter)
                    .build(this);
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

    @OnClick(R.id.frequency)
    public void showFrequencyOptions() {
        new MaterialDialog.Builder(this)
                .title(R.string.frequency_title)
                .items(R.array.frequency_options)
                .itemsCallbackSingleChoice(currentFrequencyIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        currentFrequencyIndex = which;
                        frequencyInput.setText(text);
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.no)
                .show();
    }

    @Subscribe
    public void onEvent(String event) {
        switch (event) {
            case AddPokemonCallback.ADD_POKEMON_SUCCESS:
                pokemonInput.setText("");
                frequencyInput.setText("");
                currentFrequencyIndex = -1;
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

        String pokemonName = pokemonInput.getText().toString();
        String frequencyText = frequencyInput.getText().toString();

        if (!PokemonServer.get().isValidPokemon(pokemonName)) {
            UIUtils.showSnackbar(parent, getString(R.string.invalid_pokemon));
        } else if (location.getPlaceId() == null) {
            UIUtils.showSnackbar(parent, getString(R.string.no_place));
        } else if (frequencyText.isEmpty()) {
                UIUtils.showSnackbar(parent, getString(R.string.no_frequency));
        } else {
            int pokemonId = PokemonServer.get().getPokemonId(pokemonName);
            if (DatabaseManager.get().getFinding(pokemonId, location) != null) {
                String dupeMessage = String.format(getString(R.string.already_submitted),
                        pokemonName, location.getDisplayName());
                UIUtils.showSnackbar(parent, dupeMessage);
            } else {
                float frequencyValue = PokemonUtils.getFrequency(currentFrequencyIndex);

                progressDialog.show();
                AddPokemonRequest addPokemonRequest = new AddPokemonRequest();
                addPokemonRequest.setLocation(location);
                addPokemonRequest.addPokemon(pokemonId, frequencyValue);
                RestClient.get().getPokemonService()
                        .addPokemon(addPokemonRequest)
                        .enqueue(new AddPokemonCallback(pokemonId, location, frequencyText));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
