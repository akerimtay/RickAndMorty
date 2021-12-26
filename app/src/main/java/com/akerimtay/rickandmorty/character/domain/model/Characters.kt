package com.akerimtay.rickandmorty.character.domain.model

data class Characters(
    val info: Info,
    val results: List<Character>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}