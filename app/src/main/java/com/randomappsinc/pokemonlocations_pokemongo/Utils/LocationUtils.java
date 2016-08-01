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

    public static double getDistance(double latitude1, double longitude1,
                                     double latitude2, double longitude2) {
        double theta = longitude1 - longitude2;
        double dist = Math.sin(degreeToRadians(latitude1)) * Math.sin(degreeToRadians(latitude2))
                + Math.cos(degreeToRadians(latitude1)) * Math.cos(degreeToRadians(latitude2))
                * Math.cos(degreeToRadians(theta));
        dist = Math.acos(dist);
        dist = radianToDegree(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double degreeToRadians(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double radianToDegree(double rad) {
        return (rad * 180 / Math.PI);
    }
}
