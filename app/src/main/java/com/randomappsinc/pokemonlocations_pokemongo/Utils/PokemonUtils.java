package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import android.content.Context;
import android.view.View;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.EggDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonUtils {
    public static final String NORMAL = "Normal";
    public static final String FIGHTING = "Fighting";
    public static final String FLYING = "Flying";
    public static final String POISON = "Poison";
    public static final String GROUND = "Ground";
    public static final String ROCK = "Rock";
    public static final String BUG = "Bug";
    public static final String GHOST = "Ghost";
    public static final String STEEL = "Steel";
    public static final String FIRE = "Fire";
    public static final String WATER = "Water";
    public static final String GRASS = "Grass";
    public static final String ELECTRIC = "Electric";
    public static final String PSYCHIC = "Psychic";
    public static final String ICE = "Ice";
    public static final String DRAGON = "Dragon";
    public static final String DARK = "Dark";
    public static final String FAIRY = "Fairy";

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
            while ((nbread = fileStream.read(data)) > -1) {
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

    public static void setTypeBackground(View typeContainer, String type) {
        switch (type) {
            case NORMAL:
                typeContainer.setBackgroundResource(R.drawable.normal_background);
                break;
            case FIGHTING:
                typeContainer.setBackgroundResource(R.drawable.fighting_background);
                break;
            case FLYING:
                typeContainer.setBackgroundResource(R.drawable.flying_background);
                break;
            case POISON:
                typeContainer.setBackgroundResource(R.drawable.poison_background);
                break;
            case GROUND:
                typeContainer.setBackgroundResource(R.drawable.ground_background);
                break;
            case ROCK:
                typeContainer.setBackgroundResource(R.drawable.rock_background);
                break;
            case BUG:
                typeContainer.setBackgroundResource(R.drawable.bug_background);
                break;
            case GHOST:
                typeContainer.setBackgroundResource(R.drawable.ghost_background);
                break;
            case STEEL:
                typeContainer.setBackgroundResource(R.drawable.steel_background);
                break;
            case FIRE:
                typeContainer.setBackgroundResource(R.drawable.fire_background);
                break;
            case WATER:
                typeContainer.setBackgroundResource(R.drawable.water_background);
                break;
            case GRASS:
                typeContainer.setBackgroundResource(R.drawable.grass_background);
                break;
            case ELECTRIC:
                typeContainer.setBackgroundResource(R.drawable.electric_background);
                break;
            case PSYCHIC:
                typeContainer.setBackgroundResource(R.drawable.psychic_background);
                break;
            case ICE:
                typeContainer.setBackgroundResource(R.drawable.ice_background);
                break;
            case DRAGON:
                typeContainer.setBackgroundResource(R.drawable.dragon_background);
                break;
            case DARK:
                typeContainer.setBackgroundResource(R.drawable.dark_background);
                break;
            case FAIRY:
                typeContainer.setBackgroundResource(R.drawable.fairy_background);
                break;
        }
    }

    public static String getEggInfo(Pokemon pokemon) {
        EggDO eggDO = DatabaseManager.get().getEggsDBManager().getEgg(pokemon);
        if (eggDO != null) {
            String eggDistance = String.valueOf(eggDO.getDistance());
            double numEggs = 100/(eggDO.getChance());
            double distance = eggDO.getDistance() * numEggs;

            String numEggsText = String.format(Locale.US, "%.1f", numEggs);
            String distanceText = String.format(Locale.US, "%.1f", distance);

            return pokemon.getName() + " has a <b>" + eggDO.getChance() + "%</b> chance of hatching from a <b>" +
                    eggDO.getDistance() + "km</b> egg. This means that you need to walk an average of <b>" +
                    distanceText + "km</b> to hatch <b>" + numEggsText + "</b> " + eggDistance +
                    "km eggs in order to get one.";

        } else {
            return MyApplication.getAppContext().getString(R.string.does_not_hatch);
        }
    }
}
