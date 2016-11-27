package com.randomappsinc.pokemonlocations_pokemongo.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.JSONUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 11/2/16.
 */

public class PokemonFragment extends Fragment {
    public static final String POKEMON_KEY = "pokemon";

    public static PokemonFragment create(Pokemon pokemon) {
        PokemonFragment pokemonFragment = new PokemonFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(POKEMON_KEY, pokemon);
        pokemonFragment.setArguments(bundle);

        return pokemonFragment;
    }

    @Bind(R.id.pokemon_note) TextView pokemonNote;
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

    @Bind(R.id.max_cp_ranking) TextView maxCpRanking;
    @Bind(R.id.base_attack_ranking) TextView attackRanking;
    @Bind(R.id.base_defense_ranking) TextView defenseRanking;
    @Bind(R.id.base_stamina_ranking) TextView staminaRanking;
    @Bind(R.id.base_capture_rate_ranking) TextView captureRateRanking;
    @Bind(R.id.base_flee_rate_ranking) TextView fleeRateRanking;

    @Bind(R.id.egg_info) TextView eggInfo;

    @BindString(R.string.percentage) String percentage;
    @BindString(R.string.region_exclusive) String regionTemplate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.pokemon_info_card, container, false);
        ButterKnife.bind(this, rootView);

        Pokemon pokemon = getArguments().getParcelable(JSONUtils.POKEMON_KEY);

        String pokemonRegion = PokemonServer.get().isRegionExclusive(pokemon);
        if (PokemonServer.get().isUnreleased(pokemon.getName())) {
            pokemonNote.setText(R.string.unreleased_error);
        } else if (!pokemonRegion.isEmpty()){
            pokemonNote.setText(String.format(regionTemplate, pokemonRegion));
        } else  {
            pokemonNote.setVisibility(View.GONE);
        }

        if (PreferencesManager.get().areImagesEnabled()) {
            Picasso.with(getActivity())
                    .load(PokemonUtils.getPokemonIcon(pokemon.getId()))
                    .into(pokemonIcon);
        } else {
            pokemonIcon.setVisibility(View.GONE);
        }

        pokemonName.setText(pokemon.getName());

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

        maxCpRanking.setText(getRankText(pokemon.getMaxCpRanking()));
        attackRanking.setText(getRankText(pokemon.getAttackRanking()));
        defenseRanking.setText(getRankText(pokemon.getDefenseRanking()));
        staminaRanking.setText(getRankText(pokemon.getStaminaRanking()));
        captureRateRanking.setText(getRankText(pokemon.getCaptureRateRanking()));
        fleeRateRanking.setText(getRankText(pokemon.getFleeRateRanking()));

        eggInfo.setText(Html.fromHtml(PokemonUtils.getEggInfo(pokemon)));

        return rootView;
    }

    private String getRankText(int ranking) {
        return "#" + String.valueOf(ranking);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
