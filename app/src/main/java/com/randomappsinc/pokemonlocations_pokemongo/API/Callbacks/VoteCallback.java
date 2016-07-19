package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.BlankResponse;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.VoteDO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 7/19/16.
 */
public class VoteCallback implements Callback<BlankResponse> {
    private VoteDO rollback;

    public VoteCallback(int oldScore, PokeLocation place) {
        this.rollback = new VoteDO();
        this.rollback.setVote(oldScore);
        this.rollback.setPlaceId(place.getPlaceId());
    }

    @Override
    public void onResponse(Call<BlankResponse> call, Response<BlankResponse> response) {
        if (response.code() != ApiConstants.HTTP_STATUS_OK) {
            DatabaseManager.get().updateVote(rollback);
        }
    }

    @Override
    public void onFailure(Call<BlankResponse> call, Throwable t) {
        DatabaseManager.get().updateVote(rollback);
    }
}
