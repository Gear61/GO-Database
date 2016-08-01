package com.randomappsinc.pokemonlocations_pokemongo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokemonDO;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocation implements Parcelable {
    public static final String KEY = "pokeLocation";
    public static final int NUM_PREVIEWS = 7;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("display_main")
    @Expose
    private String displayName;

    @SerializedName("display_secondary")
    @Expose
    private String address;

    @SerializedName("up_votes")
    @Expose
    private int score;

    @SerializedName("lat")
    @Expose
    private double latitude;

    @SerializedName("long")
    @Expose
    private double longitude;

    @SerializedName("common_pokemon")
    @Expose
    private List<Integer> commonPokemon = new ArrayList<>();

    @SerializedName("uncommon_pokemon")
    @Expose
    private List<Integer> uncommonPokemon = new ArrayList<>();

    @SerializedName("rare_pokemon")
    @Expose
    private List<Integer> rarePokemon = new ArrayList<>();

    public PokeLocation() {}

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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

    public List<Integer> getPokemonPreviews(int seedId) {
        List<Integer> previewIds = new ArrayList<>();
        if (seedId > 0) {
            previewIds.add(seedId);
        }

        List<Integer> masterList = new ArrayList<>();
        for (Integer pokemonId : rarePokemon) {
            if (pokemonId != seedId) {
                masterList.add(pokemonId);
            }
        }
        for (Integer pokemonId : uncommonPokemon) {
            if (pokemonId != seedId) {
                masterList.add(pokemonId);
            }
        }
        for (Integer pokemonId : commonPokemon) {
            if (pokemonId != seedId) {
                masterList.add(pokemonId);
            }
        }

        int seedPresence = seedId > 0 ? 1 : 0;
        for (int i = 0; i < NUM_PREVIEWS - seedPresence && i < masterList.size(); i++) {
            previewIds.add(masterList.get(i));
        }

        return previewIds;
    }

    public int getExtraPokemon() {
        int totalPokemon = commonPokemon.size() + uncommonPokemon.size() + rarePokemon.size();
        return totalPokemon - NUM_PREVIEWS + 1;
    }

    public PokeLocationDO toPokeLocationDO() {
        PokeLocationDO placeDO = new PokeLocationDO();

        placeDO.setPlaceId(placeId);
        placeDO.setDisplayName(displayName);
        placeDO.setAddress(address);
        placeDO.setScore(score);
        placeDO.setLatitude(latitude);
        placeDO.setLongitude(longitude);

        RealmList<PokemonDO> commonPokemonDOs = new RealmList<>();
        for (Integer pokemonId : commonPokemon) {
            PokemonDO pokemonDO = new PokemonDO();
            pokemonDO.setPokemonId(pokemonId);
            commonPokemonDOs.add(pokemonDO);
        }
        placeDO.setCommonPokemon(commonPokemonDOs);

        RealmList<PokemonDO> uncommonPokemonDOs = new RealmList<>();
        for (Integer pokemonId : uncommonPokemon) {
            PokemonDO pokemonDO = new PokemonDO();
            pokemonDO.setPokemonId(pokemonId);
            uncommonPokemonDOs.add(pokemonDO);
        }
        placeDO.setUncommonPokemon(uncommonPokemonDOs);

        RealmList<PokemonDO> rarePokemonDOs = new RealmList<>();
        for (Integer pokemonId : rarePokemon) {
            PokemonDO pokemonDO = new PokemonDO();
            pokemonDO.setPokemonId(pokemonId);
            rarePokemonDOs.add(pokemonDO);
        }
        placeDO.setRarePokemon(rarePokemonDOs);

        return placeDO;
    }

    protected PokeLocation(Parcel in) {
        placeId = in.readString();
        displayName = in.readString();
        address = in.readString();
        score = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
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
        dest.writeString(placeId);
        dest.writeString(displayName);
        dest.writeString(address);
        dest.writeInt(score);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
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
