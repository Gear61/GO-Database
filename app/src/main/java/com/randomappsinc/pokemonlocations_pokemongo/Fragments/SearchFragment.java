package com.randomappsinc.pokemonlocations_pokemongo.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.SearchCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.SearchRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.MainActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.PokeLocationActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonACAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.SearchAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PermissionUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonServer;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by alexanderchiou on 7/14/16.
 */
public class SearchFragment extends Fragment {
    @Bind(R.id.search_input) AutoCompleteTextView searchInput;
    @Bind(R.id.search_results) ListView searchResults;
    @Bind(R.id.no_results) TextView noResults;
    @Bind(R.id.search_icon) ImageView searchIcon;

    private MaterialDialog progressDialog;
    private boolean locationFetched;
    private Handler locationChecker;
    private Runnable locationCheckTask;
    private SearchAdapter adapter;
    private int pokemonId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);

        searchInput.setAdapter(new PokemonACAdapter(getActivity(), R.layout.pokemon_ac_item, new ArrayList<String>()));
        adapter = new SearchAdapter(getActivity(), noResults);
        searchResults.setAdapter(adapter);

        searchIcon.setImageDrawable(
                new IconDrawable(getActivity(), IoniconsIcons.ion_android_search)
                        .colorRes(R.color.white));

        locationChecker = new Handler();
        locationCheckTask = new Runnable() {
            @Override
            public void run() {
                SmartLocation.with(getActivity()).location().stop();
                if (!locationFetched) {
                    progressDialog.dismiss();
                    showSnackbar(getString(R.string.auto_location_fail));
                }
            }
        };
        progressDialog = new MaterialDialog.Builder(getActivity())
                .progress(true, 0)
                .cancelable(false)
                .build();

        return rootView;
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @OnEditorAction(R.id.search_input)
    public boolean onKeyPress(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            fullSearch();
            return true;
        }
        return false;
    }

    @OnClick(R.id.search)
    public void onSearchClick() {
        fullSearch();
    }

    private void fullSearch() {
        UIUtils.hideKeyboard(getActivity());
        if (PokemonServer.get().isValidPokemon(searchInput.getText().toString())) {
            if (PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                doSearch();
            } else {
                askForLocation();
            }
        } else {
            showSnackbar(getString(R.string.invalid_pokemon));
        }
    }

    private void askForLocation() {
        if (PreferencesManager.get().shouldShowLocationRationale()) {
            new MaterialDialog.Builder(getActivity())
                    .content(R.string.location_permission_reason)
                    .positiveText(android.R.string.yes)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestLocation();
                        }
                    })
                    .show();
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, 1);
    }

    private void doSearch() {
        if (SmartLocation.with(getActivity()).location().state().locationServicesEnabled()) {
            progressDialog.setContent(R.string.getting_your_location);
            progressDialog.show();
            locationFetched = false;
            SmartLocation.with(getActivity()).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            locationChecker.removeCallbacks(locationCheckTask);
                            locationFetched = true;
                            progressDialog.setContent(R.string.finding_pokemon);

                            pokemonId = PokemonServer.get().getPokemonId(searchInput.getText().toString());
                            SearchRequest request = new SearchRequest();
                            request.setPokemonId(pokemonId);
                            request.setLocation(location.getLatitude(), location.getLongitude());
                            RestClient.get().getPokemonService()
                                    .doSearch(request)
                                    .enqueue(new SearchCallback());
                        }
                    });
            locationChecker.postDelayed(locationCheckTask, 10000L);
        } else {
            showLocationServicesDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // If they give us access to their location, run the search, otherwise, pester them for permission
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doSearch();
        } else {
            requestLocation();
        }
    }

    private void showLocationServicesDialog() {
        new MaterialDialog.Builder(getActivity())
                .content(R.string.location_services_needed)
                .negativeText(android.R.string.cancel)
                .positiveText(android.R.string.yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .show();
    }

    private void showSnackbar(String message) {
        ((MainActivity) getActivity()).showSnackbar(message);
    }

    @OnItemClick(R.id.search_results)
    public void onResultClick(int position) {
        PokeLocation place = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), PokeLocationActivity.class);
        intent.putExtra(PokeLocation.KEY, place);
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(List<PokeLocation> results) {
        progressDialog.dismiss();
        adapter.processResults(results, pokemonId);
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals(SearchCallback.SEARCH_FAIL)) {
            progressDialog.dismiss();
            showSnackbar(getString(R.string.search_fail));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
