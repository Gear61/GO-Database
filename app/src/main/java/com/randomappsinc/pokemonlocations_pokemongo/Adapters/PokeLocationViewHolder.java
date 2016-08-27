package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LatLong;
import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
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

/**
 * Created by alexanderchiou on 7/17/16.
 */
public class PokeLocationViewHolder {
    @Bind(R.id.display_name) TextView displayName;
    @Bind(R.id.distance) TextView distance;
    @Bind(R.id.preview_gallery) View previewGallery;
    @Bind({R.id.pokemon1, R.id.pokemon2, R.id.pokemon3, R.id.pokemon4,
           R.id.pokemon5, R.id.pokemon6}) List<ImageView> pokemonPreviews;
    @Bind(R.id.pokemon7) ImageView finalPreview;
    @Bind(R.id.overflow_number) TextView overflow;
    @Bind(R.id.like_icon) TextView likeIcon;
    @Bind(R.id.score) TextView score;

    @BindString(R.string.miles_away) String milesTemplate;
    @BindString(R.string.kilometers_away) String kilometersTemplate;
    @BindString(R.string.zero) String zero;
    @BindString(R.string.positive_score) String positiveScore;
    @BindString(R.string.negative_score) String negativeScore;

    @BindColor(R.color.gray) int gray;
    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.app_red) int red;
    @BindColor(R.color.green) int green;

    private Context context;

    public PokeLocationViewHolder(View view, Context context) {
        this.context = context;
        ButterKnife.bind(this, view);
    }

    public void loadItem(PokeLocation pokeLocation, int pokemonId, LatLong currentLocation) {
        displayName.setText(pokeLocation.getDisplayName());
        if (currentLocation == null) {
            distance.setVisibility(View.GONE);
        } else {
            LatLong pokeCoords = new LatLong(pokeLocation.getLatitude(), pokeLocation.getLongitude());
            double distanceValue = LocationUtils.getDistance(pokeCoords, currentLocation);
            if (PreferencesManager.get().getIsAmerican()) {
                distance.setText(String.format(milesTemplate, distanceValue));
            } else {
                distance.setText(String.format(kilometersTemplate, distanceValue));
            }
        }

        if (PreferencesManager.get().areImagesEnabled()) {
            previewGallery.setVisibility(View.VISIBLE);
            List<Integer> previewIds = pokeLocation.getPokemonPreviews(pokemonId);

            // Load as many Pokemon as possible into previews
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

            // If there's not enough Pokemon to load the final item, hide it
            if (previewIds.size() < PokeLocation.NUM_PREVIEWS) {
                finalPreview.setVisibility(View.GONE);
                overflow.setVisibility(View.GONE);
            } else {
                int extraPokemon = pokeLocation.getExtraPokemon();
                // If there is only 1 "extra" Pokemon, just show it
                if (extraPokemon == 1) {
                    overflow.setVisibility(View.GONE);
                    Picasso.with(context)
                            .load(PokemonUtils.getPokemonIcon(previewIds.get(PokeLocation.NUM_PREVIEWS - 1)))
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

        // Load score
        int locationScore = pokeLocation.getScore();
        if (locationScore == 0) {
            likeIcon.setText(R.string.like_icon);
            likeIcon.setTextColor(darkGray);
            score.setText(zero);
        } else if (locationScore > 0) {
            likeIcon.setText(R.string.liked_icon);
            likeIcon.setTextColor(green);
            score.setText(String.format(positiveScore, locationScore));
        } else {
            likeIcon.setText(R.string.disliked_icon);
            likeIcon.setTextColor(red);
            score.setText(String.format(negativeScore, locationScore));
        }
    }
}
