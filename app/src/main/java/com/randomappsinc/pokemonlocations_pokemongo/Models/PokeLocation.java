package com.randomappsinc.pokemonlocations_pokemongo.Models;

import java.util.List;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocation {
    private int locationId;
    private String displayName;
    private int score;
    private float latitude;
    private float longitude;
    private List<Integer> commonPokemon;
    private List<Integer> uncommonPokemon;
    private List<Integer> rarePokemon;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public List<Integer> getCommonPokemon() {
        return commonPokemon;
    }

    public void setCommonPokemon(List<Integer> commonPokemon) {
        this.commonPokemon = commonPokemon;
    }

    public List<Integer> getUncommonPokemon() {
        return uncommonPokemon;
    }

    public void setUncommonPokemon(List<Integer> uncommonPokemon) {
        this.uncommonPokemon = uncommonPokemon;
    }

    public List<Integer> getRarePokemon() {
        return rarePokemon;
    }

    public void setRarePokemon(List<Integer> rarePokemon) {
        this.rarePokemon = rarePokemon;
    }
}
