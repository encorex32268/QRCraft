package com.lihan.qrcraft.core.presentation.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.lihan.qrcraft.core.domain.Route

fun NavDestination.currentRoute(): Route {
    return listOf(
        Route.History,
        Route.Scan,
        Route.Generate
    ).first {
        this.hasRoute(it::class)
    }

}

fun NavDestination?.isMainRoute(): Boolean {
    this ?: return false
    return hasRoute<Route.Scan>() ||
            hasRoute<Route.History>() ||
            hasRoute<Route.Generate>()
}

fun NavHostController.safeNavigateUp() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        if (!navigateUp()) {
            popBackStack()
        }
    }
}