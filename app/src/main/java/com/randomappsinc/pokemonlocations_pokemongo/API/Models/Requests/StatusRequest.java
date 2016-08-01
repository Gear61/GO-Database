package com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 7/31/16.
 */
public class StatusRequest {
    @SerializedName("current_version")
    @Expose
    private int currentVersion;

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }
}
