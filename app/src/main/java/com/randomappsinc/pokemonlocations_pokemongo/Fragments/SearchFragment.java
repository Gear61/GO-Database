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
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.NearbyRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.SearchRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.MainActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Activities.PokeLocationActivity;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonACAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.SearchAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
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
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by alexanderchiou on 7/14/16.
 */
public class SearchFragment extends Fragment {
    @Bind(R.id.search_input) AutoCompleteTextView searchInput;
    @Bind(R.id.search) View searchButton;
    @Bind(R.id.search_results) ListView searchResults;
    @Bind(R.id.no_results) TextView noResults;
    @Bind(R.id.search_icon) ImageView searchIcon;
    @BindColor(R.color.transparent_red) int transparentRed;

    private MaterialDialog progressDialog;
    private boolean locationFetched;
    private Handler locationChecker;
    private Runnable locationCheckTask;
    private SearchAdapter adapter;
    private int pokemonId;
    private LatLong searchedLocation;

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
        searchedLocation = new LatLong();

        if (PreferencesManager.get().shouldShowShareTutorial()) {
            showTutorial();
        }

        return rootView;
    }

    public void showTutorial() {
        MainActivity mainActivity = (MainActivity) getActivity();

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(mainActivity);
        MaterialShowcaseView searchExplanation = new MaterialShowcaseView.Builder(mainActivity)
                .setMaskColour(transparentRed)
                .setTarget(searchButton)
                .setDismissText(R.string.got_it)
                .setContentText(R.string.search_instructions)
                .setShapePadding(UIUtils.getDpInPixels(15))
                .build();
        sequence.addSequenceItem(searchExplanation);

        MaterialShowcaseView addListExplanation = new MaterialShowcaseView.Builder(mainActivity)
                .setMaskColour(transparentRed)
                .setTarget(mainActivity.getAddListing())
                .setDismissText(R.string.got_it)
                .setContentText(R.string.sharing_instructions)
                .withCircleShape()
                .build();
        sequence.addSequenceItem(addListExplanation);
        sequence.start();
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

    private void doAutomaticSearch() {
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
                            doSearch(location.getLatitude(), location.getLongitude());
                        }
                    });
            locationChecker.postDelayed(locationCheckTask, 10000L);
        } else {
            showLocationServicesDialog();
        }
    }

    private void doSearch(double latitude, double longitude) {
        searchedLocation.setLatitude(latitude);
        searchedLocation.setLongitude(longitude);

        progressDialog.setContent(R.string.finding_pokemon);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        double range = mainActivity.getRange();

        String pokemonName = searchInput.getText().toString();
        if (PokemonServer.get().isValidPokemon(pokemonName)) {
            pokemonId = PokemonServer.get().getPokemonId(searchInput.getText().toString());
            SearchRequest request = new SearchRequest();
            request.setLocation(latitude, longitude);
            request.setPokemonId(pokemonId);
            request.setRange(range);
            RestClient.get().getPokemonService()
                    .doSearch(request)
                    .enqueue(new SearchCallback());
        } else {
            pokemonId = 0;
            NearbyRequest request = new NearbyRequest();
            request.setLocation(latitude, longitude);
            request.setRange(range);
            RestClient.get().getPokemonService()
                    .searchNearby(request)
                    .enqueue(new SearchCallback());
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
        progressDialog.dismiss();
        adapter.processResults(results, pokemonId, searchedLocation);
        searchResults.smoothScrollToPosition(0);
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals(SearchCallback.SEARCH_FAIL)) {
            progressDialog.dismiss();
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
