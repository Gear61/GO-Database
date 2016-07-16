package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class StateServer {
    private static StateServer instance;
    private static List<String> statesList = new ArrayList<>();

    private StateServer() {
        statesList = FileUtils.extractItems("states.txt");
    }

    public static StateServer get() {
        if (instance == null) {
            instance = new StateServer();
        }
        return instance;
    }

    public boolean isValidState(String input) {
        if (input.isEmpty()) {
            return false;
        } else {
            String cleanState = input.substring(0, 1).toUpperCase() + input.substring(1);
            return statesList.contains(cleanState);
        }
    }

    public List<String> getMatchingStates(String prefix) {
        return MatchingUtils.getMatchingItems(prefix, statesList);
    }
}