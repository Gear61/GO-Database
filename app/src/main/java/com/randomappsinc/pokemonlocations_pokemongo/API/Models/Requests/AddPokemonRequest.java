package com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.PokemonPosting;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;

import java.util.List;

/**
 * Created by alexanderchiou on 7/18/16.
 */
public class AddPokemonRequest {
    @SerializedName("location")
    @Expose
    private PokeLocation location;

    @SerializedName("pokemon_data")
    @Expose
    private List<PokemonPosting> pokemonToAdd;

    public void setLocation(PokeLocation location) {
        this.location = location;
    }

    public void setPostings(List<PokemonPosting> postings) {
        this.pokemonToAdd = postings;
    }
}
