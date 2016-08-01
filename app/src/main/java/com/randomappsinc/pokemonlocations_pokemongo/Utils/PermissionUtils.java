package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.app.Fragment;
import android.content.pm.PackageManager;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class PermissionUtils {
    public static void requestPermission(Fragment fragment, String permission, int requestCode) {
        FragmentCompat.requestPermissions(fragment, new String[]{permission}, requestCode);
    }

    public static boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(MyApplication.getAppContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
