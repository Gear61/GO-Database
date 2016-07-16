package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class PokemonServer {
    private static PokemonServer instance;
    private static List<String> pokemonList = new ArrayList<>();

    private PokemonServer() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(MyApplication.getAppContext()
                    .getAssets().open("pokemon.txt")));
            String pokemon;
            while ((pokemon = reader.readLine()) != null) {
                pokemonList.add(pokemon);
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

    public static PokemonServer get() {
        if (instance == null) {
            instance = new PokemonServer();
        }
        return instance;
    }

    public boolean isValidPokemon(String input) {
        String cleanName = input.substring(0, 1).toUpperCase() + input.substring(1);
        return pokemonList.contains(cleanName);
    }

    public List<String> getMatchingPokemon(String prefix) {
        int indexOfMatch = binarySearch(prefix);
        if (indexOfMatch == -1) {
            return new ArrayList<>();
        } else {
            List<String> matchingPokemon = new ArrayList<>();
            matchingPokemon.add(pokemonList.get(indexOfMatch));

            // Grab everything from the left and right of the matching index that also matches
            String cleanPrefix = prefix.toLowerCase();
            for (int i = indexOfMatch - 1; i >= 0; i--) {
                String pokemonSubstring = getSubstring(pokemonList.get(i), prefix);
                if (pokemonSubstring.equals(cleanPrefix)) {
                        matchingPokemon.add(0, pokemonList.get(i));
                } else {
                    break;
                }
            }
            for (int i = indexOfMatch + 1; i < pokemonList.size(); i++) {
                String pokemonSubstring = getSubstring(pokemonList.get(i), prefix).toLowerCase();
                if (pokemonSubstring.equals(cleanPrefix)) {
                    matchingPokemon.add(pokemonList.get(i));
                } else {
                    break;
                }
            }
            return matchingPokemon;
        }
    }

    // Returns index of first word with given prefix (-1 if it's not found)
    private int binarySearch(String prefix) {
        String cleanPrefix = prefix.toLowerCase();
        int lo = 0;
        int hi = pokemonList.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int compareToValue = cleanPrefix.compareTo(getSubstring(pokemonList.get(mid), cleanPrefix));
            if (compareToValue < 0) {
                hi = mid - 1;
            } else if (compareToValue > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // Get the first X characters of candidate, where X is the search input's length
    private String getSubstring(String main, String prefix) {
        if (prefix.length() > main.length()) {
            return main.toLowerCase();
        }
        return main.substring(0, prefix.length()).toLowerCase();
    }
}
