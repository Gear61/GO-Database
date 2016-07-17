package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
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
    @Bind(R.id.parent) View parent;
    @Bind(R.id.pokemon_input) AutoCompleteTextView pokemonInput;
    @Bind(R.id.frequency) EditText frequencyInput;
    @Bind(R.id.location_input) EditText locationInput;

    int currentFrequencyIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_listing);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentFrequencyIndex = -1;

        pokemonInput.setAdapter(new PokemonACAdapter(this, R.layout.pokemon_ac_item, new ArrayList<String>()));
    }

    @OnTextChanged(value = R.id.pokemon_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onPokemonChanged(Editable pokemonInput) {
        if (PokemonServer.get().isValidPokemon(pokemonInput.toString())) {
            UIUtils.hideKeyboard(this);
        }
    }

    @OnClick(R.id.clear_pokemon)
    public void clearInput() {
        pokemonInput.setText("");
    }

    @OnClick(R.id.frequency)
    public void showFrequencyOptions() {
        new MaterialDialog.Builder(this)
                .title(R.string.frequency_title)
                .items(R.array.frequency_options)
                .itemsCallbackSingleChoice(currentFrequencyIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        currentFrequencyIndex = which;
                        frequencyInput.setText(text);
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.no)
                .show();
    }

    @OnClick(R.id.add_pokemon_listing)
    public void addListing() {
        UIUtils.hideKeyboard(this);

        String pokemonName = pokemonInput.getText().toString();
        String frequency = frequencyInput.getText().toString();

        if (!PokemonServer.get().isValidPokemon(pokemonName)) {
            UIUtils.showSnackbar(parent, getString(R.string.invalid_pokemon));
        } else if (frequency.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.no_frequency));
        }
    }
}
