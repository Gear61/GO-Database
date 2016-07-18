package com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class VoteDO extends RealmObject {
    @PrimaryKey
    private String placeId;

    private int vote;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
