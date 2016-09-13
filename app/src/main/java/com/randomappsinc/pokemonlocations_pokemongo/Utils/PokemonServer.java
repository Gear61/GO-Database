package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;

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

    private List<String> pokemonNamesList;
    private Map<String, Integer> nameToIdMappings;
    private Map<Integer, String> idToNameMappings;
    private List<Pokemon> pokemonList;

    private PokemonServer() {
        pokemonNamesList = new ArrayList<>();
        nameToIdMappings = new HashMap<>();
        idToNameMappings = new HashMap<>();
        pokemonList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(MyApplication.getAppContext()
                    .getAssets().open("pokemon.txt")));
            String pokemon;
            int currentIndex = 1;
            while ((pokemon = reader.readLine()) != null) {
                pokemonNamesList.add(pokemon);
                nameToIdMappings.put(pokemon.toLowerCase(), currentIndex);
                idToNameMappings.put(currentIndex, pokemon);
                pokemonList.add(new Pokemon(currentIndex, pokemon));
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
        Collections.sort(pokemonNamesList);
    }

    public boolean isValidPokemon(String input) {
        return nameToIdMappings.keySet().contains(input.toLowerCase());
    }

    public List<String> getMatchingPokemon(String prefix) {
        return MatchingUtils.getMatchingItems(prefix, pokemonNamesList);
    }

    public int getPokemonId(String pokemonName) {
        return nameToIdMappings.get(pokemonName.toLowerCase());
    }

    public String getPokemonName(int id) {
        return idToNameMappings.get(id);
    }

    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    public boolean isUnreleased(String pokemonName) {
        int pokemonId = getPokemonId(pokemonName);
        return pokemonId == 132 || pokemonId == 144 || pokemonId == 145 ||
                pokemonId == 146 || pokemonId == 150 || pokemonId == 151;
    }

    public boolean cantBeCommon(String pokemonName) {
        int pokemonId = getPokemonId(pokemonName);
        return pokemonId == 3 || pokemonId == 6 || pokemonId == 9 || pokemonId == 31 || pokemonId == 34
                || pokemonId == 45 || pokemonId == 62 || pokemonId == 65 || pokemonId == 68 || pokemonId == 71
                || pokemonId == 76 || pokemonId == 94 || pokemonId == 103 || pokemonId == 130 || pokemonId == 131
                || pokemonId == 134 || pokemonId == 135 || pokemonId == 136 || pokemonId == 143 || pokemonId == 149;
    }
}
