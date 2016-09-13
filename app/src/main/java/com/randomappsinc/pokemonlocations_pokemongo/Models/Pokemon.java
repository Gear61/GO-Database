package com.randomappsinc.pokemonlocations_pokemongo.Models;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class Pokemon {
    private int id;
    private String name;
    private float frequency;

    public Pokemon() {}

    public Pokemon(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }
}
