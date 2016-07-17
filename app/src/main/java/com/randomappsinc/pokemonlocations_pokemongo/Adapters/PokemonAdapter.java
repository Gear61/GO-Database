package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {


        public PokemonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
