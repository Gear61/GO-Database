package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.LocationsResult;
import com.randomappsinc.pokemonlocations_pokemongo.API.PokeLocationsEvent;
import com.randomappsinc.pokemonlocations_pokemongo.Fragments.SearchFragment;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 7/19/16.
 */
public class SearchCallback implements Callback<LocationsResult> {
    public static final String SEARCH_FAIL = "searchFail";

    @Override
    public void onResponse(Call<LocationsResult> call, Response<LocationsResult> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            EventBus.getDefault().post(new PokeLocationsEvent(SearchFragment.SCREEN_NAME, response.body().getLocations()));
        } else {
            EventBus.getDefault().post(SEARCH_FAIL);
        }
    }

    @Override
    public void onFailure(Call<LocationsResult> call, Throwable error) {
        // Make sure not to send out failure events if the call was canceled
        if (error != null && error.getMessage() != null) {
            String errorMessage = error.getMessage().toLowerCase();
            if (!(errorMessage.equals(ApiConstants.CANCELED)
                    || errorMessage.equals(ApiConstants.SOCKET_CLOSED)
                    || errorMessage.equals(ApiConstants.UNEXPECTED_STREAM_END))) {
                EventBus.getDefault().post(SEARCH_FAIL);
            }
        }
    }
}
