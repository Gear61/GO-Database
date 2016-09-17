package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 9/16/16.
 */
public class TeamsAdapter extends BaseAdapter {
    private Context context;
    private String[] teamNames;
    private int[] teamIcons;

    public TeamsAdapter(Context context) {
        this.context = context;
        this.teamNames = context.getResources().getStringArray(R.array.team_names);
        this.teamIcons = new int[] {R.drawable.mystic, R.drawable.valor, R.drawable.instinct};
    }

    @Override
    public int getCount() {
        return teamNames.length;
    }

    @Override
    public String getItem(int position) {
        return teamNames[position];
    }

    public int getTeamIcon(int position) {
        return teamIcons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class TeamViewHolder {
        @Bind(R.id.team_icon) ImageView teamIcon;
        @Bind(R.id.team_name) TextView teamName;

        public TeamViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadTeam(int position) {
            teamIcon.setImageResource(teamIcons[position]);
            teamName.setText(teamNames[position]);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TeamViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.team_cell, parent, false);
            holder = new TeamViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (TeamViewHolder) view.getTag();
        }
        holder.loadTeam(position);
        return view;
    }
}
