package com.toto.pokemonapp.AppUtils

fun getQueryParam(url: String, param: String): String? {
    val regex = "(\\?|&)$param=([^&]*)".toRegex()
    return regex.find(url)?.groupValues?.get(2)
}