package com.kursor.crypto_decompose.features.root.ui

import com.arkivanov.decompose.router.stack.ChildStack
import com.kursor.crypto_decompose.core.message.ui.MessageComponent
import com.kursor.crypto_decompose.features.crypto.ui.CryptoComponent
import com.kursor.crypto_decompose.features.pokemons.ui.PokemonsComponent

/**
 * A root of a Decompose component tree.
 *
 * Note: Try to minimize child count in RootComponent. It should operate by flows of screens rather than separate screens.
 */
interface RootComponent {

    val childStack: ChildStack<*, Child>

    val messageComponent: MessageComponent

    sealed interface Child {

        class Pokemons(val component: PokemonsComponent) : Child

        class Crypto(val component: CryptoComponent) : Child

    }
}