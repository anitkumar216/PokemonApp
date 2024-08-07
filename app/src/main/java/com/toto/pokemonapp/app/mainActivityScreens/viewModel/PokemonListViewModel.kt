package com.toto.pokemonapp.app.mainActivityScreens.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toto.pokemonapp.AppUtils.getQueryParam
import com.toto.pokemonapp.repository.PokemonAppRepository
import com.toto.pokemonapp.app.mainActivityScreens.model.ResultsResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val repository: PokemonAppRepository =
        PokemonAppRepository.getInstance()
) : ViewModel() {

    /*init {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonListState.value = getPokemonList()
        }
    }

    val pokemonListState = mutableStateOf(emptyList<ResultsResponseModel>())

    private suspend fun getPokemonList(): List<ResultsResponseModel> = repository.getPokemonList().results
*/
    val pokemonListState = mutableStateOf(emptyList<ResultsResponseModel>())
    var nextUrl: String? = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=20"
    val isFetching = mutableStateOf(false)

    init {
        fetchPokemonList()
    }

    fun fetchPokemonList() {
        if (isFetching.value || nextUrl == null) return

        isFetching.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getPokemonList(
                    offset = getQueryParam(nextUrl!!, "offset")?.toInt() ?: 0,
                    limit = getQueryParam(nextUrl!!, "limit")?.toInt() ?: 20
                )
                response.let {
                    pokemonListState.value += it.results
                    nextUrl = it.next
                }
            } catch (e: Exception) {
                // Handle error (e.g., logging, user notification)
            } finally {
                isFetching.value = false
            }
        }
    }
}