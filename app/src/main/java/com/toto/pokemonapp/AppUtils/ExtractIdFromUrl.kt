package com.toto.pokemonapp.AppUtils

fun extractIdFromUrl(url: String): String? {
    val regex = """.*/(\d+)/?$""".toRegex()
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)
}