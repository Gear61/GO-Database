package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/18/16.
 */
public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<PokeLocation> results;
    private TextView noResults;

    public SearchAdapter(Context context, TextView noResults) {
        this.context = context;
        this.results = new ArrayList<>();
        this.noResults = noResults;
    }

    public void processResults(List<PokeLocation> locations) {
        this.results = locations;
        notifyDataSetChanged();
        setNoContent();
    }

    public void setNoContent() {
        noResults.setText(R.string.no_results);
        int viewVisibility = results.isEmpty() ? View.VISIBLE : View.GONE;
        noResults.setVisibility(viewVisibility);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public PokeLocation getItem(int position) {
        return results.get(position);
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
            view = vi.inflate(R.layout.pokelocation_cell, parent, false);
            holder = new PokeLocationViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PokeLocationViewHolder) view.getTag();
        }
        holder.loadItem(getItem(position));
        return view;
    }
}
