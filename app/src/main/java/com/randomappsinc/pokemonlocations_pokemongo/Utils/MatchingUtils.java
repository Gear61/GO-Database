package com.randomappsinc.pokemonlocations_pokemongo.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class MatchingUtils {
    public static List<String> getMatchingItems(String prefix, List<String> items) {
        int indexOfMatch = MatchingUtils.binarySearch(prefix, items);
        if (indexOfMatch == -1) {
            return new ArrayList<>();
        } else {
            List<String> matchingItems = new ArrayList<>();
            matchingItems.add(items.get(indexOfMatch));

            // Grab everything from the left and right of the matching index that also matches
            String cleanPrefix = prefix.toLowerCase();
            for (int i = indexOfMatch - 1; i >= 0; i--) {
                String substring = getSubstring(items.get(i), prefix);
                if (substring.equals(cleanPrefix)) {
                    matchingItems.add(0, items.get(i));
                } else {
                    break;
                }
            }
            for (int i = indexOfMatch + 1; i < items.size(); i++) {
                String substring = getSubstring(items.get(i), prefix).toLowerCase();
                if (substring.equals(cleanPrefix)) {
                    matchingItems.add(items.get(i));
                } else {
                    break;
                }
            }
            return matchingItems;
        }
    }

    // Returns index of first word with given prefix (-1 if it's not found)
    public static int binarySearch(String prefix, List<String> items) {
        String cleanPrefix = prefix.toLowerCase();
        int lo = 0;
        int hi = items.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int compareToValue = cleanPrefix.compareTo(getSubstring(items.get(mid), cleanPrefix));
            if (compareToValue < 0) {
                hi = mid - 1;
            } else if (compareToValue > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // Get the first X characters of candidate, where X is the search input's length
    public static String getSubstring(String main, String prefix) {
        if (prefix.length() > main.length()) {
            return main.toLowerCase();
        }
        return main.substring(0, prefix.length()).toLowerCase();
    }
}
