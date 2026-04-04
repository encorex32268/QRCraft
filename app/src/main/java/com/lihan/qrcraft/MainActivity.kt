package com.lihan.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.core.presentation.components.BottomNavigation
import com.lihan.qrcraft.core.presentation.screens.preview.PreviewScreenRoot
import com.lihan.qrcraft.generate.presentation.GenerateScreen
import com.lihan.qrcraft.generate.presentation.create.CreateScreenRoot
import com.lihan.qrcraft.history.presentation.ScanHistoryScreenRoot
import com.lihan.qrcraft.scan.presentation.ScanScreenRoot
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.Surface


fun NavDestination.currentRoute(): Route {
    return listOf(
        Route.History,
        Route.Scan,
        Route.Generate
    ).first {
        this.hasRoute(it::class)
    }

}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                val navController = rememberNavController()

                val startDestination = Route.Scan


                val currentRoute by navController
                    .currentBackStackEntryAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    bottomBar = {
                        if (currentRoute?.destination.isMainRoute()){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                                    .navigationBarsPadding(),
                                contentAlignment = Alignment.Center
                            ){
                                BottomNavigation(
                                    onItemClick = { bottomItem ->
                                        navController.navigate(bottomItem.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    currentRoute = currentRoute?.destination?.currentRoute()?:Route.Scan
                                )
                            }
                        }
                    }
                ) {  it
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = startDestination
                    ){
                        composable<Route.Scan>{
                            ScanScreenRoot(
                                navigateToPreview = { id,screenTitle ->
                                    navController.navigate(
                                        Route.Preview(
                                            id = id,
                                            screenTitle = screenTitle
                                        )
                                    )
                                },
                                closeApp = {
                                    finish()
                                }
                            )
                        }

                        composable<Route.Generate>{
                            GenerateScreen(
                                onItemClick = { qrCodeTypeUi ->
                                    navController.navigate(
                                        Route.Create(qrCodeTypeUi.type)
                                    )
                                }
                            )
                        }

                        composable<Route.Create>{
                            CreateScreenRoot(
                                onBack = {
                                    navController.safeNavigateUp()
                                },
                                navigateToPreview = { id,screenTitle ->
                                    navController.navigate(
                                        Route.Preview(
                                            id = id,
                                            screenTitle = screenTitle
                                        )
                                    )
                                }
                            )
                        }

                        composable<Route.Preview>{
                            PreviewScreenRoot(
                                onBack = {
                                    navController.safeNavigateUp()
                                }
                            )
                        }

                        composable<Route.History>{
                            ScanHistoryScreenRoot(
                                navigateToPreview = { id,screenTitle ->
                                    navController.navigate(
                                        Route.Preview(
                                            id = id,
                                            screenTitle = screenTitle
                                        )
                                    )
                                }
                            )
                        }
                    }

                }


            }
        }
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