package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.ArtistsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.ArtistInfoServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 7/24/16.
 */
public class CreditsActivity extends StandardActivity {
    @Bind(R.id.artists) ListView artists;

    private ArtistsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sprite_credits);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ArtistsAdapter(this);
        artists.setAdapter(adapter);
    }

    @OnItemClick(R.id.artists)
    public void artistChosen(final int position) {
        int[] pokemonIds = ArtistInfoServer.artistPokemonPairings[position];
        StringBuilder pokemonList = new StringBuilder();
        for (int i = 0; i < pokemonIds.length; i++) {
            if (i != 0) {
                pokemonList.append("\n");
            }
            pokemonList.append(PokemonServer.get().getPokemonName(pokemonIds[i]));
        }
        String artistName = adapter.getItem(position);
        new MaterialDialog.Builder(this)
                .title(artistName)
                .content(pokemonList)
                .positiveText(R.string.view_artist_profile)
                .negativeText(R.string.close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String profileUrl = ArtistInfoServer.artistPages[position];
                        openUrl(profileUrl);
                    }
                })
                .show();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
