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

    companion object{
        fun showBottomBarRoute(): List<Route>{
            return listOf(
                Scan, History, Generate
            )
        }
    }
}