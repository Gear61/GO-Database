package com.randomappsinc.pokemonlocations_pokemongo.Utils;

/**
 * Created by alexanderchiou on 8/30/16.
 */
public class FeedUtils {
    public static final int SECONDS_IN_A_MINUTE = 60;
    public static final int SECONDS_IN_AN_HOUR = SECONDS_IN_A_MINUTE * 60;
    public static final int SECONDS_IN_A_DAY = SECONDS_IN_AN_HOUR * 24;
    public static final int SECONDS_IN_A_MONTH = SECONDS_IN_A_DAY * 31;
    public static final int SECONDS_IN_A_YEAR = SECONDS_IN_A_MONTH * 365;

    // Given a UNIX time in the past, tells us in a human-readable format how far back it is
    // Examples: 35s, 7m, 18h, 4d, 5w, 8mo, 8y
    public static String humanizeUnixTime(long oldUnixTime) {
        long currentUnixTime = System.currentTimeMillis() / 1000L;
        long secondsElapsed = currentUnixTime - oldUnixTime;
        if (secondsElapsed < SECONDS_IN_A_MINUTE) {
            return secondsElapsed + "s";
        }

        // Need to figure out how many minutes have elapsed
        if (secondsElapsed < SECONDS_IN_AN_HOUR) {
            return (secondsElapsed / SECONDS_IN_A_MINUTE) + "m";
        }

        // Need to figure out how many hours have elapsed
        if (secondsElapsed < SECONDS_IN_A_DAY) {
            return (secondsElapsed / SECONDS_IN_AN_HOUR) + "h";
        }

        // Need to figure out how many days have elapsed
        if (secondsElapsed < SECONDS_IN_A_MONTH) {
            return (secondsElapsed / SECONDS_IN_A_DAY) + "d";
        }

        // Need to figure out how many months have elapsed
        if (secondsElapsed < SECONDS_IN_A_YEAR) {
            return (secondsElapsed / SECONDS_IN_A_MONTH) + "mo";
        }

        // Lastly, figure out how many years have elapsed
        return (secondsElapsed / SECONDS_IN_A_YEAR) + "y";
    }
}
