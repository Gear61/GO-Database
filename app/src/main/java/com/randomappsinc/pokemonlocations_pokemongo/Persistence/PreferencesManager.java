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
    private static final String FIRST_TIME_SEARCH = "firstTimeSearch";
    private static final String FIRST_TIME_DISTANCE = "firstTimeDistance";
    private static final String FIRST_TIME_LOCATION = "firstTimeLocation";
    private static final String SHOULD_EXPLAIN_NO_RESULTS = "shouldExplainNoResults";
    private static final String SHOULD_SHOW_LOCATION_RATIONALE_KEY = "shouldShowLocationRationale";
    public static final String CURRENT_LOCATION_KEY = "currentLocation";
    private static final String IMAGES_ENABLED_KEY = "imagesEnabled";
    private static final String IS_AMERICAN_KEY = "isAmerican";
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

    public boolean shouldShowWelcome() {
        boolean isFirstTime = prefs.getBoolean(FIRST_TIME_SEARCH, true);
        prefs.edit().putBoolean(FIRST_TIME_SEARCH, false).apply();
        return isFirstTime;
    }

    public boolean shouldShowLocationRationale() {
        boolean shouldShow = prefs.getBoolean(SHOULD_SHOW_LOCATION_RATIONALE_KEY, true);
        prefs.edit().putBoolean(SHOULD_SHOW_LOCATION_RATIONALE_KEY, false).apply();
        return shouldShow;
    }

    public boolean shouldSetDistanceUnit() {
        boolean shouldSet = prefs.getBoolean(FIRST_TIME_DISTANCE, true);
        prefs.edit().putBoolean(FIRST_TIME_DISTANCE, false).apply();
        return shouldSet;
    }

    public boolean shouldShowLocationTut() {
        return prefs.getBoolean(FIRST_TIME_LOCATION, true);
    }

    public void turnOffLocationTut() {
        prefs.edit().putBoolean(FIRST_TIME_LOCATION, false).apply();
    }

    public boolean shouldExplainNoResults() {
        boolean shouldExplain = prefs.getBoolean(SHOULD_EXPLAIN_NO_RESULTS, true);
        prefs.edit().putBoolean(SHOULD_EXPLAIN_NO_RESULTS, false).apply();
        return shouldExplain;
    }

    public void setIsAmerican(boolean isAmerican) {
        prefs.edit().putBoolean(IS_AMERICAN_KEY, isAmerican).apply();
    }

    public boolean getIsAmerican() {
        return prefs.getBoolean(IS_AMERICAN_KEY, false);
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

    public boolean areImagesEnabled() {
        return prefs.getBoolean(IMAGES_ENABLED_KEY, false);
    }

    public void enableImages() {
        prefs.edit().putBoolean(IMAGES_ENABLED_KEY, true).apply();
    }
}