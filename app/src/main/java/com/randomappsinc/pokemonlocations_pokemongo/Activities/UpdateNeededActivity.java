package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 8/19/16.
 */
public class UpdateNeededActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_needed);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        // No-op here to prevent them from escaping
    }

    @OnClick(R.id.take_to_play)
    public void takeToPlay() {
        Uri uri =  Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
