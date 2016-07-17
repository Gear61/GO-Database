package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import android.content.Context;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class DatabaseManager {
    private static DatabaseManager instance;

    public static DatabaseManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized DatabaseManager getSync() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private Realm realm;

    private DatabaseManager() {
        Context context = MyApplication.getAppContext();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfig);
    }

    /* public void addFavorite(final Restaurant restaurant) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(restaurant.toRestaurantDO());
            }
        });
    } */

    public List<PokeLocation> getFavorites () {
        List<PokeLocation> favorites = new ArrayList<>();

        PokeLocation derp = new PokeLocation();
        derp.setScore(5);
        derp.setDisplayName("Lake Elizabeth");

        PokeLocation derp2 = new PokeLocation();
        derp2.setScore(-9999);
        derp2.setDisplayName("Lake Elizabeth 2");

        favorites.add(derp);
        favorites.add(derp2);
        return favorites;
    }
}
