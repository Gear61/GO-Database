package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.StatusInfo;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 7/31/16.
 */
public class StatusCallback implements Callback<StatusInfo> {
    public static final String UPDATE_NEEDED = "updateNeeded";

    @Override
    public void onResponse(Call<StatusInfo> call, Response<StatusInfo> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            if (response.body().getMaxVersion() >= MyApplication.getVersionCode()) {
                PreferencesManager.get().enableImages();
            }
            if (response.body().getMinVersion() > MyApplication.getVersionCode()) {
                EventBus.getDefault().post(UPDATE_NEEDED);
            }
        }
    }

    @Override
    public void onFailure(Call<StatusInfo> call, Throwable t) {}
}
