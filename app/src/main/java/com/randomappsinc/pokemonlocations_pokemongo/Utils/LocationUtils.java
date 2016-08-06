package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        if (!PreferencesManager.get().getIsAmerican()) {
            dist = dist * 1.609344;
        }
        return (dist);
    }

    private static double degreeToRadians(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double radianToDegree(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static double getRangeFromIndex(int index) {
        if (PreferencesManager.get().getIsAmerican()) {
            switch (index) {
                case 0:
                    return 0.0145;
                case 1:
                    return 0.0725;
                case 2:
                    return 0.145;
                case 3:
                    return 0.362;
                case 4:
                    return 0.725;
                default:
                    return 0.25;
            }
        } else {
            switch (index) {
                case 0:
                    return 0.018;
                case 1:
                    return 0.09;
                case 2:
                    return 0.225;
                case 3:
                    return 0.45;
                case 4:
                    return 0.9;
                default:
                    return 0.45;
            }
        }
    }

    public static String getAddressFromLocation(Location location) {
        try {
            Geocoder geocoder = new Geocoder(MyApplication.getAppContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder addressText = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    if (i != 0) {
                        addressText.append(" ");
                    }
                    addressText.append(address.getAddressLine(i));
                }
                return addressText.toString();
            }
        } catch (Exception ignored) {}
        return "";
    }

    public static int getCheckboxId(int containerId) {
        switch (containerId) {
            case R.id.nearby:
                return R.id.nearby_toggle;
            case R.id.very_close:
                return R.id.very_close_toggle;
            case R.id.close:
                return R.id.close_toggle;
            case R.id.far:
                return R.id.far_toggle;
            case R.id.very_far:
                return R.id.very_far_toggle;
            default:
                return R.id.far_toggle;
        }
    }
}
