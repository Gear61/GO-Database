package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<String> pokemonList;
    private Map<String, Integer> nameToIdMappings;
    private Map<Integer, String> idToNameMappings;

    private PokemonServer() {
        pokemonList = new ArrayList<>();
        nameToIdMappings = new HashMap<>();
        idToNameMappings = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(MyApplication.getAppContext()
                    .getAssets().open("pokemon.txt")));
            String pokemon;
            int currentIndex = 1;
            while ((pokemon = reader.readLine()) != null) {
                pokemonList.add(pokemon);
                nameToIdMappings.put(pokemon.toLowerCase(), currentIndex);
                idToNameMappings.put(currentIndex, pokemon);
                currentIndex++;
            }
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {}
            }
        }
        Collections.sort(pokemonList);
    }

    public boolean isValidPokemon(String input) {
        if (input.isEmpty()) {
            return false;
        } else {
            String cleanName = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            return pokemonList.contains(cleanName);
        }
    }

    public List<String> getMatchingPokemon(String prefix) {
        return MatchingUtils.getMatchingItems(prefix, pokemonList);
    }

    public int getPokemonId(String pokemonName) {
        return nameToIdMappings.get(pokemonName.toLowerCase());
    }

    public String getPokemonName(int id) {
        return idToNameMappings.get(id);
    }
}
