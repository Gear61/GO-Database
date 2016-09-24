package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.JSONUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 9/22/16.
 */

public class PokemonActivity extends StandardActivity {
    @Bind(R.id.pokemon_icon) ImageView pokemonIcon;
    @Bind(R.id.pokemon_name) TextView pokemonName;
    @Bind(R.id.type1) TextView type1;
    @Bind(R.id.type2) TextView type2;
    @Bind(R.id.max_cp) TextView maxCp;
    @Bind(R.id.base_capture_rate) TextView captureRate;
    @Bind(R.id.base_flee_rate) TextView fleeRate;
    @Bind(R.id.base_attack) TextView attack;
    @Bind(R.id.base_defense) TextView defense;
    @Bind(R.id.base_stamina) TextView stamina;
    @Bind(R.id.candy_to_evolve) TextView candyToEvolve;
    @Bind(R.id.average_cp_gain) TextView cpGain;

    @BindString(R.string.percentage) String percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_view);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Pokemon pokemon = getIntent().getParcelableExtra(JSONUtils.POKEMON_KEY);
        setTitle(pokemon.getName());

        if (PreferencesManager.get().areImagesEnabled()) {
            Picasso.with(this)
                    .load(PokemonUtils.getPokemonIcon(pokemon.getId()))
                    .into(pokemonIcon);
            pokemonName.setText(pokemon.getName());
        } else {
            pokemonIcon.setVisibility(View.GONE);
        }

        PokemonUtils.setTypeBackground(type1, pokemon.getType1());
        type1.setText(pokemon.getType1());

        if (pokemon.getType2().isEmpty()) {
            type2.setVisibility(View.GONE);
        } else {
            PokemonUtils.setTypeBackground(type2, pokemon.getType2());
            type2.setText(pokemon.getType2());
        }

        maxCp.setText(String.valueOf(pokemon.getMaxCp()));
        captureRate.setText(String.format(percentage, pokemon.getBaseCaptureRate()));
        fleeRate.setText(String.format(percentage, pokemon.getBaseFleeRate()));
        attack.setText(String.valueOf(pokemon.getBaseAttack()));
        defense.setText(String.valueOf(pokemon.getBaseDefense()));
        stamina.setText(String.valueOf(pokemon.getBaseStamina()));

        int candyNeeded = pokemon.getCandyToEvolve();
        if (candyNeeded > 0) {
            candyToEvolve.setText(String.valueOf(pokemon.getCandyToEvolve()));
        } else {
            candyToEvolve.setText(R.string.not_applicable);
        }

        cpGain.setText(String.valueOf(pokemon.getAvgCpGain()));
    }
}
