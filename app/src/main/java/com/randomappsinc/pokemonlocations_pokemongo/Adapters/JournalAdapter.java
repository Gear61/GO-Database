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
import com.randomappsinc.pokemonlocations_pokemongo.Utils.FeedUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/18/16.
 */
public class JournalAdapter extends BaseAdapter {
    private Context context;
    private List<PokeFindingDO> findings;
    private View noFindings;

    public JournalAdapter(Context context, View noFindings) {
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

    public class JournalEntryViewHolder {
        @Bind(R.id.pokemon_icon) ImageView pokemonPicture;
        @Bind(R.id.finding_info) TextView findingInfo;
        @Bind(R.id.timestamp) TextView timestamp;

        @BindString(R.string.journal_entry_template) String journalEntry;

        public JournalEntryViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadItem(PokeFindingDO pokeFindingDO) {
            Picasso.with(context)
                    .load(PokemonUtils.getPokemonIcon(pokeFindingDO.getPokemonId()))
                    .into(pokemonPicture);
            findingInfo.setText(PokemonUtils.getFindingInfo(pokeFindingDO));

            if (pokeFindingDO.getReportTime() == 0) {
                // If the finding DO has a report time of 0, set it to current time
                DatabaseManager.get().updatePokeFinding(pokeFindingDO, null);
            }

            timestamp.setText(FeedUtils.humanizeUnixTime(pokeFindingDO.getReportTime()));
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        JournalEntryViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.journal_entry, parent, false);
            holder = new JournalEntryViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (JournalEntryViewHolder) view.getTag();
        }
        holder.loadItem(getItem(position));
        return view;
    }
}
