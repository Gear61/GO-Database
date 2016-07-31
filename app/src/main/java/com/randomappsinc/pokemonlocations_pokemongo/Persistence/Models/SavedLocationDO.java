package com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models;

import io.realm.RealmObject;

/**
 * Created by alexanderchiou on 7/29/16.
 */
public class SavedLocationDO extends RealmObject {
    private String displayName;
    private double latitude;
    private double longitude;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
