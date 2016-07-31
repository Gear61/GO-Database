package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.app.FragmentManager;
import android.content.Intent;
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
import com.randomappsinc.pokemonlocations_pokemongo.Fragments.NavigationDrawerFragment;
import com.randomappsinc.pokemonlocations_pokemongo.Fragments.SearchFragment;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.add_pokemon_listing) FloatingActionButton addListing;

    private NavigationDrawerFragment navDrawerFragment;

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

        addListing.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_ios_bookmarks).colorRes(R.color.white));

        navDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navDrawerFragment.setUp(R.id.navigation_drawer, drawerLayout);

        FragmentManager fragmentManager = getFragmentManager();
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction().replace(R.id.container, searchFragment).commit();

        if (PreferencesManager.get().shouldShowShareTutorial()) {
            showTutorial();
        } else if (PreferencesManager.get().shouldAskToRate()) {
            showPleaseRateDialog();
        }
    }

    public void showTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        MaterialShowcaseView addListExplanation = new MaterialShowcaseView.Builder(this)
                .setTarget(addListing)
                .setTitleText(R.string.welcome)
                .setDismissText(R.string.got_it)
                .setContentText(R.string.sharing_instructions)
                .withCircleShape()
                .build();
        sequence.addSequenceItem(addListExplanation);
        sequence.start();
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
                intent = new Intent(this, PokeFindingsActivity.class);
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

    private void chooseCurrentLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.choose_current_location)
                .content(R.string.current_instructions)
                .items(DatabaseManager.get().getLocationsArray())
                .itemsCallbackSingleChoice(DatabaseManager.get().getCurrentLocationIndex(),
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                PreferencesManager.get().setCurrentLocation(text.toString());
                                UIUtils.showSnackbar(parent, getString(R.string.current_location_set));
                                return true;
                            }
                        })
                .positiveText(R.string.choose)
                .negativeText(android.R.string.cancel)
                .neutralText(R.string.add_location_title)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addLocation();
                    }
                })
                .show();
    }

    private void addLocation() {
        new MaterialDialog.Builder(this)
                .title(R.string.add_location_title)
                .input(getString(R.string.location), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        String currentInput = input.toString().trim();
                    }
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.add)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newLocation = dialog.getInputEditText().getText().toString();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.set_current_location, IoniconsIcons.ion_android_map);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_current_location:
                drawerLayout.closeDrawers();
                chooseCurrentLocation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
