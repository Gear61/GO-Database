package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.randomappsinc.pokemonlocations_pokemongo.API.PlaceSuggestionsClient;
import com.randomappsinc.pokemonlocations_pokemongo.API.LocationsEvent;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PlaceSuggestionsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 8/11/16.
 */
public class SelectLocationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.location_input) EditText locationInput;
    @Bind(R.id.place_suggestions) ListView results;
    @Bind(R.id.powered_by_google) View googlePowered;

    private GoogleApiClient googleApiClient;
    private PlaceSuggestionsClient locationClient;
    private PlaceSuggestionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_location);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        googleApiClient.connect();

        locationClient = new PlaceSuggestionsClient(googleApiClient);

        adapter = new PlaceSuggestionsAdapter(this, googlePowered);
        results.setAdapter(adapter);
    }

    @OnTextChanged(value = R.id.location_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        if (googleApiClient.isConnected() && input.length() > 0) {
            locationClient.doSearch(input.toString());
        } else {
            adapter.setSuggestions(new ArrayList<PokeLocation>());
        }
    }

    @OnClick(R.id.clear_location)
    public void clearLocation() {
        locationInput.setText("");
    }

    @OnItemClick(R.id.place_suggestions)
    public void chooseResult() {

    }

    @Subscribe
    public void onEvent(final LocationsEvent event) {
        if (event.getInput().equals(locationInput.getText().toString())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setSuggestions(event.getResults());
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        UIUtils.showSnackbar(parent, getString(R.string.google_locations_down));
    }

    @Override
    public void finish() {
        UIUtils.hideKeyboard(this);
        googleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
        locationClient.cancelCalls();

        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
