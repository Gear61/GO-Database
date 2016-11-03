package com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alexanderchiou on 9/25/16.
 */

public class EggDO extends RealmObject{
    @PrimaryKey
    private int pokemonId;

    private int distance;
    private double chance;

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}
