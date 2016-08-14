package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonACAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Filter;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.LocationUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;

/**
 * Created by alexanderchiou on 8/4/16.
 */
public class FilterActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.pokemon_name) AutoCompleteTextView pokemonInput;
    @Bind(R.id.clear_pokemon) View clearPokemon;
    @Bind(R.id.current_location) EditText currentLocationInput;
    @Bind({R.id.nearby_toggle, R.id.very_close_toggle, R.id.close_toggle,
           R.id.far_toggle, R.id.very_far_toggle}) List<CheckBox> distanceOptions;
    @Bind(R.id.nearby_text) TextView nearbyText;
    @Bind(R.id.very_close_text) TextView veryCloseText;
    @Bind(R.id.close_text) TextView closeText;
    @Bind(R.id.far_text) TextView farText;
    @Bind(R.id.very_far_text) TextView veryFarText;

    private MaterialDialog processingLocation;
    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = getIntent().getParcelableExtra(Filter.KEY);
        pokemonInput.setAdapter(new PokemonACAdapter(this, R.layout.pokemon_ac_item, new ArrayList<String>()));

        if (filter.getPokemonId() > 0) {
            pokemonInput.setText(PokemonServer.get().getPokemonName(filter.getPokemonId()));
        }

        String currentLocation = PreferencesManager.get().getCurrentLocation();
        currentLocationInput.setText(currentLocation);

        CheckBox checkBox = distanceOptions.get(filter.getDistanceIndex());
        checkBox.setCheckedImmediately(true);
        checkBox.setClickable(false);

        processingLocation = new MaterialDialog.Builder(this)
                .content(R.string.processing_location)
                .progress(true, 0)
                .cancelable(false)
                .build();

        if (PreferencesManager.get().getIsAmerican()) {
            nearbyText.setText(R.string.nearby);
            veryCloseText.setText(R.string.very_close);
            closeText.setText(R.string.close_option);
            farText.setText(R.string.far);
            veryFarText.setText(R.string.very_far);
        } else {
            nearbyText.setText(R.string.nearby_km);
            veryCloseText.setText(R.string.very_close_km);
            closeText.setText(R.string.close_option_km);
            farText.setText(R.string.far_km);
            veryFarText.setText(R.string.very_far_km);
        }
    }

    @OnClick(R.id.clear_pokemon)
    public void clearPokemon() {
        pokemonInput.setText("");
    }

    @OnTextChanged(value = R.id.pokemon_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        if (input.length() == 0) {
            clearPokemon.setVisibility(View.GONE);
        } else {
            clearPokemon.setVisibility(View.VISIBLE);
            if (PokemonServer.get().isValidPokemon(input.toString())) {
                UIUtils.hideKeyboard(this);
            }
        }
    }

    // Distance checkbox clicked
    @OnClick({R.id.nearby_toggle, R.id.very_close_toggle, R.id.close_toggle,
              R.id.far_toggle, R.id.very_far_toggle})
    public void distanceToggleClick(View view) {
        CheckBox currentlyCheckedIn = distanceOptions.get(filter.getDistanceIndex());

        // If they're checking in something new, do the check, change radius
        if (currentlyCheckedIn.getId() != view.getId()) {
            currentlyCheckedIn.setChecked(false);
            currentlyCheckedIn.setClickable(true);
            for (int i = 0; i < distanceOptions.size(); i++) {
                // We found the index of the checkbox they clicked
                if (view.getId() == distanceOptions.get(i).getId()) {
                    filter.setDistanceIndex(i);
                    distanceOptions.get(i).setClickable(false);
                }
            }
        }
    }

    // Distance container clicked
    @OnClick({R.id.nearby, R.id.very_close, R.id.close, R.id.far, R.id.very_far})
    public void distanceBoxClick(View view) {
        CheckBox currentlyCheckedIn = distanceOptions.get(filter.getDistanceIndex());

        int checkboxId = LocationUtils.getCheckboxId(view.getId());

        // If they're checking in something new, do the check, change radius
        if (currentlyCheckedIn.getId() != checkboxId) {
            currentlyCheckedIn.setChecked(false);
            currentlyCheckedIn.setClickable(true);
            for (int i = 0; i < distanceOptions.size(); i++) {
                // We found the index of the container they clicked
                if (checkboxId == distanceOptions.get(i).getId()) {
                    filter.setDistanceIndex(i);
                    distanceOptions.get(i).setChecked(true);
                    distanceOptions.get(i).setClickable(false);
                }
            }
        }
    }

    @OnClick(R.id.current_location)
    public void setLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.choose_current_location)
                .content(R.string.current_instructions)
                .items(DatabaseManager.get().getLocationsArray())
                .itemsCallbackSingleChoice(DatabaseManager.get().getCurrentLocationIndex(),
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                PreferencesManager.get().setCurrentLocation(text.toString());
                                currentLocationInput.setText(text.toString());
                                return true;
                            }
                        })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.cancel)
                .neutralText(R.string.add_location_title)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addLocation();
                    }
                })
                .show();
    }

    private void addLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.add_location_title)
                .input(getString(R.string.location), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        String locationInput = input.toString().trim();
                        boolean submitEnabled = !(locationInput.isEmpty()
                                || DatabaseManager.get().alreadyHasLocation(locationInput));
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(submitEnabled);
                    }
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.add)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newLocation = dialog.getInputEditText().getText().toString();
                        processLocation(newLocation);
                    }
                })
                .show();
    }

    public void processLocation(final String locationName) {
        processingLocation.show();
        SmartLocation.with(this).geocoding()
                .direct(locationName, new OnGeocodingListener() {
                    @Override
                    public void onLocationResolved(String name, List<LocationAddress> results) {
                        if (!results.isEmpty()) {
                            Location location = results.get(0).getLocation();
                            SavedLocationDO locationDO = new SavedLocationDO();
                            locationDO.setDisplayName(locationName);
                            locationDO.setLatitude(location.getLatitude());
                            locationDO.setLongitude(location.getLongitude());
                            DatabaseManager.get().addMyLocation(locationDO);
                            currentLocationInput.setText(locationDO.getDisplayName());
                            PreferencesManager.get().setCurrentLocation(locationDO.getDisplayName());
                            processingLocation.dismiss();
                        } else {
                            processingLocation.dismiss();
                            UIUtils.showSnackbar(parent, getString(R.string.process_location_failed));
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.perform_search, IoniconsIcons.ion_checkmark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.perform_search:
                String pokemonName = pokemonInput.getText().toString();
                if (pokemonName.isEmpty() || PokemonServer.get().isValidPokemon(pokemonName)) {
                    if (!pokemonName.isEmpty()) {
                        filter.setPokemonId(PokemonServer.get().getPokemonId(pokemonName));
                    } else {
                        filter.setPokemonId(0);
                    }
                    Intent intent = new Intent();
                    intent.putExtra(Filter.KEY, filter);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    UIUtils.showSnackbar(parent, getString(R.string.invalid_pokemon));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
