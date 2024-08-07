package com.toto.pokemonapp.api

import com.toto.pokemonapp.AppUtils.APIEndpoints
import com.toto.pokemonapp.app.mainActivityScreens.model.PokemonDetailsResponseModel
import com.toto.pokemonapp.app.mainActivityScreens.model.PokemonListResponseModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class PokemonWebService {
    private lateinit var api: PokemonApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PokemonApi::class.java)
    }

    suspend fun getPokemonList(offset: Int = 0, limit: Int = 20): PokemonListResponseModel = api.getPokemonList(offset = offset, limit = limit)

    suspend fun getPokemonDetails(id: String): PokemonDetailsResponseModel = api.getPokemonDetails(id)

}

interface PokemonApi {
    @GET(APIEndpoints.POKEMON)
    suspend fun getPokemonList(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): PokemonListResponseModel

    @GET("${APIEndpoints.POKEMON}/{id}")
    suspend fun getPokemonDetails(@Path("id") id: String): PokemonDetailsResponseModel

}