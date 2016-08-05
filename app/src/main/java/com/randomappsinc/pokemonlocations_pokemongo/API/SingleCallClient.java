package com.randomappsinc.pokemonlocations_pokemongo.API;

import android.os.AsyncTask;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by alexanderchiou on 8/4/16.
 */
public class SingleCallClient<T> {
    private Call<T> currentCall;
    private Call<T> newCall;
    private Callback<T> callback;

    public synchronized void executeCall(Call<T> newCall, Callback<T> callback) {
        this.newCall = newCall;
        this.callback = callback;
        new CallTask().execute();
    }

    private class CallTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (currentCall != null) {
                currentCall.cancel();
            }
            try {
                currentCall = newCall;
                currentCall.enqueue(callback);
            } catch (IllegalStateException ignored) {}
            return null;
        }
    }
}
