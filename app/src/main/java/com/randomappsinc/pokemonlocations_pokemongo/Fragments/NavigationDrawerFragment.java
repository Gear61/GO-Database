package com.randomappsinc.pokemonlocations_pokemongo.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.IconItemsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Adapters.TeamsAdapter;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.PreferencesManager;
import com.randomappsinc.pokemonlocations_pokemongo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 7/14/16.
 */
public class NavigationDrawerFragment extends Fragment {
    @Bind(R.id.nav_drawer_tabs) ListView mDrawerListView;
    @Bind(R.id.no_team) View noTeam;
    @Bind(R.id.team_icon) ImageView teamIcon;
    @Bind(R.id.username) TextView username;

    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private MaterialDialog setTeamDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout navDrawer = (LinearLayout) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.bind(this, navDrawer);
        mDrawerListView.setAdapter(new IconItemsAdapter(getActivity(), R.array.nav_drawer_tabs, R.array.nav_drawer_icons));

        int team = PreferencesManager.get().getTeam();
        if (team == -1) {
            noTeam.setVisibility(View.VISIBLE);
            teamIcon.setVisibility(View.GONE);
        } else {
            switch (team) {
                case 0:
                    teamIcon.setImageResource(R.drawable.mystic);
                    break;
                case 1:
                    teamIcon.setImageResource(R.drawable.valor);
                    break;
                case 2:
                    teamIcon.setImageResource(R.drawable.instinct);
                    break;
            }
        }
        username.setText(PreferencesManager.get().getUsername());

        setTeamDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.set_team)
                .customView(R.layout.team_dialog, false)
                .build();
        new TeamViewHolder(setTeamDialog.getCustomView());

        return navDrawer;
    }

    public class TeamViewHolder {
        @Bind(R.id.teams) ListView teamsList;

        private TeamsAdapter teamsAdapter;

        public TeamViewHolder(View view) {
            ButterKnife.bind(this, view);
            teamsAdapter = new TeamsAdapter(getActivity());
            teamsList.setAdapter(teamsAdapter);
        }

        @OnItemClick(R.id.teams)
        public void chooseTeam(int position) {
            PreferencesManager.get().setTeam(position);
            noTeam.setVisibility(View.GONE);
            teamIcon.setImageResource(teamsAdapter.getTeamIcon(position));
            teamIcon.setVisibility(View.VISIBLE);
            setTeamDialog.dismiss();
        }
    }

    @OnClick(R.id.icon_container)
    public void setTeam() {
        setTeamDialog.show();
    }

    @OnClick(R.id.username)
    public void setUsername() {
        String prefill = PreferencesManager.get().getUsername().equals(getString(R.string.hello_trainer))
                ? "" : PreferencesManager.get().getUsername();
        new MaterialDialog.Builder(getActivity())
                .title(R.string.set_username)
                .content(R.string.username_prompt)
                .input(getString(R.string.username), prefill, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, @NonNull CharSequence input) {
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(!input.toString().trim().isEmpty());
                    }
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.set)
                .negativeText(android.R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newUsername = dialog.getInputEditText().getText().toString();
                        PreferencesManager.get().setUsername(newUsername);
                        username.setText(newUsername);
                    }
                })
                .show();
    }

    @OnItemClick(R.id.nav_drawer_tabs)
    public void onItemClick(int position) {
        selectItem(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set up the drawer's list view with items and click listener
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                toolbar,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {};

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    private void selectItem(int position) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (NavigationDrawerCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }
}
