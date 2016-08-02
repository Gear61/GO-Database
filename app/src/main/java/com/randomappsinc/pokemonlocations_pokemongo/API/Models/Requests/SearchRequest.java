package com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;

/**
 * Created by alexanderchiou on 7/18/16.
 */
public class SearchRequest {
    @SerializedName("range")
    @Expose
    private double range;

    @SerializedName("pokemon_id")
    @Expose
    private int pokemonId;

    @SerializedName("location")
    @Expose
    private LatLong location;

    public void setRange(double range) {
        this.range = range;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public void setLocation(double latitude, double longitude) {
        this.location = new LatLong();
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
    }
}
