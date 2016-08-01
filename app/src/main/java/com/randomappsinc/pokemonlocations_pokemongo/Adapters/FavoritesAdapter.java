package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.util.List;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class FavoritesAdapter extends BaseAdapter {
    private Context context;
    private List<PokeLocation> favorites;
    private View noFavorites;

    public FavoritesAdapter(Context context, View noFavorites) {
        this.context = context;
        this.noFavorites = noFavorites;
        syncWithDb();
    }

    public void syncWithDb() {
        this.favorites = DatabaseManager.get().getFavorites();
        notifyDataSetChanged();
        setNoContent();
    }

    public void setNoContent() {
        int viewVisibility = favorites.isEmpty() ? View.VISIBLE : View.GONE;
        noFavorites.setVisibility(viewVisibility);
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public PokeLocation getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PokeLocationViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.poke_location_cell, parent, false);
            holder = new PokeLocationViewHolder(view, context);
            view.setTag(holder);
        } else {
            holder = (PokeLocationViewHolder) view.getTag();
        }
        holder.loadItem(getItem(position), -1, null);
        return view;
    }
}
