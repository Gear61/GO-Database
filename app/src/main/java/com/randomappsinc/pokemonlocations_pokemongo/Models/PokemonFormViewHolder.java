package com.randomappsinc.pokemonlocations_pokemongo.Models;

import android.content.Context;
import android.text.Editable;
import android.view.View;
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
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 7/22/16.
 */
public class PokemonFormViewHolder {
    @Bind(R.id.pokemon_input) AutoCompleteTextView pokemonInput;
    @Bind(R.id.frequency_input) Spinner frequencyChoice;
    @Bind(R.id.error) TextView error;
    @BindString(R.string.already_attached) String dupeTemplate;

    private View addButton;
    private Set<String> alreadyChosen;

    public PokemonFormViewHolder(Context context, View rootView, View addButton) {
        ButterKnife.bind(this, rootView);
        this.addButton = addButton;
        addButton.setEnabled(false);

        pokemonInput.setAdapter(new PokemonACAdapter(context, R.layout.pokemon_ac_item, new ArrayList<String>()));

        String[] frequencyOptions = context.getResources()
                .getStringArray(R.array.frequency_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, frequencyOptions);
        frequencyChoice.setAdapter(adapter);
    }

    public PokemonPosting getPosting() {
        PokemonPosting posting = new PokemonPosting();
        posting.setPokemonId(PokemonServer.get().getPokemonId(pokemonInput.getText().toString()));
        String choice = frequencyChoice.getSelectedItem().toString();
        posting.setRarity(PokemonUtils.getFrequency(choice));
        return posting;
    }

    public void setAlreadyChosen(Set<String> alreadyChosen) {
        this.alreadyChosen = alreadyChosen;
    }

    public void clearForm() {
        pokemonInput.setText("");
        frequencyChoice.setSelection(0);
        error.setVisibility(View.GONE);
    }

    @OnClick(R.id.clear_pokemon)
    public void clearPokemon() {
        pokemonInput.setText("");
    }

    @OnTextChanged(value = R.id.pokemon_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        String pokemonName = input.toString();
        if (PokemonServer.get().isValidPokemon(pokemonName)) {
            if (alreadyChosen.contains(pokemonName.toLowerCase())) {
                String cleanName = pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1);
                error.setText(String.format(dupeTemplate, cleanName));
                error.setVisibility(View.VISIBLE);
                return;
            } else {
                addButton.setEnabled(true);
                return;
            }
        }
        error.setVisibility(View.GONE);
        addButton.setEnabled(false);
    }
}
