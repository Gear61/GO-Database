package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;

/**
 * Created by alexanderchiou on 7/14/16.
 */
public class PreferencesManager {
    private static final String NUM_APP_OPENS_KEY = "numAppOpens";
    private static final String LOCATION_FIRST_TIME_KEY = "locationFirstTime";
    private static final String SHOULD_SHOW_LOCATION_RATIONALE_KEY = "shouldShowLocationRationale";
    private static PreferencesManager instance;
    private SharedPreferences prefs;

    public static PreferencesManager get() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private PreferencesManager() {
        prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
    }

    public boolean shouldAskToRate() {
        int numAppOpens = prefs.getInt(NUM_APP_OPENS_KEY, 0) + 1;
        prefs.edit().putInt(NUM_APP_OPENS_KEY, numAppOpens).apply();
        return numAppOpens == 5;
    }

    public boolean shouldExplainLocation() {
        boolean firstTime = prefs.getBoolean(LOCATION_FIRST_TIME_KEY, true);
        prefs.edit().putBoolean(LOCATION_FIRST_TIME_KEY, false).apply();
        return firstTime;
    }

    public boolean shouldShowLocationRationale() {
        boolean shouldShow = prefs.getBoolean(SHOULD_SHOW_LOCATION_RATIONALE_KEY, true);
        prefs.edit().putBoolean(SHOULD_SHOW_LOCATION_RATIONALE_KEY, false).apply();
        return shouldShow;
    }
}