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
    private String address;
    private double latitude;
    private double longitude;
    private int numLikes;
    private int numDislikes;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumDislikes() {
        return numDislikes;
    }

    public void setNumDislikes(int numDislikes) {
        this.numDislikes = numDislikes;
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
