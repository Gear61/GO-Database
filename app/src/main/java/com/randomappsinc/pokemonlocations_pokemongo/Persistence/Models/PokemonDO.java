package com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models;

import io.realm.RealmObject;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonDO extends RealmObject {
    private int pokemonId;

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }
}
