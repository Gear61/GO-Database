package com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 9/6/16.
 */
public class EditRarityRequest {
    @SerializedName("pokemon_id")
    @Expose
    private int pokemonId;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("delta")
    @Expose
    private float delta;

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }
}
