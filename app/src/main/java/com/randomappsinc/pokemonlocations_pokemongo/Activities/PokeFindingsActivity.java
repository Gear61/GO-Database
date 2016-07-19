package com.randomappsinc.pokemonlocations_pokemongo.Activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.randomappsinc.pokemonlocations_pokemongo.Adapters.PokeFindingsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class PokeFindingsActivity extends StandardActivity {
    @Bind(R.id.content) ListView content;
    @Bind(R.id.no_content) TextView noContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regular_listview);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noContent.setText(R.string.no_findings);
        content.setAdapter(new PokeFindingsAdapter(this, noContent));
    }
}
