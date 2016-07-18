package com.randomappsinc.pokemonlocations_pokemongo.Utils;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonUtils {
    public static String getPokemonUrl(String pokemonName) {
        String url = "https://img.pokemondb.net/artwork/%s.jpg";
        if (pokemonName.equals("Nidoran♀")) {
            pokemonName = "nidoran-f";
        }
        if (pokemonName.equals("Nidoran♂")) {
            pokemonName = "nidoran-m";
        }
        return String.format(url, pokemonName.toLowerCase());
    }

    public static float getFrequency(int index) {
        switch (index) {
            case 0:
                return 3F;
            case 1:
                return 2F;
            case 2:
                return 1F;
            case 3:
                return -0.5F;
            default:
                return 3F;
        }
    }
}
