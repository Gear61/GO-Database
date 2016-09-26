package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.randomappsinc.pokemonlocations_pokemongo.Fragments.EggFragment;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;

/**
 * Created by alexanderchiou on 9/25/16.
 */

public class EggTabsAdapter extends FragmentStatePagerAdapter {
    public String[] eggTypes;

    public EggTabsAdapter (FragmentManager fragmentManager) {
        super(fragmentManager);
        eggTypes = MyApplication.getAppContext().getResources().getStringArray(R.array.egg_types);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                bundle.putInt(EggFragment.DISTANCE_KEY, 2);
                break;
            case 1:
                bundle.putInt(EggFragment.DISTANCE_KEY, 5);
                break;
            case 2:
                bundle.putInt(EggFragment.DISTANCE_KEY, 10);
                break;
        }

        EggFragment eggFragment = new EggFragment();
        eggFragment.setArguments(bundle);
        return eggFragment;
    }

    @Override
    public int getCount() {
        return eggTypes.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return eggTypes[position];
    }
}
