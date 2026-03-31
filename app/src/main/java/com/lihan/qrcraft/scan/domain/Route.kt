package com.lihan.qrcraft.scan.domain

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
}