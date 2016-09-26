package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class SettingsAdapter extends BaseAdapter {
    private Context context;
    private String[] itemNames;
    private String[] itemIcons;

    public SettingsAdapter(Context context) {
        this.context = context;
        this.itemNames = context.getResources().getStringArray(R.array.settings_options);
        this.itemIcons = context.getResources().getStringArray(R.array.settings_icons);
    }

    @Override
    public int getCount() {
        return itemNames.length;
    }

    @Override
    public String getItem(int position) {
        return itemNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class SettingsViewHolder {
        @Bind(R.id.settings_icon) TextView itemIcon;
        @Bind(R.id.settings_option) TextView itemName;

        SettingsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        SettingsViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.settings_cell, parent, false);
            holder = new SettingsViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SettingsViewHolder) view.getTag();
        }

        holder.itemName.setText(itemNames[position]);
        holder.itemIcon.setText(itemIcons[position]);

        return view;
    }
}
