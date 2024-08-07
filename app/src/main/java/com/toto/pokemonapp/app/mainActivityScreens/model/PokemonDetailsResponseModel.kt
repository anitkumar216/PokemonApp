package com.toto.pokemonapp.app.mainActivityScreens.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailsResponseModel(
    //val abilities: List<Ability>,
    @SerializedName("base_experience") val baseExperience: Int?,
    //val cries: Cries,
    //val forms: List<Form>,
    //val game_indices: List<GameIndice>,
    //val height: Int,
    //val held_items: List<Any>,
    //val id: Int,
    //val is_default: Boolean,
    //val location_area_encounters: String,
    //val moves: List<Move>,
    @SerializedName("name") val name: String?,
    //val order: Int,
    //val past_abilities: List<Any>,
    //val past_types: List<Any>,
    //val species: Species,
    @SerializedName("sprites") val sprites: Sprites?,
    @SerializedName("stats") val stats: ArrayList<Stats>?,
    @SerializedName("types") val types: ArrayList<Types>?,
    //val weight: Int
)

data class Sprites(
    @SerializedName("back_default") val backDefault: String?
)

data class Stats(
    @SerializedName("base_stat") val baseStat: Int?,
    @SerializedName("effort") val effort: Int?,
    @SerializedName("stat") val stat: StatType?
)

data class Types(
    @SerializedName("slot") val slot: Int?,
    @SerializedName("type") val type: StatType?
)

data class StatType(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?
)