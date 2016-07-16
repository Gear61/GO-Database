package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.StateServer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/16/16.
 */
public class StateACAdapter extends ArrayAdapter<String> {
    private ArrayList<String> suggestions;
    private Context context;

    @SuppressWarnings("unchecked")
    public StateACAdapter(Context context, int viewResourceId, ArrayList<String> items) {
        super(context, viewResourceId, items);
        this.context = context;
        this.suggestions = new ArrayList<>();
    }

    public class StateSuggestionViewHolder {
        @Bind(R.id.state) TextView state;

        public StateSuggestionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadState(int position) {
            if (position < suggestions.size()) {
                state.setText(suggestions.get(position));
            }
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        StateSuggestionViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.state_ac_item, parent, false);
            holder = new StateSuggestionViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (StateSuggestionViewHolder) view.getTag();
        }
        holder.loadState(position);
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
                suggestions.addAll(StateServer.get().getMatchingStates(constraint.toString()));

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
