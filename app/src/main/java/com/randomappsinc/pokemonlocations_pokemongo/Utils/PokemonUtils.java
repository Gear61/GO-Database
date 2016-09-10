package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.content.Context;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokemonDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonUtils {
    public static File getPokemonIcon(int pokemonId) {
        Context context = MyApplication.getAppContext();
        File file = new File(context.getFilesDir() +  "/" + String.valueOf(pokemonId) + ".png");
        FileOutputStream fos = null;
        try {
            InputStream fileStream = MyApplication.getAppContext().getAssets()
                    .open("icons/" + String.valueOf(pokemonId) + ".png");
            byte[] data = new byte[2048];
            int nbread;
            fos = new FileOutputStream(file);
            while((nbread = fileStream.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        }
        catch (Exception ignored) {}
        finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (Exception ignored) {}
            }
        }
        return file;
    }

    public static float getFrequencyFromIndex(int index) {
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

    public static String getFrequencyTextFromScore(float score) {
        Context context = MyApplication.getAppContext();
        if (score == 3F) {
            return context.getString(R.string.common);
        } else if (score == 2F) {
            return context.getString(R.string.uncommon);
        } else if (score == 1F) {
            return context.getString(R.string.rare);
        } else if (score == -0.5F) {
            return context.getString(R.string.non_existent);
        } else {
            return context.getString(R.string.common);
        }
    }

    public static float getFrequencyFromHeader(String option) {
        Context context = MyApplication.getAppContext();
        if (option.equals(context.getString(R.string.common_option))) {
            return 3F;
        } else if (option.equals(context.getString(R.string.uncommon_option))) {
            return 2F;
        } else if (option.equals(context.getString(R.string.rare_option))) {
            return 1F;
        } else {
            return 3F;
        }
    }

    public static float getFrequencyScoreFromText(String option) {
        Context context = MyApplication.getAppContext();
        if (option.equals(context.getString(R.string.common))) {
            return 3F;
        } else if (option.equals(context.getString(R.string.uncommon))) {
            return 2F;
        } else if (option.equals(context.getString(R.string.rare))) {
            return 1F;
        } else if (option.equals(context.getString(R.string.non_existent))) {
            return -0.5F;
        } else {
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

    public static String getFindingInfo(PokeFindingDO pokeFindingDO) {
        String journalTemplate = MyApplication.getAppContext().getString(R.string.journal_entry_template);
        String pokemonName = PokemonServer.get().getPokemonName(pokeFindingDO.getPokemonId());
        String frequency = pokeFindingDO.getFrequency().toLowerCase();
        String locationName = pokeFindingDO.getLocationName();
        return String.format(journalTemplate, pokemonName, frequency, locationName);
    }

    public static String getPokemonString(List<Integer> pokemonIds) {
        StringBuilder pokemonString = new StringBuilder();
        for (Integer pokemonId : pokemonIds) {
            pokemonString.append(PokemonServer.get().getPokemonName(pokemonId));
        }
        return pokemonString.toString();
    }
}
