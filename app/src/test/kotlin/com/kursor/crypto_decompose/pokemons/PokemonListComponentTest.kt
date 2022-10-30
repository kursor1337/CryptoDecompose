package com.kursor.crypto_decompose.pokemons

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arkivanov.essenty.lifecycle.Lifecycle
import me.aartikov.replica.single.Loadable
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import com.kursor.crypto_decompose.core.error_handling.ServerException
import com.kursor.crypto_decompose.features.pokemons.createPokemonListComponent
import com.kursor.crypto_decompose.features.pokemons.ui.list.PokemonListComponent
import com.kursor.crypto_decompose.utils.*
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class PokemonListComponentTest {

    @Test
    fun `loads pokemons for the first tab on start`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakePokemons.firePokemonsJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actual = sut.pokemonsState
        )
    }

    @Test
    fun `redirects to details when a pokemon is clicked`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val outputCaptor = OutputCaptor<PokemonListComponent.Output>()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext, outputCaptor)

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakePokemons.firePokemonsJson)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        sut.onPokemonClick(FakePokemons.detailedPonyta.id)

        assertEquals(
            expected = listOf(PokemonListComponent.Output.PokemonDetailsRequested(FakePokemons.detailedPonyta.id)),
            actual = outputCaptor.outputs
        )
    }

    @Test
    fun `shows fullscreen error when pokemons loading failed`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }

        assertTrue(sut.pokemonsState.error?.exception is ServerException)
    }

    @Test
    fun `update pokemons when retry is clicked after failed loading`() = withTestKoin { koin ->
        val fakeWebServer = koin.fakeWebServer
        val componentContext = TestComponentContext()
        val sut = koin.componentFactory.createPokemonListComponent(componentContext) {}

        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakeResponse.BadRequest)
        componentContext.moveToState(Lifecycle.State.RESUMED)
        awaitUntil { !sut.pokemonsState.loading }
        fakeWebServer.prepare(HttpMethod.Get, "/api/v2/type/10", FakePokemons.firePokemonsJson)
        sut.onRetryClick()
        awaitUntil { !sut.pokemonsState.loading }

        assertEquals(
            expected = Loadable(loading = false, data = FakePokemons.firePokemons, error = null),
            actual = sut.pokemonsState
        )
    }
}