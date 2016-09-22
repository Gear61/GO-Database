package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 9/21/16.
 */

public class JSONUtils {
    public static final String POKEMON_KEY = "pokemon";
    public static final String NAME_KEY = "name";
    public static final String TYPE_1_KEY = "type1";
    public static final String TYPE_2_KEY = "type2";
    public static final String BASE_DEFENSE_KEY = "base_defense";
    public static final String BASE_STAMINA_KEY = "base_stamina";
    public static final String BASE_ATTACK_KEY = "base_attack";
    public static final String MAX_CP_KEY = "max_cp";
    public static final String BASE_CAPTURE_RATE_KEY = "base_capture_rate";
    public static final String BASE_FLEE_RATE_KEY = "base_flee_rate";
    public static final String CANDY_TO_EVOLVE_KEY = "candy_to_evolve";
    public static final String AVERAGE_CP_GAIN_KEY = "average_cp_gain";

    public static List<Pokemon> extractPokemon(String jsonText) {
        List<Pokemon> pokemonList = new ArrayList<>();

        try {
            JSONObject pokemonListJson = new JSONObject(jsonText);
            JSONArray pokemonArray = pokemonListJson.getJSONArray(POKEMON_KEY);
            for (int i = 0; i < pokemonArray.length(); i++) {
                JSONObject pokemonJSON = pokemonArray.getJSONObject(i);

                Pokemon pokemon = new Pokemon();
                pokemon.setId(i + 1);
                pokemon.setName(pokemonJSON.getString(NAME_KEY));
                pokemon.setType1(pokemonJSON.getString(TYPE_1_KEY));
                pokemon.setType2(pokemonJSON.getString(TYPE_2_KEY));
                pokemon.setBaseDefense(pokemonJSON.getInt(BASE_DEFENSE_KEY));
                pokemon.setBaseStamina(pokemonJSON.getInt(BASE_STAMINA_KEY));
                pokemon.setBaseAttack(pokemonJSON.getInt(BASE_ATTACK_KEY));
                pokemon.setMaxCp(pokemonJSON.getInt(MAX_CP_KEY));
                pokemon.setBaseCaptureRate(pokemonJSON.getInt(BASE_CAPTURE_RATE_KEY));
                pokemon.setBaseFleeRate(pokemonJSON.getInt(BASE_FLEE_RATE_KEY));
                pokemon.setCandyToEvolve(pokemonJSON.getInt(CANDY_TO_EVOLVE_KEY));
                pokemon.setAvgCpGain(pokemonJSON.getDouble(AVERAGE_CP_GAIN_KEY));

                pokemonList.add(pokemon);
            }
        }
        catch (JSONException ignored) {}

        return pokemonList;
    }
}
