package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.LocationUtils;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocationViewHolder {
    @Bind(R.id.score) TextView score;
    @Bind(R.id.upvote) TextView upvote;
    @Bind(R.id.downvote) TextView downvote;
    @Bind(R.id.display_name) TextView displayName;
    @Bind(R.id.distance) TextView distance;
    @Bind(R.id.preview_gallery) View previewGallery;
    @Bind({R.id.pokemon1, R.id.pokemon2, R.id.pokemon3, R.id.pokemon4,
           R.id.pokemon5, R.id.pokemon6}) List<ImageView> pokemonPreviews;
    @Bind(R.id.pokemon7) ImageView finalPreview;
    @Bind(R.id.overflow_number) TextView overflow;

    @BindString(R.string.miles_away) String milesTemplate;
    @BindString(R.string.kilometers_away) String kilometersTemplate;

    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.app_red) int red;

    private PokeLocation place;
    private Context context;

    public PokeLocationViewHolder(View view, Context context) {
        this.context = context;
        ButterKnife.bind(this, view);
    }

    public void loadItem(PokeLocation pokeLocation, int pokemonId, LatLong location) {
        place = pokeLocation;

        displayName.setText(place.getDisplayName());
        if (location == null) {
            distance.setVisibility(View.GONE);
        } else {
            double distanceValue = LocationUtils.getDistance(place.getLatitude(), place.getLongitude(),
                    location.getLatitude(), location.getLongitude());
            if (PreferencesManager.get().getIsAmerican()) {
                distance.setText(String.format(milesTemplate, distanceValue));
            } else {
                distance.setText(String.format(kilometersTemplate, distanceValue));
            }
        }

        if (PreferencesManager.get().areImagesEnabled()) {
            previewGallery.setVisibility(View.VISIBLE);
            List<Integer> previewIds = place.getPokemonPreviews(pokemonId);
            for (int i = 0; i < PokeLocation.NUM_PREVIEWS - 1; i++) {
                if (i >= previewIds.size()) {
                    pokemonPreviews.get(i).setVisibility(View.GONE);
                } else {
                    Picasso.with(context)
                            .load(PokemonUtils.getPokemonIcon(previewIds.get(i)))
                            .into(pokemonPreviews.get(i));
                    pokemonPreviews.get(i).setVisibility(View.VISIBLE);
                }
            }

            if (previewIds.size() < PokeLocation.NUM_PREVIEWS) {
                finalPreview.setVisibility(View.GONE);
                overflow.setVisibility(View.GONE);
            } else {
                int extraPokemon = place.getExtraPokemon();
                if (extraPokemon == 1) {
                    overflow.setVisibility(View.GONE);
                    Picasso.with(context)
                            .load(PokemonUtils.getPokemonIcon(previewIds.get(6)))
                            .into(finalPreview);
                    finalPreview.setVisibility(View.VISIBLE);
                } else {
                    finalPreview.setVisibility(View.GONE);
                    String overflowText = "+" + String.valueOf(extraPokemon);
                    overflow.setText(overflowText);
                    overflow.setVisibility(View.VISIBLE);
                }
            }
        } else {
            previewGallery.setVisibility(View.GONE);
        }

        loadScoreModule();
    }

    private void loadScoreModule() {
        score.setText(String.valueOf(place.getScore()));
        int currentVote = DatabaseManager.get().getVote(place);
        switch (currentVote) {
            case 1:
                upvote.setTextColor(red);
                downvote.setTextColor(darkGray);
                break;
            case 0:
                upvote.setTextColor(darkGray);
                downvote.setTextColor(darkGray);
                break;
            case -1:
                upvote.setTextColor(darkGray);
                downvote.setTextColor(red);
                break;
        }
    }

    @OnClick(R.id.upvote)
    public void upvote() {
        DatabaseManager.get().processUpvote(place);
        loadScoreModule();
    }

    @OnClick(R.id.downvote)
    public void downvote() {
        DatabaseManager.get().processDownvote(place);
        loadScoreModule();
    }
}
