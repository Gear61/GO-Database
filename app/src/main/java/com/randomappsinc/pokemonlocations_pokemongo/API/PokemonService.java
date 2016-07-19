package com.randomappsinc.pokemonlocations_pokemongo.API;

import com.randomappsinc.pokemonlocations_pokemongo.API.Models.AddPokemonRequest;
import com.randomappsinc.pokemonlocations_pokemongo.API.Models.SearchRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alexanderchiou on 7/15/16.
 */
public interface PokemonService {
    @POST("findPokemon/")
    Call<BlankResponse> doSearch(@Body SearchRequest request);

    @POST("addPokemon/")
    Call<BlankResponse> addPokemon(@Body AddPokemonRequest request);
}
