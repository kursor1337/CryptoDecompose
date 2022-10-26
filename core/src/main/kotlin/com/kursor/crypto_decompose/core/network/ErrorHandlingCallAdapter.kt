package com.kursor.crypto_decompose.core.network

import retrofit2.Call
import retrofit2.CallAdapter
import com.kursor.crypto_decompose.core.debug_tools.DebugTools
import com.kursor.crypto_decompose.core.error_handling.ApplicationException
import java.lang.reflect.Type

/**
 * Converts platform exceptions to [ApplicationException]s. See [ErrorHandlingCall] for more details.
 */
internal class ErrorHandlingCallAdapter<R>(
    private val responseType: Type,
    private val debugTools: DebugTools
) : CallAdapter<R, Call<R>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): Call<R> = ErrorHandlingCall(
        sourceCall = call,
        debugTools = debugTools
    )
}