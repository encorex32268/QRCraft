package com.lihan.qrcraft.core.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object Scan: Route

    @Serializable
    data class ScanResult(
        val type: Int,
        val content: String
    ): Route

    @Serializable
    data object History: Route

    @Serializable
    data object Generate: Route

    @Serializable
    data class Create(val type: Int): Route

    @Serializable
    data class Preview(
        val type: Int,
        val content: String
    ): Route
}