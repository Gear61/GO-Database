package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.content.Context;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PokemonDBManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class PokemonServer {
    private static PokemonServer instance;

    public static PokemonServer get() {
        if (instance == null) {
            instance = new PokemonServer();
        }
        return instance;
    }

    private PokemonDBManager pokemonDBManager;
    private List<String> pokemonNamesList;

    private PokemonServer() {
        pokemonDBManager = DatabaseManager.get().getPokemonDBManager();
        pokemonNamesList = new ArrayList<>();
    }

    public void initialize() {
        if (PreferencesManager.get().getPokemonDBVersion() < PokemonDBManager.CURRENT_POKEMON_DB_VERSION) {
            InputStream inputStream = null;
            try {
                inputStream = MyApplication.getAppContext().getAssets().open("pokemon.txt");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);

                JSONUtils.extractPokemon(new String(buffer));
            } catch (IOException ignored) {
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception ignored) {}
                }
            }

            PreferencesManager.get().updatePokemonDBVersion();
        }

        pokemonNamesList.addAll(pokemonDBManager.getPokemonNames());
    }

    public boolean isValidPokemon(String input) {
        return pokemonDBManager.isValidPokemon(input);
    }

    public List<String> getMatchingPokemon(String prefix) {
        return MatchingUtils.getMatchingItems(prefix, pokemonNamesList);
    }

    public int getPokemonId(String pokemonName) {
        return pokemonDBManager.getPokemonId(pokemonName);
    }

    public String getPokemonName(int id) {
        return pokemonDBManager.getPokemonName(id);
    }

    public boolean isUnreleased(String pokemonName) {
        int pokemonId = getPokemonId(pokemonName);
        return pokemonId == 132 || pokemonId == 144 || pokemonId == 145 ||
                pokemonId == 146 || pokemonId == 150 || pokemonId == 151;
    }

    public String isRegionExclusive(Pokemon pokemon) {
        Context context = MyApplication.getAppContext();

        int pokemonId = pokemon.getId();
        switch (pokemonId) {
            case 128:
                return context.getString(R.string.north_america);
            case 115:
                return context.getString(R.string.australasia);
            case 83:
                return context.getString(R.string.asia);
            case 122:
                return context.getString(R.string.europe);
            default:
                return "";
        }
    }

    public boolean cantBeCommon(String pokemonName) {
        int pokemonId = getPokemonId(pokemonName);
        return pokemonId == 3 || pokemonId == 6 || pokemonId == 9 || pokemonId == 31 || pokemonId == 34
                || pokemonId == 45 || pokemonId == 62 || pokemonId == 65 || pokemonId == 68 || pokemonId == 71
                || pokemonId == 76 || pokemonId == 94 || pokemonId == 103 || pokemonId == 130 || pokemonId == 131
                || pokemonId == 134 || pokemonId == 135 || pokemonId == 136 || pokemonId == 143 || pokemonId == 149;
    }
}
