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

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 8/11/16.
 */
public class PlaceSuggestionsAdapter extends BaseAdapter {
    private Context context;
    private List<PokeLocation> suggestions;
    private View googlePowered;

    public PlaceSuggestionsAdapter(Context context, View googlePowered) {
        this.context = context;
        this.suggestions = new ArrayList<>();
        this.googlePowered = googlePowered;
    }

    public void setSuggestions(List<PokeLocation> suggestions) {
        this.suggestions = suggestions;
        if (suggestions.isEmpty()) {
            googlePowered.setVisibility(View.VISIBLE);
        } else {
            googlePowered.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public PokeLocation getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class PlaceSuggestionViewHolder {
        @Bind(R.id.place_name) TextView displayName;
        @Bind(R.id.place_address) TextView address;
        @BindString(R.string.no_address) String noAddress;

        public PlaceSuggestionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadItem(int position) {
            displayName.setText(getItem(position).getDisplayName());

            String addressText = getItem(position).getAddress();
            if (addressText.isEmpty()) {
                address.setText(noAddress);
            } else {
                address.setText(addressText);
            }
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PlaceSuggestionViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.place_suggestion, parent, false);
            holder = new PlaceSuggestionViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PlaceSuggestionViewHolder) view.getTag();
        }
        holder.loadItem(position);
        return view;
    }
}
