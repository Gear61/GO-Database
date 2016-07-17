package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private Context context;
    private List<Integer> pokemon;

    public PokemonAdapter(Context context, List<Integer> pokemon) {
        this.context = context;
        this.pokemon = pokemon;
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_cell, parent, false);
        return new PokemonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        holder.loadPokemon(position);
    }

    @Override
    public int getItemCount() {
        return pokemon.size();
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pokemon_icon) ImageView pokemonPicture;

        public PokemonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void loadPokemon(int position) {
            String pokemonName = PokemonServer.get().getPokemonName(pokemon.get(position));
            String imageUrl = UIUtils.getPokemonUrl(pokemonName);
            Picasso.with(context).load(imageUrl).into(pokemonPicture);
        }
    }
}
