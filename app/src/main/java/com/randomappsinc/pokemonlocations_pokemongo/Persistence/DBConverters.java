package com.randomappsinc.pokemonlocations_pokemongo.Persistence;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokedexPokemonDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokemonDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 9/23/16.
 */

public class DBConverters {
    public static PokeLocation getLocationFromDO(PokeLocationDO locationDO) {
        PokeLocation location = new PokeLocation();

        location.setPlaceId(locationDO.getPlaceId());
        location.setDisplayName(locationDO.getDisplayName());
        location.setAddress(locationDO.getAddress());
        location.setLatitude(locationDO.getLatitude());
        location.setLongitude(locationDO.getLongitude());
        location.setNumLikes(locationDO.getNumLikes());
        location.setNumDislikes(locationDO.getNumDislikes());

        List<Integer> commonPokemon = new ArrayList<>();
        for (PokemonDO pokemonDO : locationDO.getCommonPokemon()) {
            commonPokemon.add(pokemonDO.getPokemonId());
        }
        location.setCommonPokemon(commonPokemon);

        List<Integer> uncommonPokemon = new ArrayList<>();
        for (PokemonDO pokemonDO : locationDO.getUncommonPokemon()) {
            uncommonPokemon.add(pokemonDO.getPokemonId());
        }
        location.setUncommonPokemon(uncommonPokemon);

        List<Integer> rarePokemon = new ArrayList<>();
        for (PokemonDO pokemonDO : locationDO.getRarePokemon()) {
            rarePokemon.add(pokemonDO.getPokemonId());
        }
        location.setRarePokemon(rarePokemon);

        return location;
    }

    public static Pokemon getPokemonFromDO(PokedexPokemonDO pokemonDO) {
        Pokemon pokemon = new Pokemon();
        pokemon.setId(pokemonDO.getPokemonId());
        pokemon.setName(pokemonDO.getName());
        pokemon.setType1(pokemonDO.getType1());
        pokemon.setType2(pokemonDO.getType2());
        pokemon.setMaxCp(pokemonDO.getMaxCp());
        pokemon.setBaseAttack(pokemonDO.getBaseAttack());
        pokemon.setBaseDefense(pokemonDO.getBaseDefense());
        pokemon.setBaseStamina(pokemonDO.getBaseStamina());
        pokemon.setBaseCaptureRate(pokemonDO.getBaseCaptureRate());
        pokemon.setBaseFleeRate(pokemonDO.getBaseFleeRate());
        pokemon.setCandyToEvolve(pokemonDO.getCandyToEvolve());
        pokemon.setAvgCpGain(pokemonDO.getAvgCpGain());
        pokemon.setMaxCpRanking(pokemonDO.getMaxCpRanking());
        pokemon.setAttackRanking(pokemonDO.getAttackRanking());
        pokemon.setDefenseRanking(pokemonDO.getDefenseRanking());
        pokemon.setStaminaRanking(pokemonDO.getStaminaRanking());
        pokemon.setCaptureRateRanking(pokemonDO.getCaptureRateRanking());
        pokemon.setFleeRateRanking(pokemonDO.getFleeRateRanking());
        return pokemon;
    }
}
