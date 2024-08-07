package com.toto.pokemonapp.app.mainActivityScreens.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponseModel(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") val results: ArrayList<ResultsResponseModel>
)

data class ResultsResponseModel(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)