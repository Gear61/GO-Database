package com.randomappsinc.pokemonlocations_pokemongo.Models;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.API.Models.PokemonPosting;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonACAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 7/22/16.
 */
public class PokemonFormViewHolder {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.pokemon_input) AutoCompleteTextView pokemonInput;
    @Bind(R.id.clear_pokemon) View clearPokemon;
    @Bind(R.id.frequency_input) Spinner frequencyChoice;
    @Bind(R.id.error) TextView error;
    @BindString(R.string.already_attached) String dupeTemplate;

    private Context context;
    private View addButton;
    private Set<String> alreadyChosen;
    private boolean prefillMode;

    public PokemonFormViewHolder(Context context, View rootView, View addButton) {
        ButterKnife.bind(this, rootView);
        this.context = context;
        this.addButton = addButton;
        addButton.setEnabled(false);

        pokemonInput.setAdapter(new PokemonACAdapter(context, R.layout.pokemon_ac_item, new ArrayList<String>()));

        String[] frequencyOptions = context.getResources().getStringArray(R.array.frequency_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, frequencyOptions);
        frequencyChoice.setAdapter(adapter);
    }

    public void setFrequency(int index) {
        frequencyChoice.setSelection(index);
    }

    public PokemonPosting getPosting() {
        PokemonPosting posting = new PokemonPosting();
        posting.setPokemonId(PokemonServer.get().getPokemonId(pokemonInput.getText().toString()));
        String choice = frequencyChoice.getSelectedItem().toString();
        posting.setRarity(PokemonUtils.getFrequencyFromHeader(choice));
        return posting;
    }

    public void setAlreadyChosen(Set<String> alreadyChosen) {
        this.alreadyChosen = alreadyChosen;
    }

    public void setPrefillMode(boolean prefillMode) {
        this.prefillMode = prefillMode;
    }

    public void clearForm() {
        pokemonInput.setText("");
        frequencyChoice.setSelection(0);
        error.setVisibility(View.GONE);
    }

    public void requestFocus() {
        pokemonInput.requestFocus();
    }

    @OnClick(R.id.clear_pokemon)
    public void clearPokemon() {
        pokemonInput.setText("");
    }

    @OnTextChanged(value = R.id.pokemon_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        if (input.length() == 0) {
            clearPokemon.setVisibility(View.GONE);
        } else {
            clearPokemon.setVisibility(View.VISIBLE);
            if (PokemonServer.get().isValidPokemon(input.toString())) {
                // Hide keyboard
                InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(pokemonInput.getWindowToken(), 0);

                if (!prefillMode) {
                    frequencyChoice.requestFocus();
                    frequencyChoice.performClick();
                } else {
                    prefillMode = false;
                    parent.requestFocus();
                }
            }
        }
        verifyInputs();
    }

    @OnItemSelected(R.id.frequency_input)
    public void onFrequencyChosen() {
        verifyInputs();
    }

    public void verifyInputs() {
        String pokemonName = pokemonInput.getText().toString();
        boolean isCommon = frequencyChoice.getSelectedItemPosition() == 0;

        if (PokemonServer.get().isValidPokemon(pokemonName)) {
            if (alreadyChosen.contains(pokemonName.toLowerCase())) {
                String cleanName = pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1);
                error.setText(String.format(dupeTemplate, cleanName));
                error.setVisibility(View.VISIBLE);
                addButton.setEnabled(false);
            } else if (PokemonServer.get().isUnreleased(pokemonName)) {
                error.setText(R.string.unreleased_error);
                error.setVisibility(View.VISIBLE);
                addButton.setEnabled(false);
            } else if (isCommon && PokemonServer.get().cantBeCommon(pokemonName)) {
                error.setText(R.string.bogus_common);
                error.setVisibility(View.VISIBLE);
                addButton.setEnabled(false);
            } else {
                error.setVisibility(View.GONE);
                addButton.setEnabled(true);
            }
        } else {
            // If there's not even a valid Pokemon, just hide the error message and disable the button
            error.setVisibility(View.GONE);
            addButton.setEnabled(false);
        }
    }
}
