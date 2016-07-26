package com.randomappsinc.pokemonlocations_pokemongo.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 7/18/16.
 */
public class SearchRequest {
    @SerializedName("pokemon_id")
    @Expose
    private int pokemon_id;

    @SerializedName("location")
    @Expose
    private LatLong location;

    public void setPokemonId(int pokemonId) {
        this.pokemon_id = pokemonId;
    }

    public void setLocation(double latitude, double longitude) {
        this.location = new LatLong();
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
    }

    public class LatLong {
        @SerializedName("lat")
        @Expose
        private double latitude;

        @SerializedName("long")
        @Expose
        private double longitude;

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
