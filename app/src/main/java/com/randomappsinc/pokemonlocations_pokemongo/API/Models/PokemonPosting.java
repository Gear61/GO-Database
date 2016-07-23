package com.randomappsinc.pokemonlocations_pokemongo.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexanderchiou on 7/22/16.
 */
public class PokemonPosting {
    @SerializedName("pokemon_id")
    @Expose
    private int pokemonId;

    @SerializedName("rarity")
    @Expose
    private float rarity;

    public int getPokemonId() {
        return pokemonId;
    }

    public float getRarity() {
        return rarity;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public void setRarity(float rarity) {
        this.rarity = rarity;
    }
}
