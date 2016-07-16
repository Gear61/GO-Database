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
    @Bind(R.id.state_input) AutoCompleteTextView stateInput;
    @Bind(R.id.city_input) EditText cityInput;
    @Bind(R.id.name_or_address) EditText nameInput;
    @Bind(R.id.frequency) EditText frequencyInput;

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
    public void afterTextChanged(Editable pokemonInput) {
        if (PokemonServer.get().isValidPokemon(pokemonInput.toString())) {
            UIUtils.hideKeyboard(this);
        }
    }

    @OnClick({R.id.clear_pokemon, R.id.clear_state, R.id.clear_city, R.id.clear_name})
    public void clearInput(View view) {
        switch (view.getId()) {
            case R.id.clear_pokemon:
                pokemonInput.setText("");
                break;
            case R.id.clear_state:
                stateInput.setText("");
                break;
            case R.id.clear_city:
                cityInput.setText("");
                break;
            case R.id.clear_name:
                nameInput.setText("");
                break;
        }
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
        String state = stateInput.getText().toString().trim();
        String city = stateInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();

        if (!PokemonServer.get().isValidPokemon(pokemonName)) {
            UIUtils.showSnackbar(parent, getString(R.string.invalid_pokemon));
        } else if (frequency.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.no_frequency));
        } else if (state.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.no_state));
        } else if (city.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.no_city));
        } else if (name.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.no_display_name));
        }
    }
}
