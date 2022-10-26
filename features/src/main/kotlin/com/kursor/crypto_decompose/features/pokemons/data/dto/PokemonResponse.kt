package com.kursor.crypto_decompose.features.pokemons.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.kursor.crypto_decompose.features.pokemons.domain.Pokemon
import com.kursor.crypto_decompose.features.pokemons.domain.PokemonId

@Serializable
class PokemonResponse(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
class PokemonWrapperResponse(
    @SerialName("pokemon") val pokemon: PokemonResponse
)

fun PokemonResponse.toDomain(): Pokemon {
    return Pokemon(
        id = PokemonId(parseId(url)),
        name = formatName(name)
    )
}