package com.randomappsinc.pokemonlocations_pokemongo.Utils;

/**
 * Created by alexanderchiou on 7/14/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

public class UIUtils {
    public static void showSnackbar(View parent, String message) {
        Context context = MyApplication.getAppContext();
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.app_red));
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void loadMenuIcon(Menu menu, int itemId, Icon icon) {
        menu.findItem(itemId).setIcon(
                new IconDrawable(MyApplication.getAppContext(), icon)
                        .colorRes(R.color.white)
                        .actionBarSize());
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static void addFavoriteSnackbar(final View parent, String message, final PokeLocation location) {
        final Context context = MyApplication.getAppContext();
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_INDEFINITE);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.app_red));
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.setAction(android.R.string.yes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.get().addOrUpdateLocation(location);
                RestClient.get().syncFavorites();
                showSnackbar(parent, context.getString(R.string.location_favorited));
            }
        });
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showSetCurrentSnackbar(final String location, final View parent, final BaseAdapter adapter) {
        final Context context = MyApplication.getAppContext();
        Snackbar snackbar = Snackbar.make(parent, context.getString(R.string.location_added), Snackbar.LENGTH_INDEFINITE);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.app_red));
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(android.R.string.yes, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.get().setCurrentLocation(location);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                showSnackbar(parent, context.getString(R.string.current_location_set));
            }
        });
        snackbar.show();
    }
}