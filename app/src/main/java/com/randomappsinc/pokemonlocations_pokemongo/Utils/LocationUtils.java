package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.content.Context;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/31/16.
 */
public class LocationUtils {
    public static String[] getLocationOptions(String location) {
        Context context = MyApplication.getAppContext();
        List<String> options = new ArrayList<>();
        if (!location.equals(PreferencesManager.get().getCurrentLocation())) {
            options.add(context.getString(R.string.set_as_current));
        }
        options.add(context.getString(R.string.edit_location));
        options.add(context.getString(R.string.delete_location));
        return options.toArray(new String[options.size()]);
    }
}
