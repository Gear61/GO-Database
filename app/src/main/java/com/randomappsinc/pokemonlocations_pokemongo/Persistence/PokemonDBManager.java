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
    public static final int CURRENT_POKEMON_DB_VERSION = 3;

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

    public List<Pokemon> getPokemonRanked(int sortOption) {
        List<Pokemon> pokemon = new ArrayList<>();

        List<PokedexPokemonDO> pokemonDOs = new ArrayList<>();

        switch (sortOption) {
            case 0:
                pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("maxCpRanking");
                break;
            case 1:
                pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("attackRanking");
                break;
            case 2:
                pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("defenseRanking");
                break;
            case 3:
                pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("staminaRanking");
                break;
            case 4:
                pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("captureRateRanking");
                break;
            case 5:
                pokemonDOs = getRealm().where(PokedexPokemonDO.class).findAllSorted("fleeRateRanking");
                break;
        }

        for (PokedexPokemonDO pokemonDO : pokemonDOs) {
            pokemon.add(DBConverters.getPokemonFromDO(pokemonDO));
        }

        return pokemon;
    }
}
