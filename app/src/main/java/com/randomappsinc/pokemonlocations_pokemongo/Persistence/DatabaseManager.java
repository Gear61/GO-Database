package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.VoteCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.VoteRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.VoteDO;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;

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

    // Upvote/downvote
    public void processUpvote(PokeLocation place) {
        int oldScore = getVote(place);
        int movement = 0;
        final VoteDO voteDO = new VoteDO();
        voteDO.setPlaceId(place.getPlaceId());

        switch (oldScore) {
            // Upvote to neutral
            case 1:
                movement = -1;
                voteDO.setVote(0);
                break;
            // Neutral to upvote
            case 0:
                movement = 1;
                place.setScore(place.getScore() + 1);
                voteDO.setVote(1);
                break;
            // Downvote to upvote
            case -1:
                movement = 2;
                voteDO.setVote(1);
                break;
        }

        place.setScore(place.getScore() + movement);
        updateVote(voteDO);

        if (isLocationFavorited(place)) {
            addOrUpdateLocation(place);
        }

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setPlaceId(place.getPlaceId());
        voteRequest.setAmount(movement);
        RestClient.get().getPokemonService()
                .voteLocation(voteRequest)
                .enqueue(new VoteCallback(oldScore, place));
    }

    public void processDownvote(PokeLocation place) {
        int oldScore = getVote(place);
        int movement = 0;
        final VoteDO voteDO = new VoteDO();
        voteDO.setPlaceId(place.getPlaceId());

        switch (oldScore) {
            // Upvote to downvote
            case 1:
                movement = -2;
                voteDO.setVote(-1);
                break;
            // Neutral to downvote
            case 0:
                movement = -1;
                voteDO.setVote(-1);
                break;
            // Downvote to neutral
            case -1:
                movement = 1;
                voteDO.setVote(0);
                break;
        }

        place.setScore(place.getScore() + movement);
        updateVote(voteDO);

        if (isLocationFavorited(place)) {
            addOrUpdateLocation(place);
        }

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setPlaceId(place.getPlaceId());
        voteRequest.setAmount(movement);
        RestClient.get().getPokemonService()
                .voteLocation(voteRequest)
                .enqueue(new VoteCallback(oldScore, place));
    }

    public void updateVote(final VoteDO vote) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(vote);
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

    // Favorites
    public List<PokeLocation> getFavorites () {
        List<PokeLocation> favorites = new ArrayList<>();
        List<PokeLocationDO> pokeLocationDOs = realm.where(PokeLocationDO.class).findAll();
        for (PokeLocationDO pokeLocationDO : pokeLocationDOs) {
            favorites.add(PokemonUtils.getLocationFromDO(pokeLocationDO));
        }
        return favorites;
    }

    public boolean isLocationFavorited(PokeLocation pokeLocation) {
        return realm.where(PokeLocationDO.class)
                .equalTo("placeId", pokeLocation.getPlaceId())
                .findFirst() != null;
    }

    public void unfavoriteLocation(final PokeLocation pokeLocation) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(PokeLocationDO.class)
                        .equalTo("placeId", pokeLocation.getPlaceId())
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }

    public void addOrUpdateLocation(final PokeLocation pokeLocation) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(pokeLocation.toPokeLocationDO());
            }
        });
    }

    // Pokemon Findings
    public List<PokeFindingDO> getFindings () {
        return realm.where(PokeFindingDO.class).findAll();
    }

    public void addPokeFinding(final PokeFindingDO pokeFindingDO) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(pokeFindingDO);
            }
        });
    }

    public PokeFindingDO getFinding(int pokemonId, PokeLocation place) {
        return realm.where(PokeFindingDO.class)
                .equalTo("pokemonId", pokemonId)
                .equalTo("placeId", place.getPlaceId())
                .findFirst();
    }
}
