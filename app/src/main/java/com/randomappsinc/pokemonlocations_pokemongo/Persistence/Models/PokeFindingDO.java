package com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models;

import io.realm.RealmObject;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeFindingDO extends RealmObject {
    private int pokemonId;
    private String placeId;
    private String locationName;
    private String frequency;

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
