package com.toto.pokemonapp.app.mainActivityScreens.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toto.pokemonapp.app.mainActivityScreens.model.PokemonDetailsResponseModel
import com.toto.pokemonapp.repository.PokemonAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val repository: PokemonAppRepository = PokemonAppRepository.getInstance()

    init {
        val id = savedStateHandle.get<String>("id") ?: "1"
        viewModelScope.launch(Dispatchers.IO) {
            pokemonState.value = getPokemonDetails(id)
        }
    }

    var pokemonState = mutableStateOf<PokemonDetailsResponseModel?>(null)

    private suspend fun getPokemonDetails(id: String): PokemonDetailsResponseModel = repository.getPokemonDetails(id)
}