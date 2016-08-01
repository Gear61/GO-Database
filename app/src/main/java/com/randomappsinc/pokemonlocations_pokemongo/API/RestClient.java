package com.randomappsinc.pokemonlocations_pokemongo.API;

import com.randomappsinc.pokemonlocations_pokemongo.API.Callbacks.FavoritesCallback;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.SyncLocationsRequest;
import com.randomappsinc.pokemonlocations_pokemongo.Persistence.DatabaseManager;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public class RestClient {
    private static RestClient restClient;

    public static RestClient get() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    private PokemonService pokemonService;

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pokemonService = retrofit.create(PokemonService.class);
    }

    public PokemonService getPokemonService() {
        return pokemonService;
    }

    public void syncFavorites() {
        List<String> favoriteIds = DatabaseManager.get().getFavoriteIds();
        if (!favoriteIds.isEmpty()) {
            SyncLocationsRequest request = new SyncLocationsRequest();
            request.setPlaceIds(favoriteIds);
            RestClient.get().getPokemonService()
                    .syncLocations(request)
                    .enqueue(new FavoritesCallback());
        }
    }
}
