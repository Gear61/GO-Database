package com.randomappsinc.pokemonlocations_pokemongo.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;

import java.util.ArrayList;
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
        pokemonToAdd = new ArrayList<>();

        // Strip away postings the user has already submitted
        for (PokemonPosting posting : postings) {
            if (DatabaseManager.get().getFinding(posting.getPokemonId(), location) == null) {
                pokemonToAdd.add(posting);
            }
        }
    }
}
