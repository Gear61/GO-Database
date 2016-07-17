package com.randomappsinc.pokemonlocations_pokemongo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocation implements Parcelable {
    public static final String KEY = "pokeLocation";

    private int locationId;
    private String displayName;
    private int score;
    private float latitude;
    private float longitude;
    private List<Integer> commonPokemon;
    private List<Integer> uncommonPokemon;
    private List<Integer> rarePokemon;

    public PokeLocation() {}

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public List<Integer> getCommonPokemon() {
        return commonPokemon;
    }

    public void setCommonPokemon(List<Integer> commonPokemon) {
        this.commonPokemon = commonPokemon;
    }

    public List<Integer> getUncommonPokemon() {
        return uncommonPokemon;
    }

    public void setUncommonPokemon(List<Integer> uncommonPokemon) {
        this.uncommonPokemon = uncommonPokemon;
    }

    public List<Integer> getRarePokemon() {
        return rarePokemon;
    }

    public void setRarePokemon(List<Integer> rarePokemon) {
        this.rarePokemon = rarePokemon;
    }

    protected PokeLocation(Parcel in) {
        locationId = in.readInt();
        displayName = in.readString();
        score = in.readInt();
        latitude = in.readFloat();
        longitude = in.readFloat();
        if (in.readByte() == 0x01) {
            commonPokemon = new ArrayList<>();
            in.readList(commonPokemon, Integer.class.getClassLoader());
        } else {
            commonPokemon = null;
        }
        if (in.readByte() == 0x01) {
            uncommonPokemon = new ArrayList<>();
            in.readList(uncommonPokemon, Integer.class.getClassLoader());
        } else {
            uncommonPokemon = null;
        }
        if (in.readByte() == 0x01) {
            rarePokemon = new ArrayList<>();
            in.readList(rarePokemon, Integer.class.getClassLoader());
        } else {
            rarePokemon = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(locationId);
        dest.writeString(displayName);
        dest.writeInt(score);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        if (commonPokemon == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(commonPokemon);
        }
        if (uncommonPokemon == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(uncommonPokemon);
        }
        if (rarePokemon == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(rarePokemon);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PokeLocation> CREATOR = new Parcelable.Creator<PokeLocation>() {
        @Override
        public PokeLocation createFromParcel(Parcel in) {
            return new PokeLocation(in);
        }

        @Override
        public PokeLocation[] newArray(int size) {
            return new PokeLocation[size];
        }
    };
}
