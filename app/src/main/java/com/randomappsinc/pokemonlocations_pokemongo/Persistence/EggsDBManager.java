package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.EggDO;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by alexanderchiou on 9/25/16.
 */

public class EggsDBManager {
    public static final int CURRENT_EGGS_DB_VERSION = 1;
    private Realm realm;

    public EggsDBManager(Realm realm) {
        this.realm = realm;
    }

    public void addOrUpdateEgg(final int pokemonId, final int distance, final double chance) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                EggDO eggDO = new EggDO();
                eggDO.setPokemonId(pokemonId);
                eggDO.setDistance(distance);
                eggDO.setChance(chance);
                realm.copyToRealmOrUpdate(eggDO);
            }
        });
    }

    public List<EggDO> getEggs(int distance) {
        return realm.where(EggDO.class)
                .equalTo("distance", distance)
                .findAllSorted("chance", Sort.DESCENDING);
    }
}
