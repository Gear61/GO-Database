package com.randomappsinc.pokemonlocations_pokemongo.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/19/16.
 */
public class SearchResults {
    @SerializedName("locations")
    @Expose
    private List<PokeLocation> locations = new ArrayList<>();

    public List<PokeLocation> getLocations() {
        return locations;
    }
}
