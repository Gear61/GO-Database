package com.randomappsinc.pokemonlocations_pokemongo.Adapters;

import android.view.View;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

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
    @Bind(R.id.address) TextView address;

    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.app_red) int red;

    private PokeLocation place;

    public PokeLocationViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void loadItem(PokeLocation pokeLocation) {
        place = pokeLocation;
        loadLocation();
    }

    public void loadLocation() {
        score.setText(String.valueOf(place.getScore()));
        displayName.setText(place.getDisplayName());
        address.setText(place.getAddress());

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
        loadLocation();
    }

    @OnClick(R.id.downvote)
    public void downvote() {
        DatabaseManager.get().processDownvote(place);
        loadLocation();
    }
}
