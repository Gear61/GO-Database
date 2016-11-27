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
import java.util.Collections;
import java.util.Comparator;
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

                pokemonList.add(pokemon);
            }

            Collections.sort(pokemonList, CP_SORTER);
            pokemonList.get(0).setMaxCpRanking(1);
            for (int i = 1; i < pokemonList.size(); i++) {
                int previousStat = pokemonList.get(i - 1).getMaxCp();
                int currentStat = pokemonList.get(i).getMaxCp();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(i - 1).getMaxCpRanking();
                    pokemonList.get(i).setMaxCpRanking(previousRanking);
                } else {
                    pokemonList.get(i).setMaxCpRanking(i + 1);
                }
            }

            Collections.sort(pokemonList, ATTACK_SORTER);
            pokemonList.get(0).setAttackRanking(1);
            for (int i = 1; i < pokemonList.size(); i++) {
                int previousStat = pokemonList.get(i - 1).getBaseAttack();
                int currentStat = pokemonList.get(i).getBaseAttack();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(i - 1).getAttackRanking();
                    pokemonList.get(i).setAttackRanking(previousRanking);
                } else {
                    pokemonList.get(i).setAttackRanking(i + 1);
                }
            }

            Collections.sort(pokemonList, DEFENSE_SORTER);
            pokemonList.get(0).setDefenseRanking(1);
            for (int i = 1; i < pokemonList.size(); i++) {
                int previousStat = pokemonList.get(i - 1).getBaseDefense();
                int currentStat = pokemonList.get(i).getBaseDefense();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(i - 1).getDefenseRanking();
                    pokemonList.get(i).setDefenseRanking(previousRanking);
                } else {
                    pokemonList.get(i).setDefenseRanking(i + 1);
                }
            }

            Collections.sort(pokemonList, STAMINA_SORTER);
            pokemonList.get(0).setStaminaRanking(1);
            for (int i = 1; i < pokemonList.size(); i++) {
                int previousStat = pokemonList.get(i - 1).getBaseStamina();
                int currentStat = pokemonList.get(i).getBaseStamina();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(i - 1).getStaminaRanking();
                    pokemonList.get(i).setStaminaRanking(previousRanking);
                } else {
                    pokemonList.get(i).setStaminaRanking(i + 1);
                }
            }

            Collections.sort(pokemonList, CATCH_RATE_SORTER);
            pokemonList.get(0).setCaptureRateRanking(1);
            for (int i = 1; i < pokemonList.size(); i++) {
                int previousStat = pokemonList.get(i - 1).getBaseCaptureRate();
                int currentStat = pokemonList.get(i).getBaseCaptureRate();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(i - 1).getCaptureRateRanking();
                    pokemonList.get(i).setCaptureRateRanking(previousRanking);
                } else {
                    pokemonList.get(i).setCaptureRateRanking(i + 1);
                }
            }

            Collections.sort(pokemonList, FLEE_RATE_SORTER);
            pokemonList.get(0).setFleeRateRanking(1);
            for (int i = 1; i < pokemonList.size(); i++) {
                int previousStat = pokemonList.get(i - 1).getBaseFleeRate();
                int currentStat = pokemonList.get(i).getBaseFleeRate();
                if (previousStat == currentStat) {
                    int previousRanking = pokemonList.get(i - 1).getFleeRateRanking();
                    pokemonList.get(i).setFleeRateRanking(previousRanking);
                } else {
                    pokemonList.get(i).setFleeRateRanking(i + 1);
                }
            }

            DatabaseManager.get().getPokemonDBManager().updatePokemonList(pokemonList);
        } catch (JSONException ignored) {}
    }

    private static Comparator<Pokemon> CP_SORTER = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon first, Pokemon second) {
            return second.getMaxCp() - first.getMaxCp();
        }
    };

    private static Comparator<Pokemon> ATTACK_SORTER = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon first, Pokemon second) {
            return second.getBaseAttack() - first.getBaseAttack();
        }
    };

    private static Comparator<Pokemon> DEFENSE_SORTER = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon first, Pokemon second) {
            return second.getBaseDefense() - first.getBaseDefense();
        }
    };

    private static Comparator<Pokemon> STAMINA_SORTER = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon first, Pokemon second) {
            return second.getBaseStamina() - first.getBaseStamina();
        }
    };

    private static Comparator<Pokemon> CATCH_RATE_SORTER = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon first, Pokemon second) {
            return second.getBaseCaptureRate() - first.getBaseCaptureRate();
        }
    };

    private static Comparator<Pokemon> FLEE_RATE_SORTER = new Comparator<Pokemon>() {
        @Override
        public int compare(Pokemon first, Pokemon second) {
            return second.getBaseFleeRate() - first.getBaseFleeRate();
        }
    };

    public static void updateEggsDB() {
        if (PreferencesManager.get().getEggsDBVersion() < EggsDBManager.CURRENT_EGGS_DB_VERSION) {
            DatabaseManager.get().getEggsDBManager().clearEggs();

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
