package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.widget.AutoCompleteTextView;

import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonACAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class AddListingActivity extends StandardActivity {
    @Bind(R.id.pokemon_input) AutoCompleteTextView pokemonInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_listing);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pokemonInput.setAdapter(new PokemonACAdapter(this, R.layout.pokemon_ac_item, new ArrayList<String>()));
    }

    @OnTextChanged(value = R.id.pokemon_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable searchInput) {
        if (PokemonServer.get().isValidPokemon(searchInput.toString())) {
            UIUtils.hideKeyboard(this);
        }
    }

    @OnClick(R.id.clear_input)
    public void clearInput() {
        pokemonInput.setText("");
    }

    @OnClick(R.id.add_pokemon_listing)
    public void addListing() {

    }
}
