package com.randomappsinc.pokemonlocations_pokemongo.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.randomappsinc.pokemonlocations_pokemongo.Activities.PokemonActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.EggsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.EggDO;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 9/25/16.
 */

public class EggFragment extends Fragment {
    public static final String DISTANCE_KEY = "distance";

    @Bind(R.id.egg_list) ListView eggList;

    private EggsAdapter eggsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.egg_list, container, false);
        ButterKnife.bind(this, rootView);

        int distance = getArguments().getInt(DISTANCE_KEY);
        eggsAdapter = new EggsAdapter(getActivity(), distance);
        eggList.setAdapter(eggsAdapter);

        return rootView;
    }

    @OnItemClick(R.id.egg_list)
    public void onPokemonClicked(int position) {
        EggDO eggDO = eggsAdapter.getItem(position);
        Pokemon pokemon = DatabaseManager.get().getPokemonDBManager().getPokemon(eggDO.getPokemonId());
        Intent intent = new Intent(getActivity(), PokemonActivity.class);
        intent.putExtra(PokemonActivity.CURRENT_POSITION_KEY, pokemon.getId() - 1);
        getActivity().startActivity(intent);
    }
}
