package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;

/**
 * Created by alexanderchiou on 7/14/16.
 */
public class PreferencesManager {
    private static final String NUM_APP_OPENS_KEY = "numAppOpens";
    private static final String FIRST_TIME_KEY = "firstTime";
    private static final String SHOULD_SHOW_LOCATION_RATIONALE_KEY = "shouldShowLocationRationale";
    private static final String CURRENT_LOCATION_KEY = "currentLocation";
    private static PreferencesManager instance;

    private Context context;
    private SharedPreferences prefs;

    public static PreferencesManager get() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private PreferencesManager() {
        context = MyApplication.getAppContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean shouldAskToRate() {
        int numAppOpens = prefs.getInt(NUM_APP_OPENS_KEY, 0) + 1;
        prefs.edit().putInt(NUM_APP_OPENS_KEY, numAppOpens).apply();
        return numAppOpens == 5;
    }

    public boolean shouldShowShareTutorial() {
        boolean shouldShow = prefs.getBoolean(FIRST_TIME_KEY, true);
        prefs.edit().putBoolean(FIRST_TIME_KEY, false).apply();
        return shouldShow;
    }

    public boolean shouldShowLocationRationale() {
        boolean shouldShow = prefs.getBoolean(SHOULD_SHOW_LOCATION_RATIONALE_KEY, true);
        prefs.edit().putBoolean(SHOULD_SHOW_LOCATION_RATIONALE_KEY, false).apply();
        return shouldShow;
    }

    public String getCurrentLocation() {
        return prefs.getString(CURRENT_LOCATION_KEY, context.getString(R.string.automatic));
    }

    public void setCurrentLocation(String newLocation) {
        prefs.edit().putString(CURRENT_LOCATION_KEY, newLocation).apply();
    }

    public void resetCurrentLocation() {
        setCurrentLocation(context.getString(R.string.automatic));
    }
}