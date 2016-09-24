package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokedexPokemonDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static void extractPokemon(String jsonText) {
        try {
            JSONObject pokemonListJson = new JSONObject(jsonText);
            JSONArray pokemonArray = pokemonListJson.getJSONArray(POKEMON_KEY);
            for (int i = 0; i < pokemonArray.length(); i++) {
                JSONObject pokemonJSON = pokemonArray.getJSONObject(i);

                PokedexPokemonDO pokemonDO = new PokedexPokemonDO();
                pokemonDO.setPokemonId(i + 1);
                pokemonDO.setName(pokemonJSON.getString(NAME_KEY));
                pokemonDO.setType1(normalizeString(pokemonJSON.getString(TYPE_1_KEY)));
                pokemonDO.setType2(normalizeString(pokemonJSON.getString(TYPE_2_KEY)));
                pokemonDO.setBaseDefense(pokemonJSON.getInt(BASE_DEFENSE_KEY));
                pokemonDO.setBaseStamina(pokemonJSON.getInt(BASE_STAMINA_KEY));
                pokemonDO.setBaseAttack(pokemonJSON.getInt(BASE_ATTACK_KEY));
                pokemonDO.setMaxCp(pokemonJSON.getInt(MAX_CP_KEY));
                pokemonDO.setBaseCaptureRate(pokemonJSON.getInt(BASE_CAPTURE_RATE_KEY));
                pokemonDO.setBaseFleeRate(pokemonJSON.getInt(BASE_FLEE_RATE_KEY));
                pokemonDO.setCandyToEvolve(pokemonJSON.getInt(CANDY_TO_EVOLVE_KEY));
                pokemonDO.setAvgCpGain(pokemonJSON.getDouble(AVERAGE_CP_GAIN_KEY));

                DatabaseManager.get().getPokemonDBManager().addOrUpdatePokemon(pokemonDO);
            }
        }
        catch (JSONException ignored) {}
    }

    public static String normalizeString(String input) {
        String[] pieces = input.split(" ");
        StringBuilder finalInput = new StringBuilder();

        for (String piece : pieces) {
            if (!piece.isEmpty()) {
                finalInput.append(piece.substring(0, 1).toUpperCase());
                finalInput.append(piece.substring(1));
            }
        }

        return finalInput.toString();
    }
}
