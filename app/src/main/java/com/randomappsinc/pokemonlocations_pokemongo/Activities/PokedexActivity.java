package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokedexAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.JSONUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 9/12/16.
 */
public class PokedexActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.search_input) EditText searchInput;
    @Bind(R.id.clear_search) View clearSearch;
    @Bind(R.id.pokemon) ListView pokemonList;

    private PokedexAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedex);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PokedexAdapter(this);
        pokemonList.setAdapter(adapter);
    }

    @OnTextChanged(value = R.id.search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        adapter.filterPokemon(input.toString());
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

    @OnItemClick(R.id.pokemon)
    public void onPokemonClicked(int position) {
        UIUtils.hideKeyboard(this);
        Intent intent = new Intent(this, PokemonActivity.class);
        intent.putExtra(JSONUtils.POKEMON_KEY, adapter.getItem(position));
        startActivity(intent);
    }

    private void searchForPokemon(int pokemonId) {
        Intent intent = new Intent();
        intent.putExtra(Pokemon.ID_KEY, pokemonId);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addPokemonListing(int pokemonId) {
        Intent intent = new Intent(this, AddListingActivity.class);
        intent.putExtra(Pokemon.ID_KEY, pokemonId);
        startActivity(intent);
    }
}
