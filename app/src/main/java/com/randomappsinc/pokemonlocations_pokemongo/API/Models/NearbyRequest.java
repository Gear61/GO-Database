package com.randomappsinc.pokemonlocations_pokemongo.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 7/29/16.
 */
public class NearbyRequest {
    @SerializedName("location")
    @Expose
    private LatLong location;

    public void setLocation(double latitude, double longitude) {
        this.location = new LatLong();
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
    }
}
