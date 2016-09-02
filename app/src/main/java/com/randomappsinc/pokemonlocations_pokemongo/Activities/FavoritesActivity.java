package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.FavoritesCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.FavoritesAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class FavoritesActivity extends StandardActivity {
    @Bind(R.id.content) ListView content;
    @Bind(R.id.no_content) TextView noContent;
    @Bind(R.id.search_bar) View searchBar;
    @Bind(R.id.search_input) EditText searchInput;
    @Bind(R.id.clear_search) View clearSearch;

    private FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(this);

        adapter = new FavoritesAdapter(this, noContent, searchBar);
        content.setAdapter(adapter);
    }

    @OnItemClick(R.id.content)
    public void onFavoriteClick(int position) {
        PokeLocation place = adapter.getItem(position);
        Intent intent = new Intent(this, PokeLocationActivity.class);
        intent.putExtra(PokeLocation.KEY, place);
        startActivity(intent);
    }

    @OnTextChanged(value = R.id.search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        adapter.setSearchTerm(input.toString().trim().toLowerCase());
        if (input.length() == 0) {
            clearSearch.setVisibility(View.GONE);
        } else {
            clearSearch.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.syncWithDb();
        RestClient.get().syncFavorites();
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals(FavoritesCallback.FAVORITES_UPDATED)) {
            adapter.syncWithDb();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
