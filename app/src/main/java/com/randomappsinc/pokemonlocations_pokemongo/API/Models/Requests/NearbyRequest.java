package com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;

/**
 * Created by alexanderchiou on 7/29/16.
 */
public class NearbyRequest {
    @SerializedName("range")
    @Expose
    private double range;

    @SerializedName("location")
    @Expose
    private LatLong location;

    public void setRange(double range) {
        this.range = range;
    }

    public void setLocation(double latitude, double longitude) {
        this.location = new LatLong();
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
    }
}
