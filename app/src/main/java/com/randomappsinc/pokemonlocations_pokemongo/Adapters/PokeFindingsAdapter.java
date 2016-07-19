package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/18/16.
 */
public class PokeFindingsAdapter extends BaseAdapter {
    private Context context;
    private List<PokeFindingDO> findings;
    private View noFindings;

    public PokeFindingsAdapter(Context context, View noFindings) {
        this.context = context;
        this.noFindings = noFindings;
        syncWithDb();
    }

    public void syncWithDb() {
        this.findings = DatabaseManager.get().getFindings();
        notifyDataSetChanged();
        setNoContent();
    }

    public void setNoContent() {
        int viewVisibility = findings.isEmpty() ? View.VISIBLE : View.GONE;
        noFindings.setVisibility(viewVisibility);
    }

    @Override
    public int getCount() {
        return findings.size();
    }

    @Override
    public PokeFindingDO getItem(int position) {
        return findings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class PokeFindingViewHolder {
        @Bind(R.id.pokemon_icon) ImageView pokemonPicture;
        @Bind(R.id.finding_info) TextView findingInfo;

        public PokeFindingViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @SuppressWarnings("deprecation")
        public void loadItem(PokeFindingDO pokeFindingDO) {
            String pokemonName = PokemonServer.get().getPokemonName(pokeFindingDO.getPokemonId());
            Picasso.with(context)
                    .load(PokemonUtils.getPokemonUrl(pokemonName))
                    .into(pokemonPicture);
            findingInfo.setText(PokemonUtils.getFindingInfo(pokeFindingDO));
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PokeFindingViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.pokefinding_cell, parent, false);
            holder = new PokeFindingViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PokeFindingViewHolder) view.getTag();
        }
        holder.loadItem(getItem(position));
        return view;
    }
}
