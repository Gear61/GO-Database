package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/24/16.
 */
public class ArtistsAdapter extends BaseAdapter {
    private Context context;
    private String[] artists;

    public ArtistsAdapter(Context context) {
        this.context = context;
        this.artists = context.getResources().getStringArray(R.array.artists);
    }

    @Override
    public int getCount() {
        return artists.length;
    }

    @Override
    public String getItem(int position) {
        return artists[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ArtistViewHolder {
        @Bind(R.id.artist_name) TextView artistName;

        public ArtistViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadItem(int position) {
            artistName.setText(artists[position]);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ArtistViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.artist_cell, parent, false);
            holder = new ArtistViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ArtistViewHolder) view.getTag();
        }
        holder.loadItem(position);
        return view;
    }
}
