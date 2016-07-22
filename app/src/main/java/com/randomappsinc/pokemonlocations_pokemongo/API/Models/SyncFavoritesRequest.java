package com.randomappsinc.pokemonlocations_pokemongo.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexanderchiou on 7/21/16.
 */
public class SyncFavoritesRequest {
    @SerializedName("place_ids")
    @Expose
    private List<String> placeIds;

    public void setPlaceIds(List<String> placeIds) {
        this.placeIds = placeIds;
    }
}
