package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokedexPokemonDO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by alexanderchiou on 9/23/16.
 */

public class PokemonDBManager {
    public static final int CURRENT_POKEMON_DB_VERSION = 1;
    public static final int CURRENT_RANKINGS_DB_VERSION = 1;
    private Realm realm;

    public PokemonDBManager(Realm realm) {
        this.realm = realm;
    }

    public void setupRankings() {
        if (PreferencesManager.get().getRankingsDBVersion() < CURRENT_RANKINGS_DB_VERSION) {
            List<Pokemon> pokemonList = getPokemon();

            RealmResults<PokedexPokemonDO> pokemonDOs = realm.where(PokedexPokemonDO.class).findAll();

            pokemonDOs.sort("maxCp", Sort.DESCENDING);
            pokemonList.get(pokemonDOs.get(0).getPokemonId() - 1).setMaxCpRanking(1);
            for (int i = 1; i < pokemonDOs.size(); i++) {
                int previousStat = pokemonDOs.get(i - 1).getMaxCp();
                int currentStat = pokemonDOs.get(i).getMaxCp();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(pokemonDOs.get(i - 1).getPokemonId() - 1).getMaxCpRanking();
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setMaxCpRanking(previousRanking);
                } else {
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setMaxCpRanking(i + 1);
                }
            }

            pokemonDOs.sort("baseAttack", Sort.DESCENDING);
            pokemonList.get(pokemonDOs.get(0).getPokemonId() - 1).setAttackRanking(1);
            for (int i = 1; i < pokemonDOs.size(); i++) {
                int previousStat = pokemonDOs.get(i - 1).getBaseAttack();
                int currentStat = pokemonDOs.get(i).getBaseAttack();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(pokemonDOs.get(i - 1).getPokemonId() - 1).getAttackRanking();
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setAttackRanking(previousRanking);
                } else {
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setAttackRanking(i + 1);
                }
            }

            pokemonDOs.sort("baseDefense", Sort.DESCENDING);
            pokemonList.get(pokemonDOs.get(0).getPokemonId() - 1).setDefenseRanking(1);
            for (int i = 1; i < pokemonDOs.size(); i++) {
                int previousStat = pokemonDOs.get(i - 1).getBaseDefense();
                int currentStat = pokemonDOs.get(i).getBaseDefense();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(pokemonDOs.get(i - 1).getPokemonId() - 1).getDefenseRanking();
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setDefenseRanking(previousRanking);
                } else {
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setDefenseRanking(i + 1);
                }
            }

            pokemonDOs.sort("baseStamina", Sort.DESCENDING);
            pokemonList.get(pokemonDOs.get(0).getPokemonId() - 1).setStaminaRanking(1);
            for (int i = 1; i < pokemonDOs.size(); i++) {
                int previousStat = pokemonDOs.get(i - 1).getBaseStamina();
                int currentStat = pokemonDOs.get(i).getBaseStamina();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(pokemonDOs.get(i - 1).getPokemonId() - 1).getStaminaRanking();
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setStaminaRanking(previousRanking);
                } else {
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setStaminaRanking(i + 1);
                }
            }

            pokemonDOs.sort("baseCaptureRate", Sort.DESCENDING);
            pokemonList.get(pokemonDOs.get(0).getPokemonId() - 1).setCaptureRateRanking(1);
            for (int i = 1; i < pokemonDOs.size(); i++) {
                int previousStat = pokemonDOs.get(i - 1).getBaseCaptureRate();
                int currentStat = pokemonDOs.get(i).getBaseCaptureRate();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(pokemonDOs.get(i - 1).getPokemonId() - 1).getCaptureRateRanking();
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setCaptureRateRanking(previousRanking);
                } else {
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setCaptureRateRanking(i + 1);
                }
            }

            pokemonDOs.sort("baseFleeRate", Sort.DESCENDING);
            pokemonList.get(pokemonDOs.get(0).getPokemonId() - 1).setFleeRateRanking(1);
            for (int i = 1; i < pokemonDOs.size(); i++) {
                int previousStat = pokemonDOs.get(i - 1).getBaseFleeRate();
                int currentStat = pokemonDOs.get(i).getBaseFleeRate();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(pokemonDOs.get(i - 1).getPokemonId() - 1).getFleeRateRanking();
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setFleeRateRanking(previousRanking);
                } else {
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setFleeRateRanking(i + 1);
                }
            }

            pokemonDOs.sort("avgCpGain", Sort.DESCENDING);
            pokemonList.get(pokemonDOs.get(0).getPokemonId() - 1).setCpGainRanking(1);
            for (int i = 1; i < pokemonDOs.size(); i++) {
                double previousStat = pokemonDOs.get(i - 1).getAvgCpGain();
                double currentStat = pokemonDOs.get(i).getAvgCpGain();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(pokemonDOs.get(i - 1).getPokemonId() - 1).getCpGainRanking();
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setCpGainRanking(previousRanking);
                } else {
                    pokemonList.get(pokemonDOs.get(i).getPokemonId() - 1).setCpGainRanking(i + 1);
                }
            }

            for (Pokemon pokemon : pokemonList) {
                addOrUpdatePokemon(pokemon.toPokemonDO());
            }

            PreferencesManager.get().updateRankingsDBVersion();
        }
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
                .equalTo("name", pokemonName, Case.INSENSITIVE)
                .findFirst() != null;
    }

    public int getPokemonId(String pokemonName) {
        return realm.where(PokedexPokemonDO.class)
                .equalTo("name", pokemonName, Case.INSENSITIVE)
                .findFirst()
                .getPokemonId();
    }

    public String getPokemonName(int pokemonId) {
        return realm.where(PokedexPokemonDO.class)
                .equalTo("pokemonId", pokemonId)
                .findFirst()
                .getName();
    }

    public Pokemon getPokemon(int pokemonId) {
        PokedexPokemonDO pokemonDO = realm.where(PokedexPokemonDO.class)
                .equalTo("pokemonId", pokemonId)
                .findFirst();
        return DBConverters.getPokemonFromDO(pokemonDO);
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

        List<PokedexPokemonDO> pokemonDOs = realm.where(PokedexPokemonDO.class).findAllSorted("pokemonId");
        for (PokedexPokemonDO pokemonDO : pokemonDOs) {
            pokemon.add(DBConverters.getPokemonFromDO(pokemonDO));
        }

        return pokemon;
    }
}
