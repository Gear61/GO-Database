package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class FavoritesAdapter extends BaseAdapter {
    private Context context;
    private List<PokeLocation> allFavorites;
    private List<PokeLocation> matchingPlaces;
    private TextView noFavorites;
    private View searchBar;
    private String searchTerm;

    public FavoritesAdapter(Context context, TextView noFavorites, View searchBar) {
        this.context = context;
        this.noFavorites = noFavorites;
        this.matchingPlaces = new ArrayList<>();
        this.searchBar = searchBar;
        this.searchTerm = "";
        syncWithDb();
    }

    public void syncWithDb() {
        this.allFavorites = DatabaseManager.get().getFavorites();
        applySearch();
    }

    public void setNoContent() {
        int noContentVisibility = matchingPlaces.isEmpty() ? View.VISIBLE : View.GONE;
        noFavorites.setVisibility(noContentVisibility);
        int noContentId = allFavorites.isEmpty() ? R.string.no_favorites : R.string.no_favorite_matches;
        noFavorites.setText(noContentId);

        int searchVisibility = allFavorites.isEmpty() ? View.GONE : View.VISIBLE;
        searchBar.setVisibility(searchVisibility);
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
        applySearch();
    }

    private void applySearch() {
        matchingPlaces.clear();
        if (searchTerm.isEmpty()) {
            matchingPlaces.addAll(allFavorites);
        } else {
            for (PokeLocation favorite : allFavorites) {
                if (favorite.getSearchBlurb().contains(searchTerm)) {
                    matchingPlaces.add(favorite);
                }
            }
        }

        notifyDataSetChanged();
        setNoContent();
    }

    @Override
    public int getCount() {
        return matchingPlaces.size();
    }

    @Override
    public PokeLocation getItem(int position) {
        return matchingPlaces.get(position);
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

        LatLong currentLatLong = null;
        String currentLocation = PreferencesManager.get().getCurrentLocation();
        if (!currentLocation.equals(context.getString(R.string.automatic))) {
            SavedLocationDO savedLocation = DatabaseManager.get().getLocation(currentLocation);
            currentLatLong = new LatLong(savedLocation.getLatitude(), savedLocation.getLongitude());
        }

        holder.loadItem(getItem(position), -1, currentLatLong);
        return view;
    }
}
