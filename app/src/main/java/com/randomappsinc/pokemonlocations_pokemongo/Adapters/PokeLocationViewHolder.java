package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;
import com.randomappsinc.pokemonlocations_pokemongo.Utils.PokemonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
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
    @Bind({R.id.pokemon1, R.id.pokemon2, R.id.pokemon3, R.id.pokemon4,
           R.id.pokemon5, R.id.pokemon6, R.id.pokemon7}) List<ImageView> pokemonPreviews;

    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.app_red) int red;

    private PokeLocation place;
    private Context context;

    public PokeLocationViewHolder(View view, Context context) {
        this.context = context;
        ButterKnife.bind(this, view);
    }

    public void loadItem(PokeLocation pokeLocation, int pokemonId) {
        place = pokeLocation;
        displayName.setText(place.getDisplayName());

        List<Integer> previewIds = pokeLocation.getPokemonPreviews(pokemonId);
        for (int i = 0; i < PokeLocation.NUM_PREVIEWS; i++) {
            if (i >= previewIds.size()) {
                pokemonPreviews.get(i).setVisibility(View.GONE);
            } else {
                Picasso.with(context)
                        .load(PokemonUtils.getPokemonIcon(previewIds.get(i)))
                        .into(pokemonPreviews.get(i));
                pokemonPreviews.get(i).setVisibility(View.VISIBLE);
            }
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
