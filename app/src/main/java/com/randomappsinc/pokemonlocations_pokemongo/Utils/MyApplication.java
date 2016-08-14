package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.StatusCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.StatusRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;

import java.util.Locale;

/**
 * Created by alexanderchiou on 7/14/16.
 */
public final class MyApplication extends Application {
    private static Context context;

    private Handler poller;
    private Runnable statusCheckTask;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new IoniconsModule())
                .with(new FontAwesomeModule());
        context = getApplicationContext();

        poller = new Handler();
        statusCheckTask = new Runnable() {
            @Override
            public void run() {
                if (!PreferencesManager.get().areImagesEnabled()) {
                    StatusRequest request = new StatusRequest();
                    request.setCurrentVersion(getVersionCode());
                    RestClient.get().getPokemonService()
                            .getStatus(request)
                            .enqueue(new StatusCallback());
                    poller.postDelayed(statusCheckTask, 10000L);
                }
            }
        };

        poller.post(statusCheckTask);

        if (PreferencesManager.get().shouldSetDistanceUnit()) {
            Locale currentLocale = getResources().getConfiguration().locale;
            if (currentLocale.equals(Locale.US)) {
                PreferencesManager.get().setIsAmerican(true);
            }
        }
    }

    public static Context getAppContext() {
        return context;
    }

    public static int getVersionCode() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception ignored) {
            return 0;
        }
    }
}
