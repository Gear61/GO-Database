package com.randomappsinc.pokemonlocations_pokemongo.API;

import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.EditRarityRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.NearbyRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.SearchRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.StatusRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.SyncLocationsRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Requests.VoteRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.LocationsResult;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.Results.StatusInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public interface PokemonService {
    @POST("findPokemon/")
    Call<LocationsResult> doSearch(@Body SearchRequest request);

    @POST("findPokemon/")
    Call<LocationsResult> searchNearby(@Body NearbyRequest request);

    @POST("addPokemon/")
    Call<BlankResponse> addPokemon(@Body AddPokemonRequest request);

    @POST("voteLocation/")
    Call<BlankResponse> voteLocation(@Body VoteRequest request);

    @POST("locationInfo/")
    Call<LocationsResult> syncLocations(@Body SyncLocationsRequest request);

    @POST("status/")
    Call<StatusInfo> getStatus(@Body StatusRequest request);

    @POST("editRarity/")
    Call<BlankResponse> editRarity(@Body EditRarityRequest request);
}
