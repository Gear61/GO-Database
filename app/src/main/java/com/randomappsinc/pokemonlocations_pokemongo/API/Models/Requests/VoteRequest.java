package com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 7/19/16.
 */
public class VoteRequest {
    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("old_value")
    @Expose
    private int oldAmount;

    @SerializedName("new_value")
    @Expose
    private int newAmount;

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setOldAmount(int oldAmount) {
        this.oldAmount = oldAmount;
    }

    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }
}
