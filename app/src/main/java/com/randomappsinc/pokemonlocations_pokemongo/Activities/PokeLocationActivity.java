package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.pokemonlocations_pokemongo.API.ApiConstants;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.AddPokemonCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.EditRarityCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.SingleLocationCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.PokemonPosting;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.EditRarityRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.SyncLocationsRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.RestClient;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokemonAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Models.Pokemon;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.PokeFindingDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.Models.SavedLocationDO;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.LocationUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocationActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.like_icon) TextView likeIcon;
    @Bind(R.id.score) TextView score;
    @Bind(R.id.display_name) TextView displayName;
    @Bind(R.id.distance_container) View distanceContainer;
    @Bind(R.id.distance) TextView distanceAway;
    @Bind(R.id.address) TextView address;
    @Bind(R.id.score_report) TextView scoreReport;

    @Bind(R.id.likes_icon) TextView likesIcon;
    @Bind(R.id.likes_count) TextView likesCount;
    @Bind(R.id.dislikes_icon) TextView dislikesIcon;
    @Bind(R.id.dislikes_count) TextView dislikesCount;

    @Bind(R.id.common_pokemon) RecyclerView commonPokemon;
    @Bind(R.id.uncommon_pokemon) RecyclerView uncommonPokemon;
    @Bind(R.id.rare_pokemon) RecyclerView rarePokemon;
    @Bind(R.id.no_common_pokemon) View noCommonPokemon;
    @Bind(R.id.no_uncommon_pokemon) View noUncommonPokemon;
    @Bind(R.id.no_rare_pokemon) View noRarePokemon;

    @BindString(R.string.like_count) String likesTemplate;
    @BindString(R.string.dislike_count) String dislikesTemplate;
    @BindString(R.string.positive_score) String positiveScore;

    @BindColor(R.color.app_red) int red;
    @BindColor(R.color.gray) int gray;
    @BindColor(R.color.green) int green;
    @BindColor(R.color.transparent_red) int transparentRed;
    @BindColor(R.color.dark_gray) int darkGray;

    private String placeId;
    private PokeLocation place;
    private MaterialDialog progressDialog;
    private boolean notInitialLoad;
    private PokemonAdapter commonAdapter;
    private PokemonAdapter uncommonAdapter;
    private PokemonAdapter rareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poke_location);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new MaterialDialog.Builder(this)
                .progress(true, 0)
                .cancelable(false)
                .build();

        place = getIntent().getParcelableExtra(PokeLocation.KEY);
        if (place != null) {
            placeId = place.getPlaceId();
            loadLocationInfo(false);
        } else {
            parent.setVisibility(View.INVISIBLE);
            progressDialog.setContent(R.string.fetching_poke_location);
            progressDialog.show();
            placeId = getIntent().getStringExtra(ApiConstants.ID_KEY);
            fetchLocationInfo();
        }

        if (PreferencesManager.get().shouldShowLocationTut()) {
            PreferencesManager.get().turnOffLocationTut();
            showTutorial();
        }
    }

    // Makes an API call to fetch the info corresponding to the location the page needs to show
    private void fetchLocationInfo() {
        List<String> placeIds = new ArrayList<>();
        placeIds.add(placeId);
        SyncLocationsRequest request = new SyncLocationsRequest();
        request.setPlaceIds(placeIds);
        RestClient.get().getPokemonService()
                .syncLocations(request)
                .enqueue(new SingleLocationCallback());
    }

    private void loadLocationInfo(boolean revealPage) {
        displayName.setText(place.getDisplayName());
        address.setText(place.getAddress());

        LatLong currentLatLong;
        String currentLocation = PreferencesManager.get().getCurrentLocation();
        if (!currentLocation.equals(getString(R.string.automatic))) {
            SavedLocationDO savedLocation = DatabaseManager.get().getLocation(currentLocation);
            currentLatLong = new LatLong(savedLocation.getLatitude(), savedLocation.getLongitude());
        } else {
            currentLatLong = getIntent().getParcelableExtra(PreferencesManager.CURRENT_LOCATION_KEY);
        }

        if (currentLatLong != null) {
            LocationUtils.loadDistanceAway(place, currentLatLong, distanceAway);
        } else {
            distanceContainer.setVisibility(View.GONE);
        }

        loadScoreModule();

        commonAdapter = new PokemonAdapter(this);
        uncommonAdapter = new PokemonAdapter(this);
        rareAdapter = new PokemonAdapter(this);
        commonPokemon.setAdapter(commonAdapter);
        uncommonPokemon.setAdapter(uncommonAdapter);
        rarePokemon.setAdapter(rareAdapter);
        setGalleries();

        if (revealPage) {
            parent.setVisibility(View.VISIBLE);
        }
    }

    private void showTutorial() {
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        MaterialShowcaseView likeExplanation = new MaterialShowcaseView.Builder(this)
                .setMaskColour(transparentRed)
                .setTarget(likesIcon)
                .setDismissText(R.string.got_it)
                .setContentText(R.string.like_explanation)
                .withCircleShape()
                .build();

        MaterialShowcaseView dislikeExplanation = new MaterialShowcaseView.Builder(this)
                .setMaskColour(transparentRed)
                .setTarget(dislikesIcon)
                .setDismissText(R.string.got_it)
                .setContentText(R.string.dislike_explanation)
                .withCircleShape()
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {}

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        explainEasyVote();
                    }
                })
                .build();

        sequence.addSequenceItem(likeExplanation);
        sequence.addSequenceItem(dislikeExplanation);
        sequence.start();
    }

    private void explainEasyVote() {
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(R.string.fighting_fraud)
                .content(R.string.easy_vote_explanation)
                .positiveText(R.string.got_it)
                .show();
    }

    private void loadScoreModule() {
        LocationUtils.loadNetScore(likeIcon, score, place);
        LocationUtils.loadScoreReport(place, scoreReport);

        // Load voting module
        likesCount.setText(String.format(likesTemplate, place.getNumLikes()));
        dislikesCount.setText(String.format(dislikesTemplate, place.getNumDislikes()));

        int currentVote = DatabaseManager.get().getVote(place);

        switch (currentVote) {
            case 1:
                likesIcon.setText(R.string.liked_icon);
                likesIcon.setTextColor(green);
                likesCount.setTextColor(green);
                dislikesIcon.setText(R.string.dislike_icon);
                dislikesIcon.setTextColor(darkGray);
                dislikesCount.setTextColor(darkGray);
                break;
            case 0:
                likesIcon.setText(R.string.like_icon);
                likesIcon.setTextColor(darkGray);
                likesCount.setTextColor(darkGray);
                dislikesIcon.setText(R.string.dislike_icon);
                dislikesIcon.setTextColor(darkGray);
                dislikesCount.setTextColor(darkGray);
                break;
            case -1:
                likesIcon.setText(R.string.like_icon);
                likesIcon.setTextColor(darkGray);
                likesCount.setTextColor(darkGray);
                dislikesIcon.setText(R.string.disliked_icon);
                dislikesIcon.setTextColor(red);
                dislikesCount.setTextColor(red);
                break;
        }
    }

    @OnClick(R.id.likes_button)
    public void like() {
        DatabaseManager.get().processLike(place);
        loadScoreModule();
    }

    @OnClick(R.id.dislikes_button)
    public void dislike() {
        DatabaseManager.get().processDislike(place);
        loadScoreModule();
    }

    private void setGalleries() {
        if (!place.getCommonPokemon().isEmpty()) {
            commonAdapter.setPokemonList(place.getCommonPokemon());
            noCommonPokemon.setVisibility(View.GONE);
            commonPokemon.setVisibility(View.VISIBLE);
        } else {
            commonPokemon.setVisibility(View.GONE);
            noCommonPokemon.setVisibility(View.VISIBLE);
        }
        if (!place.getUncommonPokemon().isEmpty()) {
            uncommonAdapter.setPokemonList(place.getUncommonPokemon());
            noUncommonPokemon.setVisibility(View.GONE);
            uncommonPokemon.setVisibility(View.VISIBLE);
        } else {
            uncommonPokemon.setVisibility(View.GONE);
            noUncommonPokemon.setVisibility(View.VISIBLE);
        }
        if (!place.getRarePokemon().isEmpty()) {
            rareAdapter.setPokemonList(place.getRarePokemon());
            noRarePokemon.setVisibility(View.GONE);
            rarePokemon.setVisibility(View.VISIBLE);
        } else {
            rarePokemon.setVisibility(View.GONE);
            noRarePokemon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (notInitialLoad) {
            loadScoreModule();
            invalidateOptionsMenu();

            // Refresh location info if they're coming back from adding data
            fetchLocationInfo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        notInitialLoad = true;
    }

    @OnClick({R.id.add_common_pokemon, R.id.add_uncommon_pokemon, R.id.add_rare_pokemon})
    public void addPokemon(View view) {
        Intent intent = new Intent(this, AddListingActivity.class);
        intent.putExtra(AddListingActivity.LOCATION_KEY, place);
        switch (view.getId()) {
            case R.id.add_uncommon_pokemon:
                intent.putExtra(AddListingActivity.FREQUENCY_INDEX_KEY, 1);
                break;
            case R.id.add_rare_pokemon:
                intent.putExtra(AddListingActivity.FREQUENCY_INDEX_KEY, 2);
                break;
        }
        startActivity(intent);
    }

    public PokeLocation getPlace() {
        return place;
    }

    public void submitPokeFinding(Pokemon pokemon, int frequencyIndex) {
        progressDialog.setContent(R.string.submitting_finding);
        progressDialog.show();
        float frequencyScore = PokemonUtils.getFrequencyFromIndex(frequencyIndex);
        AddPokemonRequest addPokemonRequest = new AddPokemonRequest();
        addPokemonRequest.setLocation(place);

        List<PokemonPosting> postings = new ArrayList<>();
        PokemonPosting posting = new PokemonPosting();
        posting.setPokemonId(pokemon.getId());
        posting.setRarity(frequencyScore);
        postings.add(posting);
        addPokemonRequest.setPostings(postings);

        RestClient.get().getPokemonService()
                .addPokemon(addPokemonRequest)
                .enqueue(new AddPokemonCallback(place, postings));
    }

    public void editPokeFinding(PokeFindingDO findingDO, float newFrequency) {
        progressDialog.setContent(R.string.editing_entry);
        progressDialog.show();

        float oldScore = PokemonUtils.getFrequencyScoreFromText(findingDO.getFrequency());
        EditRarityRequest request = new EditRarityRequest();
        request.setPokemonId(findingDO.getPokemonId());
        request.setPlaceId(findingDO.getPlaceId());
        request.setDelta(newFrequency - oldScore);
        RestClient.get().getPokemonService()
                .editRarity(request)
                .enqueue(new EditRarityCallback(findingDO, newFrequency));
    }

    @OnClick(R.id.start_navigation)
    public void startHeadingHere() {
            String mapUri = "google.navigation:q=" + place.getDisplayName() + ", " + place.getAddress();
            startActivity(Intent.createChooser(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
    }

    @Subscribe
    public void onEvent(String event) {
        switch (event) {
            case AddPokemonCallback.ADD_POKEMON_SUCCESS:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.share_pokemon_success));
                break;
            case AddPokemonCallback.ADD_POKEMON_FAILURE:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.add_pokemon_fail));
                break;
            case EditRarityCallback.EDIT_RARITY_SUCCESS:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.edit_entry_success));
                break;
            case EditRarityCallback.EDIT_RARITY_FAILURE:
                progressDialog.dismiss();
                UIUtils.showSnackbar(parent, getString(R.string.edit_entry_failure));
        }
    }

    @Subscribe
    public void onEvent(PokeLocation updatedLocation) {
        if (placeId.equals(updatedLocation.getPlaceId())) {
            place = updatedLocation;
            loadLocationInfo(progressDialog.isShowing());
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(PokeLocation.KEY, place);
        setResult(RESULT_OK, intent);
        EventBus.getDefault().unregister(this);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_menu, menu);
        if (DatabaseManager.get().isLocationFavorited(place)) {
            menu.findItem(R.id.favorite_location).setTitle(R.string.unfavorite_location);
            UIUtils.loadMenuIcon(menu, R.id.favorite_location, FontAwesomeIcons.fa_heart);
        } else {
            UIUtils.loadMenuIcon(menu, R.id.favorite_location, FontAwesomeIcons.fa_heart_o);
        }
        UIUtils.loadMenuIcon(menu, R.id.share_location, IoniconsIcons.ion_android_share_alt);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite_location:
                if (DatabaseManager.get().isLocationFavorited(place)) {
                    DatabaseManager.get().unfavoriteLocation(place);
                } else {
                    DatabaseManager.get().addOrUpdateLocation(place);
                    loadScoreModule();
                }
                invalidateOptionsMenu();
                return true;
            case R.id.share_location:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, place.getShareText());
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
