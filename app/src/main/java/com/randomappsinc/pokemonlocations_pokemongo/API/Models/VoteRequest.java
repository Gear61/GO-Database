package com.randomappsinc.pokemonlocations_pokemongo.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 7/19/16.
 */
public class VoteRequest {
    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("amount")
    @Expose
    private int amount;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
