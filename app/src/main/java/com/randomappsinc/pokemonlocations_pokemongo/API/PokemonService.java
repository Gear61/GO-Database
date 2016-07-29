package com.randomappsinc.pokemonlocations_pokemongo.API;

import com.randomappsinc.pokemonlocations_pokemongo.API.Models.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.LocationsResult;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.SearchRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.SyncLocationsRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.VoteRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public interface PokemonService {
    @POST("findPokemon/")
    Call<LocationsResult> doSearch(@Body SearchRequest request);

    @POST("addPokemon/")
    Call<BlankResponse> addPokemon(@Body AddPokemonRequest request);

    @POST("voteLocation/")
    Call<BlankResponse> voteLocation(@Body VoteRequest request);

    @POST("locationInfo/")
    Call<LocationsResult> syncLocations(@Body SyncLocationsRequest request);

    @POST("nearby/")
    Call<LocationsResult> searchNearby(@Body SearchRequest request);
}
