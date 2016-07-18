package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.VoteDO;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class DatabaseManager {
    private static final int CURRENT_REALM_VERSION = 0;
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
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(MyApplication.getAppContext())
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
    }

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // No migrations yet, so empty for now
        }
    };

    public void processUpvote(PokeLocation place) {
        int currentScore = getVote(place);
        final VoteDO voteDO = new VoteDO();
        voteDO.setPlaceId(place.getPlaceId());

        switch (currentScore) {
            case 1:
                place.setScore(place.getScore() - 1);
                voteDO.setVote(0);
                break;
            case 0:
                place.setScore(place.getScore() + 1);
                voteDO.setVote(1);
                break;
            case -1:
                place.setScore(place.getScore() + 2);
                voteDO.setVote(1);
                break;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(voteDO);
            }
        });
    }

    public void processDownvote(PokeLocation place) {
        int currentScore = getVote(place);
        final VoteDO voteDO = new VoteDO();
        voteDO.setPlaceId(place.getPlaceId());

        switch (currentScore) {
            case 1:
                place.setScore(place.getScore() - 2);
                voteDO.setVote(-1);
                break;
            case 0:
                place.setScore(place.getScore() - 1);
                voteDO.setVote(-1);
                break;
            case -1:
                place.setScore(place.getScore() + 1);
                voteDO.setVote(0);
                break;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(voteDO);
            }
        });
    }

    public int getVote(PokeLocation place) {
        VoteDO voteDO = realm.where(VoteDO.class)
                .equalTo("placeId", place.getPlaceId())
                .findFirst();
        if (voteDO == null) {
            return 0;
        } else {
            return voteDO.getVote();
        }
    }

    public List<PokeLocation> getFavorites () {
        List<PokeLocation> favorites = new ArrayList<>();

        PokeLocation derp = new PokeLocation();
        derp.setPlaceId("984fnkjsn38");
        derp.setScore(5);
        derp.setDisplayName("Lake Elizabeth");

        ArrayList<Integer> pokemon = new ArrayList<>();
        pokemon.add(16);
        pokemon.add(89);
        pokemon.add(43);
        pokemon.add(90);
        pokemon.add(27);
        pokemon.add(35);
        pokemon.add(42);
        derp.setCommonPokemon(pokemon);
        derp.setUncommonPokemon(pokemon);
        derp.setRarePokemon(pokemon);

        PokeLocation derp2 = new PokeLocation();
        derp2.setScore(-9999);
        derp2.setDisplayName("Lake Elizabeth 2");

        favorites.add(derp);
        favorites.add(derp2);
        return favorites;
    }
}
