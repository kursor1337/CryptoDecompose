package com.kursor.crypto_decompose.features.pokemons.ui

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import kotlinx.parcelize.Parcelize
import com.kursor.crypto_decompose.core.ComponentFactory
import com.kursor.crypto_decompose.core.utils.toComposeState
import com.kursor.crypto_decompose.features.pokemons.createPokemonDetailsComponent
import com.kursor.crypto_decompose.features.pokemons.createPokemonListComponent
import com.kursor.crypto_decompose.features.pokemons.domain.PokemonId
import com.kursor.crypto_decompose.features.pokemons.ui.list.PokemonListComponent

class RealPokemonsComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, PokemonsComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: ChildStack<*, PokemonsComponent.Child> by childStack(
        source = navigation,
        initialConfiguration = ChildConfig.List,
        handleBackButton = true,
        childFactory = ::createChild
    ).toComposeState(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): PokemonsComponent.Child = when (config) {
        is ChildConfig.List -> {
            PokemonsComponent.Child.List(
                componentFactory.createPokemonListComponent(
                    componentContext,
                    ::onPokemonListOutput
                )
            )
        }

        is ChildConfig.Details -> {
            PokemonsComponent.Child.Details(
                componentFactory.createPokemonDetailsComponent(
                    componentContext,
                    config.pokemonId
                )
            )
        }
    }

    private fun onPokemonListOutput(output: PokemonListComponent.Output) {
        when (output) {
            is PokemonListComponent.Output.PokemonDetailsRequested -> {
                navigation.push(ChildConfig.Details(output.pokemonId))
            }
        }
    }

    private sealed interface ChildConfig : Parcelable {

        @Parcelize
        object List : ChildConfig

        @Parcelize
        data class Details(val pokemonId: PokemonId) : ChildConfig
    }
}