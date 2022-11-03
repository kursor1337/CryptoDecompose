package com.kursor.roombookkeepingmobileupstack.core.debug_tools

import okhttp3.Interceptor

interface DebugTools {

    val interceptors: List<Interceptor>

    fun launch()

    fun collectNetworkError(exception: Exception)
}