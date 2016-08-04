package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonACAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;

/**
 * Created by alexanderchiou on 8/4/16.
 */
public class FilterActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.pokemon_name) AutoCompleteTextView pokemonInput;
    @Bind(R.id.current_location) EditText currentLocationInput;

    private MaterialDialog processingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pokemonInput.setAdapter(new PokemonACAdapter(this, R.layout.pokemon_ac_item, new ArrayList<String>()));

        String currentLocation = PreferencesManager.get().getCurrentLocation();
        currentLocationInput.setText(currentLocation);

        processingLocation = new MaterialDialog.Builder(this)
                .content(R.string.processing_location)
                .progress(true, 0)
                .cancelable(false)
                .build();
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
                            processingLocation.dismiss();
                            currentLocationInput.setText(locationDO.getDisplayName());
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
