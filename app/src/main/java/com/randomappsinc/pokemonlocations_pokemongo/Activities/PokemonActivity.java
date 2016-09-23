package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
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

        Picasso.with(this)
                .load(PokemonUtils.getPokemonIcon(pokemon.getId()))
                .into(pokemonIcon);
        pokemonName.setText(pokemon.getName());
        maxCp.setText(String.valueOf(pokemon.getMaxCp()));
        captureRate.setText(String.format(percentage, pokemon.getBaseCaptureRate()));
        fleeRate.setText(String.format(percentage, pokemon.getBaseFleeRate()));
        attack.setText(String.valueOf(pokemon.getBaseAttack()));
        defense.setText(String.valueOf(pokemon.getBaseDefense()));
        stamina.setText(String.valueOf(pokemon.getBaseStamina()));
        candyToEvolve.setText(String.valueOf(pokemon.getCandyToEvolve()));
        cpGain.setText(String.valueOf(pokemon.getAvgCpGain()));
    }
}
