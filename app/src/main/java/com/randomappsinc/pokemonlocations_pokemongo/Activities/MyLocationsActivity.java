package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.LocationsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;

/**
 * Created by alexanderchiou on 7/31/16.
 */
public class MyLocationsActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.location_input) EditText locationInput;
    @Bind(R.id.plus_icon) ImageView plusIcon;
    @Bind(R.id.locations) ListView locations;
    @Bind(R.id.no_locations) View noLocations;

    private LocationsAdapter locationsAdapter;
    private MaterialDialog processingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_locations);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        plusIcon.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_android_add).colorRes(R.color.white));

        locationsAdapter = new LocationsAdapter(this, noLocations, parent);
        locations.setAdapter(locationsAdapter);

        processingLocation = new MaterialDialog.Builder(this)
                .content(R.string.processing_location)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    @OnClick(R.id.add_location)
    public void onAddClick() {
        UIUtils.hideKeyboard(this);
        String locationName = locationInput.getText().toString();
        if (locationName.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.empty_location));
        } else if (DatabaseManager.get().alreadyHasLocation(locationName)) {
            UIUtils.showSnackbar(parent, getString(R.string.duplicate_location));
        } else {
            processLocation(locationName, true, 0);
        }
    }

    public void processLocation(final String newName, final boolean addMode, final int position) {
        processingLocation.show();
        SmartLocation.with(this).geocoding()
                .direct(newName, new OnGeocodingListener() {
                    @Override
                    public void onLocationResolved(String name, List<LocationAddress> results) {
                        if (!results.isEmpty()) {
                            Location location = results.get(0).getLocation();
                            SavedLocationDO locationDO = new SavedLocationDO();
                            locationDO.setDisplayName(newName);
                            locationDO.setLatitude(location.getLatitude());
                            locationDO.setLongitude(location.getLongitude());
                            if (addMode) {
                                addLocation(locationDO);
                            } else {
                                editLocation(position, locationDO);
                            }
                        } else {
                            processingLocation.dismiss();
                            UIUtils.showSnackbar(parent, getString(R.string.process_location_failed));
                        }
                    }
                });
    }

    private void addLocation(SavedLocationDO locationDO) {
        locationInput.setText("");
        processingLocation.dismiss();
        locationsAdapter.addLocation(locationDO);
    }

    private void editLocation(int position, SavedLocationDO savedLocationDO) {
        processingLocation.dismiss();
        locationsAdapter.editLocation(position, savedLocationDO);
    }

    @OnItemClick(R.id.locations)
    public void showLocationOptions(int position) {
        locationsAdapter.showOptionsDialog(position);
    }
}