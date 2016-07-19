package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.BlankResponse;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 7/18/16.
 */
public class AddPokemonCallback implements Callback<BlankResponse> {
    public static final String ADD_POKEMON_SUCCESS = "addPokemonSuccess";
    public static final String ADD_POKEMON_FAILURE = "addPokemonFailure";

    private PokeFindingDO findingDO;

    public AddPokemonCallback(int pokemonId, PokeLocation location, String frequency) {
        findingDO = new PokeFindingDO();
        findingDO.setPokemonId(pokemonId);
        findingDO.setPlaceId(location.getPlaceId());
        findingDO.setLocationName(location.getDisplayName());
        findingDO.setFrequency(frequency);
    }

    @Override
    public void onResponse(Call<BlankResponse> call, Response<BlankResponse> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            DatabaseManager.get().addPokeFinding(findingDO);
            EventBus.getDefault().post(ADD_POKEMON_SUCCESS);
        } else {
            EventBus.getDefault().post(ADD_POKEMON_FAILURE);
        }
    }

    @Override
    public void onFailure(Call<BlankResponse> call, Throwable t) {
        EventBus.getDefault().post(ADD_POKEMON_FAILURE);
    }
}
