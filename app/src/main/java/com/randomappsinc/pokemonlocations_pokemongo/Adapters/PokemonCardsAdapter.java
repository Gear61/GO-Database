package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.randomappsinc.pokemonlocations_pokemongo.Fragments.PokemonFragment;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;

import java.util.List;

/**
 * Created by alexanderchiou on 11/2/16.
 */

public class PokemonCardsAdapter extends FragmentStatePagerAdapter {
    private List<Pokemon> pokemon;

    public PokemonCardsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.pokemon = DatabaseManager.get().getPokemonDBManager().getPokemon();
    }

    public Pokemon getPokemon(int position) {
        return pokemon.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return PokemonFragment.create(pokemon.get(position));
    }

    @Override
    public int getCount() {
        return pokemon.size();
    }
}
