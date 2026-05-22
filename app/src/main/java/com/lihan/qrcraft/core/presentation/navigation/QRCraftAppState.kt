package com.lihan.qrcraft.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lihan.qrcraft.core.domain.Route

@Composable
fun rememberQRCraftAppState(
    navController: NavHostController = rememberNavController()
): QRCraftAppState {
    return remember(navController) {
        QRCraftAppState(navController)
    }
}

@Stable
class QRCraftAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when {
            currentDestination?.hasRoute<Route.Scan>() == true -> TopLevelDestination.SCAN
            currentDestination?.hasRoute<Route.History>() == true -> TopLevelDestination.HISTORY
            currentDestination?.hasRoute<Route.Generate>() == true -> TopLevelDestination.GENERATE
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        navController.navigate(topLevelDestination.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
