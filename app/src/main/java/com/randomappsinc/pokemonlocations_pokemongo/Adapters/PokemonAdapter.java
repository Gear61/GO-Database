package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.PokeLocationActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private PokeLocationActivity context;
    private List<Integer> pokemonList;

    public PokemonAdapter(PokeLocationActivity context, List<Integer> pokemonList) {
        this.context = context;
        this.pokemonList = pokemonList;
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
        return pokemonList.size();
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pokemon_icon) ImageView pokemonPicture;
        @BindString(R.string.add_finding_question) String addFindingQuestion;

        private Pokemon pokemon;

        public PokemonViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void loadPokemon(int position) {
            pokemon = new Pokemon();
            pokemon.setId(pokemonList.get(position));
            pokemon.setName(PokemonServer.get().getPokemonName(pokemonList.get(position)));

            String imageUrl = PokemonUtils.getPokemonUrl(pokemon.getName());
            Picasso.with(context).load(imageUrl).into(pokemonPicture);
        }

        @OnClick(R.id.pokemon_parent)
        public void addPokeFinding() {
            new MaterialDialog.Builder(context)
                    .content(String.format(addFindingQuestion, pokemon.getName(), context.getPlace().getDisplayName()))
                    .items(R.array.flag_frequency_options)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            context.submitPokefinding(pokemon, text.toString());
                            return true;
                        }
                    })
                    .positiveText(R.string.choose)
                    .negativeText(android.R.string.no)
                    .show();
        }
    }
}
