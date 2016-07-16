package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import java.util.List;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class PokemonServer {
    private static PokemonServer instance;
    private static List<String> pokemonList;

    private PokemonServer() {
        pokemonList = FileUtils.extractItems("pokemon.txt");
    }

    public static PokemonServer get() {
        if (instance == null) {
            instance = new PokemonServer();
        }
        return instance;
    }

    public boolean isValidPokemon(String input) {
        if (input.isEmpty()) {
            return false;
        } else {
            String cleanName = input.substring(0, 1).toUpperCase() + input.substring(1);
            return pokemonList.contains(cleanName);
        }
    }

    public List<String> getMatchingPokemon(String prefix) {
        return MatchingUtils.getMatchingItems(prefix, pokemonList);
    }
}
