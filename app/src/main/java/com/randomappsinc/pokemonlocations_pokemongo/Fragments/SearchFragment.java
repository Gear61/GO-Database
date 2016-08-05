package com.randomappsinc.pokemonlocations_pokemongo.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.SearchCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.NearbyRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.SearchRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.LocationsResult;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.API.SingleCallClient;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.MainActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.PokeLocationActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.SearchAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Filter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.LocationUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PermissionUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;

/**
 * Created by alexanderchiou on 7/14/16.
 */
public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.search_results) ListView searchResults;
    @Bind(R.id.no_results) TextView noResults;
    @Bind(R.id.loading_search) SwipeRefreshLayout loadingSearch;
    @BindColor(R.color.transparent_red) int transparentRed;

    private boolean locationFetched;
    private Handler locationChecker;
    private Runnable locationCheckTask;
    private SearchAdapter adapter;
    private int pokemonId;
    private LatLong searchedLocation;
    private SingleCallClient<LocationsResult> searchClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search, container, false);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);

        searchClient = new SingleCallClient<>();

        adapter = new SearchAdapter(getActivity(), noResults);
        searchResults.setAdapter(adapter);

        loadingSearch.setColorSchemeResources(R.color.app_red);
        loadingSearch.setOnRefreshListener(this);

        locationChecker = new Handler();
        locationCheckTask = new Runnable() {
            @Override
            public void run() {
                SmartLocation.with(getActivity()).location().stop();
                if (!locationFetched) {
                    loadingSearch.setRefreshing(false);
                    showSnackbar(getString(R.string.auto_location_fail));
                }
            }
        };

        searchedLocation = new LatLong();

        if (PreferencesManager.get().shouldShowWelcome()) {
            noResults.setText(R.string.welcome);
        } else {
            fullSearch();
        }

        return rootView;
    }

    @Override
    public void onRefresh() {
        fullSearch();
    }

    public void fullSearch() {
        noResults.setText(R.string.finding_pokemon);
        if (PreferencesManager.get().getCurrentLocation().equals(getString(R.string.automatic))) {
            if (PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                doAutomaticSearch();
            } else {
                askForLocation();
            }
        } else {
            String currentLocation = PreferencesManager.get().getCurrentLocation();
            SavedLocationDO locationDO = DatabaseManager.get().getLocation(currentLocation);
            doSearch(locationDO.getLatitude(), locationDO.getLongitude());
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

    public void doAutomaticSearch() {
        // Cancel previous location fetches
        SmartLocation.with(getActivity()).location().stop();
        locationChecker.removeCallbacks(locationCheckTask);

        loadingSearch.setRefreshing(true);
        if (SmartLocation.with(getActivity()).location().state().locationServicesEnabled()) {
            locationFetched = false;
            SmartLocation.with(getActivity()).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            MainActivity activity = (MainActivity) getActivity();
                            activity.setLastSearched(LocationUtils.getAddressFromLocation(location));

                            locationChecker.removeCallbacks(locationCheckTask);
                            locationFetched = true;
                            doSearch(location.getLatitude(), location.getLongitude());
                        }
                    });
            locationChecker.postDelayed(locationCheckTask, 10000L);
        } else {
            showLocationServicesDialog();
        }
    }

    public void doSearch(double latitude, double longitude) {
        if (!loadingSearch.isRefreshing()) {
            loadingSearch.setRefreshing(true);
        }
        searchedLocation.setLatitude(latitude);
        searchedLocation.setLongitude(longitude);

        MainActivity mainActivity = (MainActivity) getActivity();
        Filter filter = mainActivity.getFilter();
        pokemonId = filter.getPokemonId();

        if (pokemonId > 0) {
            SearchRequest request = new SearchRequest();
            request.setLocation(latitude, longitude);
            request.setPokemonId(pokemonId);
            request.setRange(LocationUtils.getRangeFromIndex(filter.getDistanceIndex()));
            Call<LocationsResult> call = RestClient.get().getPokemonService().doSearch(request);
            searchClient.executeCall(call, new SearchCallback());
        } else {
            NearbyRequest request = new NearbyRequest();
            request.setLocation(latitude, longitude);
            request.setRange(LocationUtils.getRangeFromIndex(filter.getDistanceIndex()));
            Call<LocationsResult> call = RestClient.get().getPokemonService().searchNearby(request);
            searchClient.executeCall(call, new SearchCallback());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // If they give us access to their location, run the search, otherwise, pester them for permission
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doAutomaticSearch();
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
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PokeLocation location = data.getParcelableExtra(PokeLocation.KEY);
            if (location != null) {
                adapter.updateLocation(location);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(List<PokeLocation> results) {
        loadingSearch.setRefreshing(false);
        adapter.processResults(results, pokemonId, searchedLocation);
        searchResults.smoothScrollToPosition(0);
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals(SearchCallback.SEARCH_FAIL)) {
            loadingSearch.setRefreshing(false);
            showSnackbar(getString(R.string.search_fail));
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        UIUtils.hideKeyboard(getActivity());
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
