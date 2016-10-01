package com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alexanderchiou on 9/23/16.
 */

public class PokedexPokemonDO extends RealmObject {
    @PrimaryKey
    private int pokemonId;

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
    private int maxCpRanking;
    private int attackRanking;
    private int defenseRanking;
    private int staminaRanking;
    private int captureRateRanking;
    private int fleeRateRanking;

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getName() {
        return name;
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

    public int getMaxCpRanking() {
        return maxCpRanking;
    }

    public void setMaxCpRanking(int maxCpRanking) {
        this.maxCpRanking = maxCpRanking;
    }

    public int getAttackRanking() {
        return attackRanking;
    }

    public void setAttackRanking(int attackRanking) {
        this.attackRanking = attackRanking;
    }

    public int getDefenseRanking() {
        return defenseRanking;
    }

    public void setDefenseRanking(int defenseRanking) {
        this.defenseRanking = defenseRanking;
    }

    public int getStaminaRanking() {
        return staminaRanking;
    }

    public void setStaminaRanking(int staminaRanking) {
        this.staminaRanking = staminaRanking;
    }

    public int getCaptureRateRanking() {
        return captureRateRanking;
    }

    public void setCaptureRateRanking(int captureRateRanking) {
        this.captureRateRanking = captureRateRanking;
    }

    public int getFleeRateRanking() {
        return fleeRateRanking;
    }

    public void setFleeRateRanking(int fleeRateRanking) {
        this.fleeRateRanking = fleeRateRanking;
    }
}
