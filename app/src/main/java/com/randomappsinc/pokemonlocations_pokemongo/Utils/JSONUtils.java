package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.EggsDBManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 9/21/16.
 */

public class JSONUtils {
    // Pokemon
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

    // Eggs
    public static final String POKEMON_ID_KEY = "pokemon_id";
    public static final String CHANCE_KEY = "chance";
    public static final String TWO_KEY = "two";
    public static final String FIVE_KEY = "five";
    public static final String TEN_KEY = "ten";

    public static void extractPokemon(String jsonText) {
        try {
            JSONObject pokemonListJson = new JSONObject(jsonText);
            JSONArray pokemonArray = pokemonListJson.getJSONArray(POKEMON_KEY);

            List<Pokemon> pokemonList = new ArrayList<>();
            for (int i = 0; i < pokemonArray.length(); i++) {
                JSONObject pokemonJSON = pokemonArray.getJSONObject(i);

                Pokemon pokemon = new Pokemon();
                pokemon.setId(i + 1);
                pokemon.setName(pokemonJSON.getString(NAME_KEY));
                pokemon.setType1(normalizeString(pokemonJSON.getString(TYPE_1_KEY)));
                pokemon.setType2(normalizeString(pokemonJSON.getString(TYPE_2_KEY)));
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

            DatabaseManager.get().getPokemonDBManager().updatePokemonList(pokemonList);
        } catch (JSONException ignored) {}
    }

    public static void updateEggsDB() {
        if (PreferencesManager.get().getEggsDBVersion() < EggsDBManager.CURRENT_EGGS_DB_VERSION) {
            InputStream inputStream = null;
            try {
                inputStream = MyApplication.getAppContext().getAssets().open("eggs.txt");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                JSONUtils.extractEggData(new String(buffer));
            } catch (IOException ignored) {
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception ignored) {}
                }
            }

            PreferencesManager.get().updateEggsDBVersion();
        }
    }

    public static void extractEggData(String jsonText) {
        try {
            JSONObject eggListJson = new JSONObject(jsonText);
            JSONArray twoKmList = eggListJson.getJSONArray(TWO_KEY);
            for (int i = 0; i < twoKmList.length(); i++) {
                JSONObject eggJson = twoKmList.getJSONObject(i);
                DatabaseManager.get().getEggsDBManager()
                        .addOrUpdateEgg(eggJson.getInt(POKEMON_ID_KEY), 2, eggJson.getDouble(CHANCE_KEY));
            }

            JSONArray fiveKmList = eggListJson.getJSONArray(FIVE_KEY);
            for (int i = 0; i < fiveKmList.length(); i++) {
                JSONObject eggJson = fiveKmList.getJSONObject(i);
                DatabaseManager.get().getEggsDBManager()
                        .addOrUpdateEgg(eggJson.getInt(POKEMON_ID_KEY), 5, eggJson.getDouble(CHANCE_KEY));
            }

            JSONArray tenKmList = eggListJson.getJSONArray(TEN_KEY);
            for (int i = 0; i < tenKmList.length(); i++) {
                JSONObject eggJson = tenKmList.getJSONObject(i);
                DatabaseManager.get().getEggsDBManager()
                        .addOrUpdateEgg(eggJson.getInt(POKEMON_ID_KEY), 10, eggJson.getDouble(CHANCE_KEY));
            }
        } catch (JSONException ignored) {}
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
