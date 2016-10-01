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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.Sort;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class DatabaseManager {
    private static final int CURRENT_REALM_VERSION = 6;
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

    private PokemonDBManager pokemonDBManager;
    private EggsDBManager eggsDBManager;

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    private DatabaseManager() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(MyApplication.getAppContext())
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        pokemonDBManager = new PokemonDBManager();
        eggsDBManager = new EggsDBManager();
    }

    private RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

            // Add saved locations
            if (oldVersion == 0) {
                schema.create("SavedLocationDO")
                        .addField("displayName", String.class)
                        .addField("latitude", double.class)
                        .addField("longitude", double.class);
                oldVersion++;
            }

            if (oldVersion == 1) {
                schema.get("PokeLocationDO")
                        .removeField("score")
                        .addField("numLikes", int.class)
                        .addField("numDislikes", int.class);
                oldVersion++;
            }

            if (oldVersion == 2) {
                schema.get("PokeFindingDO")
                        .addField("reportTime", long.class);
                oldVersion++;
            }

            if (oldVersion == 3) {
                schema.create("PokedexPokemonDO")
                        .addField("pokemonId", int.class)
                        .addPrimaryKey("pokemonId")
                        .addField("name", String.class)
                        .addField("type1", String.class)
                        .addField("type2", String.class)
                        .addField("maxCp", int.class)
                        .addField("baseAttack", int.class)
                        .addField("baseDefense", int.class)
                        .addField("baseStamina", int.class)
                        .addField("baseCaptureRate", int.class)
                        .addField("baseFleeRate", int.class)
                        .addField("candyToEvolve", int.class)
                        .addField("avgCpGain", double.class);
                oldVersion++;
            }

            if (oldVersion == 4) {
                schema.create("EggDO")
                        .addField("pokemonId", int.class)
                        .addPrimaryKey("pokemonId")
                        .addField("distance", int.class)
                        .addField("chance", double.class);
                oldVersion++;
            }

            if (oldVersion == 5) {
                schema.get("PokedexPokemonDO")
                        .addField("maxCpRanking", int.class)
                        .addField("attackRanking", int.class)
                        .addField("defenseRanking", int.class)
                        .addField("staminaRanking", int.class)
                        .addField("captureRateRanking", int.class)
                        .addField("fleeRateRanking", int.class);
            }
        }
    };

    public PokemonDBManager getPokemonDBManager() {
        return pokemonDBManager;
    }

    public EggsDBManager getEggsDBManager() {
        return eggsDBManager;
    }

    // Upvote/downvote
    public void processLike(PokeLocation place) {
        int oldScore = getVote(place);
        int newScore = 0;
        VoteDO voteDO = new VoteDO();
        voteDO.setPlaceId(place.getPlaceId());

        switch (oldScore) {
            // Liked to neutral
            case 1:
                newScore = 0;
                voteDO.setVote(0);
                place.setNumLikes(place.getNumLikes() - 1);
                break;
            // Neutral to liked
            case 0:
                newScore = 1;
                voteDO.setVote(1);
                place.setNumLikes(place.getNumLikes() + 1);
                break;
            // Disliked to liked
            case -1:
                newScore = 1;
                voteDO.setVote(1);
                place.setNumLikes(place.getNumLikes() + 1);
                place.setNumDislikes(place.getNumDislikes() - 1);
                break;
        }

        updateVote(voteDO);

        if (isLocationFavorited(place)) {
            addOrUpdateLocation(place);
        }

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setPlaceId(place.getPlaceId());
        voteRequest.setOldAmount(oldScore);
        voteRequest.setNewAmount(newScore);
        RestClient.get().getPokemonService()
                .voteLocation(voteRequest)
                .enqueue(new VoteCallback(oldScore, place));
    }

    public void processDislike(PokeLocation place) {
        int oldScore = getVote(place);
        int newScore = 0;
        VoteDO voteDO = new VoteDO();
        voteDO.setPlaceId(place.getPlaceId());

        switch (oldScore) {
            // Liked to disliked
            case 1:
                newScore = -1;
                voteDO.setVote(-1);
                place.setNumLikes(place.getNumLikes() - 1);
                place.setNumDislikes(place.getNumDislikes() + 1);
                break;
            // Neutral to disliked
            case 0:
                newScore = -1;
                voteDO.setVote(-1);
                place.setNumDislikes(place.getNumDislikes() + 1);
                break;
            // Disliked to neutral
            case -1:
                newScore = 0;
                voteDO.setVote(0);
                place.setNumDislikes(place.getNumDislikes() - 1);
                break;
        }

        updateVote(voteDO);

        if (isLocationFavorited(place)) {
            addOrUpdateLocation(place);
        }

        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setPlaceId(place.getPlaceId());
        voteRequest.setOldAmount(oldScore);
        voteRequest.setNewAmount(newScore);
        RestClient.get().getPokemonService()
                .voteLocation(voteRequest)
                .enqueue(new VoteCallback(oldScore, place));
    }

    public void updateVote(final VoteDO vote) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(vote);
            }
        });
    }

    public int getVote(PokeLocation place) {
        if (place == null) {
            return 0;
        }

        VoteDO voteDO = getRealm().where(VoteDO.class)
                .equalTo("placeId", place.getPlaceId())
                .findFirst();
        if (voteDO == null) {
            return 0;
        } else {
            return voteDO.getVote();
        }
    }

    // Favorites
    public List<PokeLocation> getFavorites() {
        List<PokeLocation> favorites = new ArrayList<>();
        List<PokeLocationDO> pokeLocationDOs = getRealm().where(PokeLocationDO.class).findAll();
        for (PokeLocationDO pokeLocationDO : pokeLocationDOs) {
            favorites.add(DBConverters.getLocationFromDO(pokeLocationDO));
        }
        return favorites;
    }

    public boolean isLocationFavorited(PokeLocation pokeLocation) {
        return pokeLocation != null && getRealm().where(PokeLocationDO.class)
                .equalTo("placeId", pokeLocation.getPlaceId())
                .findFirst() != null;
    }

    public void unfavoriteLocation(final PokeLocation pokeLocation) {
        getRealm().executeTransaction(new Realm.Transaction() {
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
        // If we're favoriting the location and not merely updating it, auto-like it
        if (!isLocationFavorited(pokeLocation) && getVote(pokeLocation) == 0) {
            processLike(pokeLocation);
        }

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(pokeLocation.toPokeLocationDO());
            }
        });
    }

    public List<String> getFavoriteIds() {
        List<String> placeIds = new ArrayList<>();
        List<PokeLocationDO> favorites = getRealm().where(PokeLocationDO.class).findAll();
        for (PokeLocationDO locationDO : favorites) {
            placeIds.add(locationDO.getPlaceId());
        }
        return placeIds;
    }

    public PokeLocation getFavorite(String placeId) {
        PokeLocationDO pokeLocationDO = getRealm().where(PokeLocationDO.class)
                .equalTo("placeId", placeId)
                .findFirst();
        return pokeLocationDO == null ? null : DBConverters.getLocationFromDO(pokeLocationDO);
    }

    // Pokemon Findings
    public List<PokeFindingDO> getFindings () {
        return getRealm().where(PokeFindingDO.class).findAll()
                .sort("reportTime", Sort.DESCENDING);
    }

    public void addPokeFinding(final PokeFindingDO pokeFindingDO) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(pokeFindingDO);
            }
        });
    }

    public void updatePokeFinding(final PokeFindingDO pokeFindingDO, final String newFrequency) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PokeFindingDO finding = getFinding(pokeFindingDO.getPokemonId(), pokeFindingDO.getPlaceId());
                finding.setReportTime(System.currentTimeMillis() / 1000L);
                if (newFrequency != null) {
                    finding.setFrequency(newFrequency);
                }
            }
        });
    }

    public PokeFindingDO getFinding(int pokemonId, String placeId) {
        return getRealm().where(PokeFindingDO.class)
                .equalTo("pokemonId", pokemonId)
                .equalTo("placeId", placeId)
                .findFirst();
    }

    // My Locations
    public List<String> getMyLocations() {
        List<String> locationNames = new ArrayList<>();

        RealmResults<SavedLocationDO> locationDOs = getRealm().where(SavedLocationDO.class).findAll();
        for (SavedLocationDO locationDO : locationDOs) {
            locationNames.add(locationDO.getDisplayName());
        }

        Collections.sort(locationNames);

        return locationNames;
    }

    public void addMyLocation(final SavedLocationDO locationDO) {
        if (!alreadyHasLocation(locationDO.getDisplayName())) {
            getRealm().executeTransaction(new Realm.Transaction() {
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

        getRealm().executeTransaction(new Realm.Transaction() {
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

        getRealm().executeTransaction(new Realm.Transaction() {
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
        return getRealm().where(SavedLocationDO.class)
                .equalTo("displayName", displayName)
                .findFirst() != null;
    }

    public SavedLocationDO getLocation(String displayName) {
        return getRealm().where(SavedLocationDO.class)
                .equalTo("displayName", displayName)
                .findFirst();
    }
}
