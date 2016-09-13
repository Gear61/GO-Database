package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 9/12/16.
 */
public class PokedexAdapter extends BaseAdapter {
    private Context context;
    private List<Pokemon> allPokemon;
    private List<Pokemon> matchingPokemon;

    public PokedexAdapter(Context context) {
        this.context = context;
        this.allPokemon = PokemonServer.get().getPokemonList();
        this.matchingPokemon = new ArrayList<>();
        this.matchingPokemon.addAll(allPokemon);
    }

    public void filterPokemon(String searchTerm) {
        String cleanTerm = searchTerm.toLowerCase().trim();

        matchingPokemon.clear();

        if (searchTerm.isEmpty()) {
            matchingPokemon.addAll(allPokemon);
        } else {
            for (Pokemon pokemon : allPokemon) {
                if (pokemon.getName().toLowerCase().contains(cleanTerm)) {
                    matchingPokemon.add(pokemon);
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return matchingPokemon.size();
    }

    @Override
    public Pokemon getItem(int position) {
        return matchingPokemon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class PokedexViewHolder {
        @Bind(R.id.pokemon_id) TextView pokemonId;
        @Bind(R.id.pokemon_icon) ImageView pokemonIcon;
        @Bind(R.id.pokemon_name) TextView pokemonName;

        public PokedexViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadPokemon(int position) {
            pokemonId.setText(String.format(Locale.US, "#%03d", getItem(position).getId()));

            if (PreferencesManager.get().areImagesEnabled()) {
                Picasso.with(context)
                        .load(PokemonUtils.getPokemonIcon(getItem(position).getId()))
                        .into(pokemonIcon);
                pokemonIcon.setVisibility(View.VISIBLE);
            } else {
                pokemonIcon.setVisibility(View.GONE);
            }

            pokemonName.setText(getItem(position).getName());
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PokedexViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.pokedex_cell, parent, false);
            holder = new PokedexViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PokedexViewHolder) view.getTag();
        }
        holder.loadPokemon(position);
        return view;
    }
}
