package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.StatusInfo;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 7/31/16.
 */
public class StatusCallback implements Callback<StatusInfo> {
    @Override
    public void onResponse(Call<StatusInfo> call, Response<StatusInfo> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            if (response.body().getMaxVersion() >= MyApplication.getVersionCode()) {
                PreferencesManager.get().enableImages();
            }
        }
    }

    @Override
    public void onFailure(Call<StatusInfo> call, Throwable t) {}
}
