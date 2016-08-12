package com.randomappsinc.pokemonlocations_pokemongo.API;

import android.os.AsyncTask;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by alexanderchiou on 8/11/16.
 */
public class PlaceSuggestionsClient {
    private GoogleApiClient client;
    private PendingResult<AutocompletePredictionBuffer> results;

    public static final LatLngBounds ENTIRE_WORLD = new LatLngBounds(new LatLng(-90, 0), new LatLng(90, 180));

    public PlaceSuggestionsClient(GoogleApiClient client) {
        this.client = client;
    }

    public void doSearch(String input) {
        new SearchTask(input).execute();
    }

    public void cancelCalls() {
        new CancelCalls().execute();
    }

    private class CancelCalls extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (results != null) {
                results.cancel();
            }
            return null;
        }
    }

    private class SearchTask extends AsyncTask<Void, Void, Void> {
        private String input;

        public SearchTask(String input) {
            this.input = input;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (results != null) {
                results.cancel();
            }
            results = Places.GeoDataApi.getAutocompletePredictions(client, input, ENTIRE_WORLD, null);

            AutocompletePredictionBuffer autocompletePredictions = results.await(5, TimeUnit.SECONDS);

            final com.google.android.gms.common.api.Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                autocompletePredictions.release();
            } else {
                List<PokeLocation> results = new ArrayList<>();

                for (int i = 0; i < autocompletePredictions.getCount(); i++) {
                    String result = autocompletePredictions.get(i).getFullText(null).toString();
                    PokeLocation location = new PokeLocation();
                    int indexOfComma = result.indexOf(",");
                    if (indexOfComma != -1) {
                        location.setDisplayName(result.substring(0, indexOfComma));
                        if (result.length() > indexOfComma + 1) {
                            location.setAddress(result.substring(indexOfComma + 1).trim());
                        } else {
                            location.setAddress("");
                        }
                    } else {
                        location.setDisplayName(result);
                        location.setAddress("");
                    }

                    results.add(location);
                }

                LocationsEvent locationsEvent = new LocationsEvent(input, results);
                EventBus.getDefault().post(locationsEvent);
            }
            return null;
        }
    }
}
