package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 9/25/16.
 */

public class NavDrawerAdapter extends BaseAdapter {
    public static final int ME_INDEX = 0;
    public static final int DIVIDER_INDEX = 3;
    public static final int TOOLS_INDEX = 4;

    private Context context;
    private String[] itemNames;
    private String[] itemIcons;

    public NavDrawerAdapter(Context context) {
        this.context = context;
        this.itemNames = context.getResources().getStringArray(R.array.nav_drawer_options);
        this.itemIcons = context.getResources().getStringArray(R.array.nav_drawer_icons);
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

    public class DrawerItemViewHolder {
        @Bind(R.id.nav_item_parent) View parent;
        @Bind(R.id.divider) View divider;
        @Bind(R.id.header) TextView header;
        @Bind(R.id.nav_item) View navItem;
        @Bind(R.id.drawer_icon) TextView itemIcon;
        @Bind(R.id.drawer_option) TextView itemName;

        @BindColor(R.color.white) int white;

        public DrawerItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadItem(int position) {
            if (position == DIVIDER_INDEX) {
                header.setVisibility(View.GONE);
                navItem.setVisibility(View.GONE);
                divider.setVisibility(View.VISIBLE);

                parent.setBackgroundColor(white);
            } else if (position == ME_INDEX || position == TOOLS_INDEX) {
                header.setText(getItem(position));

                divider.setVisibility(View.GONE);
                navItem.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);

                parent.setBackgroundColor(white);
            } else {
                itemIcon.setText(itemIcons[position]);
                itemName.setText(getItem(position));

                divider.setVisibility(View.GONE);
                header.setVisibility(View.GONE);
                navItem.setVisibility(View.VISIBLE);

                int[] attrs = new int[] { android.R.attr.selectableItemBackground };
                TypedArray typedArray = context.obtainStyledAttributes(attrs);
                Drawable drawableFromTheme = typedArray.getDrawable(0);
                typedArray.recycle();
                parent.setBackground(drawableFromTheme);
            }
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        DrawerItemViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.nav_drawer_cell, parent, false);
            holder = new DrawerItemViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (DrawerItemViewHolder) view.getTag();
        }
        holder.loadItem(position);
        return view;
    }
}
