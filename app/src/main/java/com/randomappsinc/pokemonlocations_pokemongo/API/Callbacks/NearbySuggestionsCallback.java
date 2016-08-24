package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.LocationsResult;
import com.randomappsinc.pokemonlocations_pokemongo.API.PokeLocationsEvent;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.AddListingActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 8/24/16.
 */
public class NearbySuggestionsCallback implements Callback<LocationsResult> {
    public static final int NUM_NEARBY_SUGGESTIONS = 10;

    @Override
    public void onResponse(Call<LocationsResult> call, Response<LocationsResult> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            PokeLocationsEvent event = new PokeLocationsEvent();
            event.setScreen(AddListingActivity.SCREEN_NAME);

            List<PokeLocation> suggestions = new ArrayList<>();
            for (int i = 0; i < NUM_NEARBY_SUGGESTIONS && i < response.body().getLocations().size(); i++) {
                suggestions.add(response.body().getLocations().get(i));
            }
            event.setLocations(suggestions);

            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onFailure(Call<LocationsResult> call, Throwable t) {}
}
