package com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks;

import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.BlankResponse;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.EditRarityRequest;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alexanderchiou on 9/6/16.
 */
public class EditRarityCallback implements Callback<BlankResponse> {
    public static final String EDIT_RARITY_SUCCESS = "editRaritySuccess";
    public static final String EDIT_RARITY_FAILURE = "editRarityFailure";

    private PokeFindingDO findingDO;
    private float newRarityScore;

    public EditRarityCallback(PokeFindingDO pokeFindingDO, float newRarity) {
        this.findingDO = pokeFindingDO;
        this.newRarityScore = newRarity;
    }

    @Override
    public void onResponse(Call<BlankResponse> call, Response<BlankResponse> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            String newFrequency = PokemonUtils.getFrequency(newRarityScore);
            DatabaseManager.get().updatePokeFinding(findingDO, newFrequency);
            EventBus.getDefault().post(EDIT_RARITY_SUCCESS);
        } else {
            EventBus.getDefault().post(EDIT_RARITY_FAILURE);
        }
    }

    @Override
    public void onFailure(Call<BlankResponse> call, Throwable t) {
        EventBus.getDefault().post(EDIT_RARITY_FAILURE);
    }
}
