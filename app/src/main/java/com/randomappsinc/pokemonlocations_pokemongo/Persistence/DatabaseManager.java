package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.VoteCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.VoteRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.VoteDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class DatabaseManager {
    private static final int CURRENT_REALM_VERSION = 1;
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
            RealmSchema schema = realm.getSchema();

            // Add saved locations
            if (oldVersion == 0) {
                schema.create("SavedLocationDO")
                        .addField("displayName", String.class)
                        .addField("latitude", double.class)
                        .addField("longitude", double.class);
            }
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

    public List<String> getFavoriteIds() {
        List<String> placeIds = new ArrayList<>();
        List<PokeLocationDO> favorites = realm.where(PokeLocationDO.class).findAll();
        for (PokeLocationDO locationDO : favorites) {
            placeIds.add(locationDO.getPlaceId());
        }
        return placeIds;
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

    // My Locations
    public List<String> getMyLocations() {
        List<String> locationNames = new ArrayList<>();

        RealmResults<SavedLocationDO> locationDOs = realm.where(SavedLocationDO.class).findAll();
        for (SavedLocationDO locationDO : locationDOs) {
            locationNames.add(locationDO.getDisplayName());
        }

        Collections.sort(locationNames);

        return locationNames;
    }

    public void addMyLocation(final SavedLocationDO locationDO) {
        if (!alreadyHasLocation(locationDO.getDisplayName())) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(locationDO);
                }
            });
        }
    }

    public void deleteMyLocation(final String displayName) {
        if (PreferencesManager.get().getCurrentLocation().equals(displayName)) {
            PreferencesManager.get().resetCurrentLocation();
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(SavedLocationDO.class)
                        .equalTo("displayName", displayName)
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }

    public void updateMyLocation(final String oldName, final SavedLocationDO locationDO) {
        if (PreferencesManager.get().getCurrentLocation().equals(oldName)) {
            PreferencesManager.get().setCurrentLocation(locationDO.getDisplayName());
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SavedLocationDO savedLocationDO = realm.where(SavedLocationDO.class)
                        .equalTo("displayName", oldName)
                        .findFirst();
                savedLocationDO.setDisplayName(locationDO.getDisplayName());
                savedLocationDO.setLatitude(locationDO.getLatitude());
                savedLocationDO.setLongitude(locationDO.getLongitude());
            }
        });
    }

    public String[] getLocationsArray() {
        List<String> locationsList = new ArrayList<>();
        locationsList.add(MyApplication.getAppContext().getString(R.string.automatic));
        locationsList.addAll(getMyLocations());
        String[] locationsArray = new String[locationsList.size()];
        return locationsList.toArray(locationsArray);
    }

    public int getCurrentLocationIndex() {
        String[] locationOptions = getLocationsArray();

        String currentLocation = PreferencesManager.get().getCurrentLocation();
        for (int i = 0; i < locationOptions.length; i++) {
            if (locationOptions[i].equals(currentLocation)) {
                return i;
            }
        }
        return 0;
    }

    public boolean alreadyHasLocation(String displayName) {
        return realm.where(SavedLocationDO.class)
                .equalTo("displayName", displayName)
                .findFirst() != null;
    }

    public SavedLocationDO getLocation(String displayName) {
        return realm.where(SavedLocationDO.class)
                .equalTo("displayName", displayName)
                .findFirst();
    }
}
