package com.randomappsinc.pokemonlocations_pokemongo.API;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;

import java.util.List;

/**
 * Created by alexanderchiou on 8/11/16.
 */
public class LocationsEvent {
    private String input;
    private List<PokeLocation> results;

    public LocationsEvent(String input, List<PokeLocation> results) {
        this.input = input;
        this.results = results;
    }

    public String getInput() {
        return input;
    }

    public List<PokeLocation> getResults() {
        return results;
    }
}
