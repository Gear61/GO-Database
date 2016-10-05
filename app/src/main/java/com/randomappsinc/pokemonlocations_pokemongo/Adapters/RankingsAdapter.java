package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 10/4/16.
 */

public class RankingsAdapter extends BaseAdapter {
    private Context context;
    private List<Pokemon> pokemonList;
    private int currentSortIndex;

    public RankingsAdapter(Context context) {
        this.context = context;
        this.pokemonList = DatabaseManager.get().getPokemonDBManager().getPokemonRanked(currentSortIndex);
    }

    public void setSortOption(int sortIndex) {
        this.currentSortIndex = sortIndex;
        this.pokemonList = DatabaseManager.get().getPokemonDBManager().getPokemonRanked(currentSortIndex);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pokemonList.size();
    }

    @Override
    public Pokemon getItem(int position) {
        return pokemonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class PokemonViewHolder {
        @Bind(R.id.pokemon_ranking) TextView pokemonRanking;
        @Bind(R.id.pokemon_icon) ImageView pokemonIcon;
        @Bind(R.id.pokemon_name) TextView pokemonName;
        @Bind(R.id.pokemon_stat) TextView pokemonStat;

        @BindString(R.string.percentage) String percentage;

        public PokemonViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadPokemon(int position) {
            Pokemon pokemon = getItem(position);

            String rankingText = "";
            switch (currentSortIndex) {
                case 0:
                    rankingText = "#" + pokemon.getMaxCpRanking();
                    break;
                case 1:
                    rankingText = "#" + pokemon.getAttackRanking();
                    break;
                case 2:
                    rankingText = "#" + pokemon.getDefenseRanking();
                    break;
                case 3:
                    rankingText = "#" + pokemon.getStaminaRanking();
                    break;
                case 4:
                    rankingText = "#" + pokemon.getCaptureRateRanking();
                    break;
                case 5:
                    rankingText = "#" + pokemon.getFleeRateRanking();
                    break;
            }
            pokemonRanking.setText(rankingText);

            if (PreferencesManager.get().areImagesEnabled()) {
                Picasso.with(context)
                        .load(PokemonUtils.getPokemonIcon(pokemon.getId()))
                        .into(pokemonIcon);
                pokemonIcon.setVisibility(View.VISIBLE);
            } else {
                pokemonIcon.setVisibility(View.GONE);
            }

            pokemonName.setText(pokemon.getName());

            switch (currentSortIndex) {
                case 0:
                    pokemonStat.setText(String.valueOf(pokemon.getMaxCp()));
                    break;
                case 1:
                    pokemonStat.setText(String.valueOf(pokemon.getBaseAttack()));
                    break;
                case 2:
                    pokemonStat.setText(String.valueOf(pokemon.getBaseDefense()));
                    break;
                case 3:
                    pokemonStat.setText(String.valueOf(pokemon.getBaseStamina()));
                    break;
                case 4:
                    pokemonStat.setText(String.format(percentage, pokemon.getBaseCaptureRate()));
                    break;
                case 5:
                    pokemonStat.setText(String.format(percentage, pokemon.getBaseFleeRate()));
                    break;
            }
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PokemonViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.ranking_cell, parent, false);
            holder = new PokemonViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PokemonViewHolder) view.getTag();
        }
        holder.loadPokemon(position);
        return view;
    }
}
