package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.PokemonPosting;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 7/22/16.
 */
public class AddPokemonAdapter extends RecyclerView.Adapter<AddPokemonAdapter.PokePostingViewHolder> {
    private Context context;
    private List<PokemonPosting> postings;

    public AddPokemonAdapter(Context context) {
        this.context = context;
        this.postings = new ArrayList<>();
    }

    public void addPokemonPosting(PokemonPosting posting) {
        postings.add(posting);
        notifyDataSetChanged();
    }

    public void clearPostings() {
        postings.clear();
        notifyDataSetChanged();
    }

    public void deletePokemon(int position) {
        postings.remove(position);
        notifyDataSetChanged();
    }

    public List<PokemonPosting> getPostings(PokeLocation location) {
        List<PokemonPosting> purePostings = new ArrayList<>();
        // Strip away postings the user has already submitted
        for (PokemonPosting posting : postings) {
            if (DatabaseManager.get().getFinding(posting.getPokemonId(), location) == null) {
                purePostings.add(posting);
            }
        }
        return purePostings;
    }

    public Set<String> getAlreadyAdded() {
        Set<String> alreadyAdded = new HashSet<>();
        for (PokemonPosting posting : postings) {
            alreadyAdded.add(PokemonServer.get().getPokemonName(posting.getPokemonId()).toLowerCase());
        }
        return alreadyAdded;
    }

    @Override
    public PokePostingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_cell, parent, false);
        return new PokePostingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PokePostingViewHolder holder, int position) {
        holder.loadPokemon(position);
    }

    @Override
    public int getItemCount() {
        return postings.size();
    }

    public class PokePostingViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pokemon_icon) ImageView pokemonPicture;
        @Bind(R.id.poke_icon) TextView rarityIcon;
        @BindString(R.string.delete_pokemon_confirmation) String deleteTemplate;

        private int position;

        public PokePostingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void loadPokemon(int position) {
            this.position = position;
            PokemonPosting posting = postings.get(position);
            Picasso.with(context)
                    .load(PokemonUtils.getPokemonIcon(posting.getPokemonId()))
                    .into(pokemonPicture);
            String rarity = PokemonUtils.getFrequency(posting.getRarity());
            rarityIcon.setText(rarity.subSequence(0, 1));
        }

        @OnClick(R.id.pokemon_parent)
        public void addPokeFinding() {
            PokemonPosting posting = postings.get(position);
            String pokemonName = PokemonServer.get().getPokemonName(posting.getPokemonId());
            new MaterialDialog.Builder(context)
                    .title(R.string.delete_title)
                    .content(String.format(deleteTemplate, pokemonName))
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deletePokemon(position);
                        }
                    })
                    .show();
        }
    }
}
