package com.toto.pokemonapp.repository

import com.toto.pokemonapp.app.mainActivityScreens.model.PokemonDetailsResponseModel
import com.toto.pokemonapp.api.PokemonWebService
import com.toto.pokemonapp.app.mainActivityScreens.model.PokemonListResponseModel

class PokemonAppRepository(private val webService: PokemonWebService = PokemonWebService()) {

    suspend fun getPokemonList(offset: Int = 0, limit: Int = 20): PokemonListResponseModel = webService.getPokemonList(
        offset, limit)

    suspend fun getPokemonDetails(id: String): PokemonDetailsResponseModel = webService.getPokemonDetails(id)

    companion object {
        @Volatile
        private var instance: PokemonAppRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: PokemonAppRepository().also { instance = it }
        }

    }
}