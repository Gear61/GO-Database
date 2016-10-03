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
    public static final int CURRENT_POKEMON_DB_VERSION = 2;

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public void updatePokemonList(final List<Pokemon> pokemonList) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Pokemon pokemon : pokemonList) {
                    realm.copyToRealmOrUpdate(pokemon.toPokemonDO());
                }
            }
        });
    }

    public boolean isValidPokemon(String pokemonName) {
        return getRealm().where(PokedexPokemonDO.class)
                .equalTo("name", pokemonName, Case.INSENSITIVE)
                .findFirst() != null;
    }

    public int getPokemonId(String pokemonName) {
        return getRealm().where(PokedexPokemonDO.class)
                .equalTo("name", pokemonName, Case.INSENSITIVE)
                .findFirst()
                .getPokemonId();
    }

    public String getPokemonName(int pokemonId) {
        return getRealm().where(PokedexPokemonDO.class)
                .equalTo("pokemonId", pokemonId)
                .findFirst()
                .getName();
    }

    public Pokemon getPokemon(int pokemonId) {
        PokedexPokemonDO pokemonDO = getRealm().where(PokedexPokemonDO.class)
                .equalTo("pokemonId", pokemonId)
                .findFirst();
        return DBConverters.getPokemonFromDO(pokemonDO);
    }

    public List<String> getPokemonNames() {
        List<String> pokemonNames = new ArrayList<>();

        List<PokedexPokemonDO> pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("name");
        for (PokedexPokemonDO pokemonDO : pokemonDOs) {
            pokemonNames.add(pokemonDO.getName());
        }

        return pokemonNames;
    }

    public List<Pokemon> getPokemon() {
        List<Pokemon> pokemon = new ArrayList<>();

        List<PokedexPokemonDO> pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("pokemonId");
        for (PokedexPokemonDO pokemonDO : pokemonDOs) {
            pokemon.add(DBConverters.getPokemonFromDO(pokemonDO));
        }

        return pokemon;
    }
}
