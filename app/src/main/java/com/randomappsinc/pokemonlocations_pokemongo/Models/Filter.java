package com.randomappsinc.pokemonlocations_pokemongo.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexanderchiou on 8/4/16.
 */
public class Filter implements Parcelable {
    public static final String KEY = "filter";

    private int pokemonId;
    private int distanceIndex;

    public Filter() {
        distanceIndex = 3;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public int getDistanceIndex() {
        return distanceIndex;
    }

    public void setDistanceIndex(int distanceIndex) {
        this.distanceIndex = distanceIndex;
    }

    protected Filter(Parcel in) {
        pokemonId = in.readInt();
        distanceIndex = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pokemonId);
        dest.writeInt(distanceIndex);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
