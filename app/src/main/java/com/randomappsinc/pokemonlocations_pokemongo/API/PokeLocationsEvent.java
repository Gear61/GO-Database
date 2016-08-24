package com.randomappsinc.pokemonlocations_pokemongo.API;

import com.randomappsinc.pokemonlocations_pokemongo.Models.PokeLocation;

import java.util.List;

/**
 * Created by alexanderchiou on 8/23/16.
 */
public class PokeLocationsEvent {
    private String screen;
    private List<PokeLocation> locations;

    public PokeLocationsEvent() {}

    public PokeLocationsEvent(String screen, List<PokeLocation> locations) {
        this.screen = screen;
        this.locations = locations;
    }

    public String getScreen() {
        return screen;
    }

    public List<PokeLocation> getLocations() {
        return locations;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public void setLocations(List<PokeLocation> locations) {
        this.locations = locations;
    }
}
