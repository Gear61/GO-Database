package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.FavoritesCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.SyncLocationsRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.FavoritesAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class FavoritesActivity extends StandardActivity {
    @Bind(R.id.content) ListView content;
    @Bind(R.id.no_content) TextView noContent;

    private FavoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regular_listview);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(this);

        noContent.setText(R.string.no_favorites);
        adapter = new FavoritesAdapter(this, noContent);
        content.setAdapter(adapter);
    }

    @OnItemClick(R.id.content)
    public void onFavoriteClick(int position) {
        PokeLocation place = adapter.getItem(position);
        Intent intent = new Intent(this, PokeLocationActivity.class);
        intent.putExtra(PokeLocation.KEY, place);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.syncWithDb();

        List<String> favoriteIds = DatabaseManager.get().getFavoriteIds();
        if (!favoriteIds.isEmpty()) {
            SyncLocationsRequest request = new SyncLocationsRequest();
            request.setPlaceIds(favoriteIds);
            RestClient.get().getPokemonService()
                    .syncLocations(request)
                    .enqueue(new FavoritesCallback());
        }
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
