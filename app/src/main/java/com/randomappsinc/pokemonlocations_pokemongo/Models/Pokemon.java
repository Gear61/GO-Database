package com.randomappsinc.pokemonlocations_pokemongo.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokedexPokemonDO;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class Pokemon implements Parcelable {
    public static final String ID_KEY = "ID_KEY";

    private int id;
    private String name;
    private String type1;
    private String type2;
    private int maxCp;
    private int baseAttack;
    private int baseDefense;
    private int baseStamina;
    private int baseCaptureRate;
    private int baseFleeRate;
    private int candyToEvolve;
    private double avgCpGain;

    public Pokemon() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getMaxCp() {
        return maxCp;
    }

    public void setMaxCp(int maxCp) {
        this.maxCp = maxCp;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public void setBaseDefense(int baseDefense) {
        this.baseDefense = baseDefense;
    }

    public int getBaseStamina() {
        return baseStamina;
    }

    public void setBaseStamina(int baseStamina) {
        this.baseStamina = baseStamina;
    }

    public int getBaseCaptureRate() {
        return baseCaptureRate;
    }

    public void setBaseCaptureRate(int baseCaptureRate) {
        this.baseCaptureRate = baseCaptureRate;
    }

    public int getBaseFleeRate() {
        return baseFleeRate;
    }

    public void setBaseFleeRate(int baseFleeRate) {
        this.baseFleeRate = baseFleeRate;
    }

    public int getCandyToEvolve() {
        return candyToEvolve;
    }

    public void setCandyToEvolve(int candyToEvolve) {
        this.candyToEvolve = candyToEvolve;
    }

    public double getAvgCpGain() {
        return avgCpGain;
    }

    public void setAvgCpGain(double avgCpGain) {
        this.avgCpGain = avgCpGain;
    }

    public PokedexPokemonDO toPokemonDO() {
        PokedexPokemonDO pokemonDO = new PokedexPokemonDO();
        pokemonDO.setPokemonId(id);
        pokemonDO.setName(name);
        pokemonDO.setType1(type1);
        pokemonDO.setType2(type2);
        pokemonDO.setMaxCp(maxCp);
        pokemonDO.setBaseAttack(baseAttack);
        pokemonDO.setBaseDefense(baseDefense);
        pokemonDO.setBaseStamina(baseStamina);
        pokemonDO.setBaseCaptureRate(baseCaptureRate);
        pokemonDO.setBaseFleeRate(baseFleeRate);
        pokemonDO.setCandyToEvolve(candyToEvolve);
        pokemonDO.setAvgCpGain(avgCpGain);
        return pokemonDO;
    }

    protected Pokemon(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type1 = in.readString();
        type2 = in.readString();
        maxCp = in.readInt();
        baseAttack = in.readInt();
        baseDefense = in.readInt();
        baseStamina = in.readInt();
        baseCaptureRate = in.readInt();
        baseFleeRate = in.readInt();
        candyToEvolve = in.readInt();
        avgCpGain = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(type1);
        dest.writeString(type2);
        dest.writeInt(maxCp);
        dest.writeInt(baseAttack);
        dest.writeInt(baseDefense);
        dest.writeInt(baseStamina);
        dest.writeInt(baseCaptureRate);
        dest.writeInt(baseFleeRate);
        dest.writeInt(candyToEvolve);
        dest.writeDouble(avgCpGain);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Pokemon> CREATOR = new Parcelable.Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };
}
