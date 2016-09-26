package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.EggDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 9/25/16.
 */

public class EggsAdapter extends BaseAdapter {
    private Context context;
    private List<EggDO> eggList;

    public EggsAdapter(Context context, int distance) {
        this.context = context;
        this.eggList = DatabaseManager.get().getEggsDBManager().getEggs(distance);
    }

    @Override
    public int getCount() {
        return eggList.size();
    }

    @Override
    public EggDO getItem(int position) {
        return eggList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class EggViewHolder {
        @Bind(R.id.pokemon_icon) ImageView pokemonIcon;
        @Bind(R.id.pokemon_name) TextView pokemonName;
        @Bind(R.id.pokemon_chance) TextView pokemonChance;

        public EggViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadPokemon(int position) {
            int pokemonId = getItem(position).getPokemonId();
            if (PreferencesManager.get().areImagesEnabled()) {
                Picasso.with(context)
                        .load(PokemonUtils.getPokemonIcon(pokemonId))
                        .into(pokemonIcon);
                pokemonIcon.setVisibility(View.VISIBLE);
            } else {
                pokemonIcon.setVisibility(View.GONE);
            }

            pokemonName.setText(PokemonServer.get().getPokemonName(pokemonId));

            DecimalFormat formatter = new DecimalFormat("0.0");
            String chance = formatter.format(getItem(position).getChance()) + "%";
            pokemonChance.setText(chance);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EggViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.egg_cell, parent, false);
            holder = new EggViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (EggViewHolder) view.getTag();
        }
        holder.loadPokemon(position);
        return view;
    }
}
