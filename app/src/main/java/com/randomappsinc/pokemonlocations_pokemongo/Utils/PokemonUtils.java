package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokemonDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonUtils {
    public static String getPokemonUrl(String pokemonName) {
        String url = "https://img.pokemondb.net/artwork/%s.jpg";
        if (pokemonName.equals("Nidoran♀")) {
            pokemonName = "nidoran-f";
        }
        if (pokemonName.equals("Nidoran♂")) {
            pokemonName = "nidoran-m";
        }
        return String.format(url, pokemonName.toLowerCase());
    }

    public static float getFrequency(int index) {
        switch (index) {
            case 0:
                return 3F;
            case 1:
                return 2F;
            case 2:
                return 1F;
            case 3:
                return -0.5F;
            default:
                return 3F;
        }
    }

    public static PokeLocation getLocationFromDO(PokeLocationDO locationDO) {
        PokeLocation location = new PokeLocation();

        location.setPlaceId(locationDO.getPlaceId());
        location.setDisplayName(locationDO.getDisplayName());
        location.setAddress(locationDO.getAddress());
        location.setLatitude(locationDO.getLatitude());
        location.setLongitude(locationDO.getLongitude());
        location.setScore(locationDO.getScore());

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

    @SuppressWarnings("deprecation")
    public static Spanned getFindingInfo(PokeFindingDO pokeFindingDO) {
        Context context = MyApplication.getAppContext();
        String pokemonName = PokemonServer.get().getPokemonName(pokeFindingDO.getPokemonId());
        String pokemonPrefix = context.getString(R.string.pokemon_prefix);
        String locationPrefix = context.getString(R.string.location_prefix);
        String frequencyPrefix = context.getString(R.string.frequency_prefix);
        String info = "<b>" + pokemonPrefix + "</b>" + pokemonName + "<br><b>"
                + locationPrefix + "</b>" + pokeFindingDO.getLocationName() + "<br><b>"
                + frequencyPrefix + "</b>" + pokeFindingDO.getFrequency();
        return Html.fromHtml(info);
    }
}
