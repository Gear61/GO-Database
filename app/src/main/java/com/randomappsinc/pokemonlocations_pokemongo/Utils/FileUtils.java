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
public class FileUtils {
    public static List<String> extractItems(String filepath) {
        List<String> items = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(MyApplication.getAppContext()
                    .getAssets().open(filepath)));
            String pokemon;
            while ((pokemon = reader.readLine()) != null) {
                items.add(pokemon);
            }
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {}
            }
        }
        Collections.sort(items);
        return items;
    }
}
