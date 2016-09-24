package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokedexPokemonDO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;

/**
 * Created by alexanderchiou on 9/23/16.
 */

public class PokemonDBManager {
    public static final int CURRENT_POKEMON_DB_VERSION = 1;
    private Realm realm;

    public PokemonDBManager(Realm realm) {
        this.realm = realm;
    }

    public void addOrUpdatePokemon(final PokedexPokemonDO pokemonDO) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(pokemonDO);
            }
        });
    }

    public boolean isValidPokemon(String pokemonName) {
        return realm.where(PokedexPokemonDO.class)
                .contains("name", pokemonName, Case.INSENSITIVE)
                .findFirst() != null;
    }

    public int getPokemonId(String pokemonName) {
        return realm.where(PokedexPokemonDO.class)
                .contains("name", pokemonName, Case.INSENSITIVE)
                .findFirst()
                .getPokemonId();
    }

    public String getPokemonName(int pokemonId) {
        return realm.where(PokedexPokemonDO.class)
                .equalTo("pokemonId", pokemonId)
                .findFirst()
                .getName();
    }

    public List<String> getPokemonNames() {
        List<String> pokemonNames = new ArrayList<>();

        List<PokedexPokemonDO> pokemonDOs = realm.where(PokedexPokemonDO.class).findAllSorted("name");
        for (PokedexPokemonDO pokemonDO : pokemonDOs) {
            pokemonNames.add(pokemonDO.getName());
        }

        return pokemonNames;
    }

    public List<Pokemon> getPokemon() {
        List<Pokemon> pokemon = new ArrayList<>();

        List<PokedexPokemonDO> pokemonDOs = realm.where(PokedexPokemonDO.class).findAll();
        for (PokedexPokemonDO pokemonDO : pokemonDOs) {
            pokemon.add(DBConverters.getPokemonFromDO(pokemonDO));
        }

        return pokemon;
    }
}
