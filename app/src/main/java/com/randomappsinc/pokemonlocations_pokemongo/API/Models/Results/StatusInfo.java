package com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 7/31/16.
 */
public class StatusInfo {
    @SerializedName("max_version")
    @Expose
    private int maxVersion;

    public int getMaxVersion() {
        return maxVersion;
    }
}
