package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.StatusCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.StatusRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Fragments.NavigationDrawerFragment;
import com.randomappsinc.pokemonlocations_pokemongo.Fragments.SearchFragment;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Filter;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.MyApplication;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.add_pokemon_listing) FloatingActionButton addListing;
    @BindColor(R.color.transparent_red) int transparentRed;

    private NavigationDrawerFragment navDrawerFragment;
    private String lastSearchedLocation;
    private Filter filter;
    private SearchFragment searchFragment;
    private MaterialDialog processingLocation;
    private String givenFirstLocation;
    private MaterialDialog firstTimeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kill activity if it's not on top of the stack due to Samsung bug
        if (!isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        givenFirstLocation = "";
        filter = new Filter();
        addListing.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_ios_bookmarks).colorRes(R.color.white));

        navDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navDrawerFragment.setUp(R.id.navigation_drawer, drawerLayout);

        FragmentManager fragmentManager = getFragmentManager();
        searchFragment = new SearchFragment();
        fragmentManager.beginTransaction().replace(R.id.container, searchFragment).commit();

        if (PreferencesManager.get().shouldShowShareTutorial()) {
            showTutorial();
        } else if (PreferencesManager.get().shouldAskToRate()) {
            showPleaseRateDialog();
        }

        processingLocation = new MaterialDialog.Builder(this)
                .content(R.string.processing_location)
                .progress(true, 0)
                .cancelable(false)
                .build();

        firstTimeLocation = new MaterialDialog.Builder(this)
                .title(R.string.set_current_location)
                .content(R.string.looking_where)
                .cancelable(false)
                .input(getString(R.string.location), givenFirstLocation, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        String locationInput = input.toString().trim();
                        boolean submitEnabled = !(locationInput.isEmpty()
                                || DatabaseManager.get().alreadyHasLocation(locationInput));
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(submitEnabled);
                    }
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.set)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        givenFirstLocation = dialog.getInputEditText().getText().toString();
                        processLocation(givenFirstLocation);
                    }
                })
                .build();
    }

    public Filter getFilter() {
        return filter;
    }

    public void showTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        MaterialShowcaseView addListExplanation = new MaterialShowcaseView.Builder(this)
                .setMaskColour(transparentRed)
                .setTarget(addListing)
                .setDismissText(R.string.got_it)
                .setContentText(R.string.sharing_instructions)
                .withCircleShape()
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {}

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        setLocation();
                    }
                })
                .build();
        sequence.addSequenceItem(addListExplanation);
        sequence.start();
    }

    private void setLocation() {
        firstTimeLocation.show();
    }

    public void processLocation(final String locationName) {
        processingLocation.show();
        SmartLocation.with(this).geocoding()
                .direct(locationName, new OnGeocodingListener() {
                    @Override
                    public void onLocationResolved(String name, List<LocationAddress> results) {
                        if (!results.isEmpty()) {
                            Location location = results.get(0).getLocation();
                            SavedLocationDO locationDO = new SavedLocationDO();
                            locationDO.setDisplayName(locationName);
                            locationDO.setLatitude(location.getLatitude());
                            locationDO.setLongitude(location.getLongitude());
                            DatabaseManager.get().addMyLocation(locationDO);
                            PreferencesManager.get().setCurrentLocation(locationDO.getDisplayName());
                            processingLocation.dismiss();
                            searchFragment.fullSearch();
                        } else {
                            processingLocation.dismiss();
                            firstTimeLocation.setContent(R.string.bad_first_location);
                            setLocation();
                        }
                    }
                });
    }

    public void setLastSearched(String address) {
        lastSearchedLocation = address;
    }

    @OnClick(R.id.add_pokemon_listing)
    public void addPokemonListing() {
        navDrawerFragment.closeDrawer();
        startActivity(new Intent(this, AddListingActivity.class));
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        UIUtils.hideKeyboard(this);
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, FavoritesActivity.class);
                break;
            case 1:
                intent = new Intent(this, JournalActivity.class);
                break;
            case 2:
                intent = new Intent(this, MyLocationsActivity.class);
                break;
            case 3:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }
        startActivity(intent);
    }

    public void showSnackbar(String message) {
        UIUtils.showSnackbar(parent, message);
    }

    private void showPleaseRateDialog() {
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .content(R.string.please_rate)
                .negativeText(R.string.no_im_good)
                .positiveText(R.string.will_rate)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                            startActivity(intent);
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            filter = data.getParcelableExtra(Filter.KEY);
            searchFragment.fullSearch();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusRequest request = new StatusRequest();
        request.setCurrentVersion(MyApplication.getVersionCode());
        RestClient.get().getPokemonService()
                .getStatus(request)
                .enqueue(new StatusCallback());
    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals(StatusCallback.UPDATE_NEEDED)) {
            Intent intent = new Intent(this, UpdateNeededActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.send_request, IoniconsIcons.ion_android_mail);
        UIUtils.loadMenuIcon(menu, R.id.share_app, IoniconsIcons.ion_android_share_alt);
        UIUtils.loadMenuIcon(menu, R.id.search, IoniconsIcons.ion_android_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_request:
                drawerLayout.closeDrawers();
                String uriText = "mailto:" + SettingsActivity.SUPPORT_EMAIL
                        + "?subject=" + Uri.encode(getString(R.string.pokemon_data_request))
                        + "&body=" + Uri.encode(getString(R.string.data_request_body));
                if (!PreferencesManager.get().getCurrentLocation().equals(getString(R.string.automatic))) {
                    uriText += Uri.encode(PreferencesManager.get().getCurrentLocation());
                } else if (lastSearchedLocation != null) {
                    uriText += Uri.encode(lastSearchedLocation);
                }
                Uri mailUri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, mailUri);
                startActivity(Intent.createChooser(sendIntent, getString(R.string.send_email)));
                return true;
            case R.id.share_app:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_app_message));
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                return true;
            case R.id.search:
                drawerLayout.closeDrawers();
                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra(Filter.KEY, filter);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
