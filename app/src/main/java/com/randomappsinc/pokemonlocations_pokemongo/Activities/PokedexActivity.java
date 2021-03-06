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
    @Bind(R.id.search_container) View searchContainer;
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
        searchContainer.requestFocus();
        Intent intent = new Intent(this, PokemonActivity.class);
        Pokemon pokemon = adapter.getItem(position);
        intent.putExtra(PokemonActivity.CURRENT_POSITION_KEY, pokemon.getId() - 1);
        startActivity(intent);
    }
}
