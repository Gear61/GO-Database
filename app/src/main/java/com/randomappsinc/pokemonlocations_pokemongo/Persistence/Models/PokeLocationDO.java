package com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocationDO extends RealmObject {
    @PrimaryKey
    private String placeId;

    private String displayName;
    private float latitude;
    private float longitude;
    private int score;

    private RealmList<PokemonDO> commonPokemon;
    private RealmList<PokemonDO> uncommonPokemon;
    private RealmList<PokemonDO> rarePokemon;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public RealmList<PokemonDO> getCommonPokemon() {
        return commonPokemon;
    }

    public void setCommonPokemon(RealmList<PokemonDO> commonPokemon) {
        this.commonPokemon = commonPokemon;
    }

    public RealmList<PokemonDO> getUncommonPokemon() {
        return uncommonPokemon;
    }

    public void setUncommonPokemon(RealmList<PokemonDO> uncommonPokemon) {
        this.uncommonPokemon = uncommonPokemon;
    }

    public RealmList<PokemonDO> getRarePokemon() {
        return rarePokemon;
    }

    public void setRarePokemon(RealmList<PokemonDO> rarePokemon) {
        this.rarePokemon = rarePokemon;
    }
}
