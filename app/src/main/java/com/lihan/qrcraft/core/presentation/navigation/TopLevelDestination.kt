package com.lihan.qrcraft.core.presentation.navigation

import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.Route
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: Route,
    val iconId: Int,
    val labelId: Int,
    val routeClass: KClass<*>
) {
    HISTORY(
        route = Route.History,
        iconId = R.drawable.clock_refresh,
        labelId = R.string.history,
        routeClass = Route.History::class
    ),
    SCAN(
        route = Route.Scan,
        iconId = R.drawable.scan,
        labelId = R.string.scan,
        routeClass = Route.Scan::class
    ),
    GENERATE(
        route = Route.Generate,
        iconId = R.drawable.plus_circle,
        labelId = R.string.generate,
        routeClass = Route.Generate::class
    )
}
