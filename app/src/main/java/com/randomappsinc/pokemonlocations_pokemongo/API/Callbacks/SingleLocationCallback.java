package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.LocationsResult;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 7/24/16.
 */
public class SingleLocationCallback implements Callback<LocationsResult> {
    @Override
    public void onResponse(Call<LocationsResult> call, Response<LocationsResult> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            for (PokeLocation location : response.body().getLocations()) {
                EventBus.getDefault().post(location);
            }
        }
    }

    @Override
    public void onFailure(Call<LocationsResult> call, Throwable t) {}
}
