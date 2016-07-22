package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class PokemonACAdapter extends ArrayAdapter<String> {
    private ArrayList<String> suggestions;
    private Context context;

    @SuppressWarnings("unchecked")
    public PokemonACAdapter(Context context, int viewResourceId, ArrayList<String> items) {
        super(context, viewResourceId, items);
        this.context = context;
        this.suggestions = new ArrayList<>();
    }

    public class PokemonSuggestionViewHolder {
        @Bind(R.id.pokemon_icon) ImageView pokemonIcon;
        @Bind(R.id.pokemon_name) TextView pokemonName;

        public PokemonSuggestionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadPokemon(int position) {
            if (position < suggestions.size()) {
                String pokemonText = suggestions.get(position);
                int pokemonId = PokemonServer.get().getPokemonId(pokemonText);
                Picasso.with(context)
                        .load(PokemonUtils.getPokemonIcon(pokemonId))
                        .into(pokemonIcon);
                pokemonName.setText(pokemonText);
            }
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        PokemonSuggestionViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.pokemon_ac_item, parent, false);
            holder = new PokemonSuggestionViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PokemonSuggestionViewHolder) view.getTag();
        }
        holder.loadPokemon(position);
        return view;
    }

    @Override
    public android.widget.Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            return (resultValue).toString();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                suggestions.addAll(PokemonServer.get().getMatchingPokemon(constraint.toString()));

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            List<String> filteredList = (ArrayList<String>) results.values;
            if (results.count > 0) {
                for (String c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}