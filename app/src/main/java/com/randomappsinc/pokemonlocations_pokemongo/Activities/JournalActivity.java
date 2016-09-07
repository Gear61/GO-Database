package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.EditRarityCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.EditRarityRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.JournalAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class JournalActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.journal) ListView journal;
    @Bind(R.id.no_content) TextView noContent;

    @BindString(R.string.add_finding_question) String setRarityQuestion;

    private JournalAdapter adapter;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(this);

        adapter = new JournalAdapter(this, noContent);
        journal.setAdapter(adapter);

        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.editing_entry)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    @OnItemClick(R.id.journal)
    public void onEntryClick(final int position) {
        String pokemonName = PokemonServer.get().getPokemonName(adapter.getItem(position).getPokemonId());
        String placeName = adapter.getItem(position).getLocationName();
        new MaterialDialog.Builder(this)
                .title(String.format(getString(R.string.entry_options_title), pokemonName, placeName))
                .items(R.array.journal_options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                showEditDialog(position);
                                break;
                            case 1:
                                openEntryLocation(position);
                                break;
                        }
                    }
                })
                .show();
    }

    private void showEditDialog(final int position) {
        String pokemonName = PokemonServer.get().getPokemonName(adapter.getItem(position).getPokemonId());
        String placeName = adapter.getItem(position).getLocationName();
        new MaterialDialog.Builder(this)
                .content(String.format(setRarityQuestion, pokemonName, placeName))
                .items(R.array.flag_frequency_options)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        editRarity(position, PokemonUtils.getFrequency(which));
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.no)
                .show();
    }

    private void editRarity(int position, float newFrequency) {
        progressDialog.show();

        PokeFindingDO pokeFindingDO = adapter.getItem(position);
        float oldScore = PokemonUtils.getFrequencyScore(pokeFindingDO.getFrequency());
        float delta = newFrequency - oldScore;
        EditRarityRequest request = new EditRarityRequest();
        request.setPokemonId(pokeFindingDO.getPokemonId());
        request.setPlaceId(pokeFindingDO.getPlaceId());
        request.setDelta(delta);
        RestClient.get().getPokemonService()
                .editRarity(request)
                .enqueue(new EditRarityCallback(pokeFindingDO, newFrequency));
    }

    private void openEntryLocation(int position) {
        String placeId = adapter.getItem(position).getPlaceId();
        PokeLocation pokeLocation = DatabaseManager.get().getFavorite(placeId);
        if (pokeLocation != null) {
            Intent intent = new Intent(this, PokeLocationActivity.class);
            intent.putExtra(PokeLocation.KEY, pokeLocation);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PokeLocationActivity.class);
            intent.putExtra(ApiConstants.ID_KEY, placeId);
            startActivity(intent);
        }
    }

    @Subscribe
    public void onEvent(String event) {
        switch (event) {
            case EditRarityCallback.EDIT_RARITY_SUCCESS:
                adapter.syncWithDb();
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.edit_entry_success));
                break;
            case EditRarityCallback.EDIT_RARITY_FAILURE:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.edit_entry_failure));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
