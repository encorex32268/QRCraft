package com.lihan.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.core.presentation.navigation.TopLevelDestination
import com.lihan.qrcraft.core.presentation.navigation.rememberQRCraftAppState
import com.lihan.qrcraft.core.presentation.navigation.safeNavigateUp
import com.lihan.qrcraft.core.presentation.screens.preview.PreviewScreenRoot
import com.lihan.qrcraft.core.ui.theme.QRCraftTheme
import com.lihan.qrcraft.generate.presentation.GenerateScreen
import com.lihan.qrcraft.generate.presentation.create.CreateScreenRoot
import com.lihan.qrcraft.history.presentation.ScanHistoryScreenRoot
import com.lihan.qrcraft.scan.presentation.ScanScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                val appState = rememberQRCraftAppState()

                AppNavHost(
                    navController = appState.navController,
                    startDestination = Route.Scan,
                    onNavigateToDestination = { topLevelDestination ->
                        appState.navigateToTopLevelDestination(topLevelDestination)
                    },
                    closeApp = { finish() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    startDestination: Route,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    closeApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Route.Scan> {
            ScanScreenRoot(
                navigateToPreview = { id, screenTitle ->
                    navController.navigate(
                        Route.Preview(
                            id = id,
                            screenTitle = screenTitle
                        )
                    )
                },
                onNavigateToDestination = onNavigateToDestination,
                closeApp = closeApp
            )
        }

        composable<Route.Generate> {
            GenerateScreen(
                onItemClick = { qrCodeTypeUi ->
                    navController.navigate(
                        Route.Create(qrCodeTypeUi.type)
                    )
                },
                onNavigateToDestination = onNavigateToDestination
            )
        }

        composable<Route.Create> {
            CreateScreenRoot(
                onBack = {
                    navController.safeNavigateUp()
                },
                navigateToPreview = { id, screenTitle ->
                    navController.navigate(
                        Route.Preview(
                            id = id,
                            screenTitle = screenTitle
                        )
                    )
                }
            )
        }

        composable<Route.Preview> {
            PreviewScreenRoot(
                onBack = {
                    navController.safeNavigateUp()
                }
            )
        }

        composable<Route.History> {
            ScanHistoryScreenRoot(
                navigateToPreview = { id, screenTitle ->
                    navController.navigate(
                        Route.Preview(
                            id = id,
                            screenTitle = screenTitle
                        )
                    )
                },
                onNavigateToDestination = onNavigateToDestination
            )
        }
    }
}
