package com.lihan.qrcraft.core.presentation.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController

fun NavHostController.safeNavigateUp() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        if (!navigateUp()) {
            popBackStack()
        }
    }
}
