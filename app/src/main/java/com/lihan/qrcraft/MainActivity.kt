package com.lihan.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.core.presentation.components.BottomNavigation
import com.lihan.qrcraft.core.presentation.navigation.rememberQRCraftAppState
import com.lihan.qrcraft.core.presentation.navigation.safeNavigateUp
import com.lihan.qrcraft.core.presentation.screens.preview.PreviewScreenRoot
import com.lihan.qrcraft.generate.presentation.GenerateScreen
import com.lihan.qrcraft.generate.presentation.create.CreateScreenRoot
import com.lihan.qrcraft.history.presentation.ScanHistoryScreenRoot
import com.lihan.qrcraft.scan.presentation.ScanScreenRoot
import com.lihan.qrcraft.core.ui.theme.QRCraftTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import com.lihan.qrcraft.core.presentation.components.AdaptiveNavigationRail


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                val appState = rememberQRCraftAppState()
                val currentDestination = appState.currentDestination
                val showNav = appState.currentTopLevelDestination != null
                val isWideScreen = LocalConfiguration.current.screenWidthDp >= 600

                if (isWideScreen) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (showNav) {
                            AdaptiveNavigationRail(
                                currentDestination = currentDestination,
                                onItemClick = { topLevelDestination ->
                                    appState.navigateToTopLevelDestination(topLevelDestination)
                                },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 16.dp, horizontal = 12.dp)
                                    .systemBarsPadding()
                            )
                        }
                        AppNavHost(
                            navController = appState.navController,
                            startDestination = Route.Scan,
                            closeApp = { finish() },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent,
                        bottomBar = {
                            if (showNav) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent)
                                        .navigationBarsPadding(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BottomNavigation(
                                        currentDestination = currentDestination,
                                        onItemClick = { topLevelDestination ->
                                            appState.navigateToTopLevelDestination(topLevelDestination)
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        innerPadding
                        AppNavHost(
                            navController = appState.navController,
                            startDestination = Route.Scan,
                            closeApp = { finish() },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    startDestination: Route,
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
                closeApp = closeApp
            )
        }

        composable<Route.Generate> {
            GenerateScreen(
                onItemClick = { qrCodeTypeUi ->
                    navController.navigate(
                        Route.Create(qrCodeTypeUi.type)
                    )
                }
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
                }
            )
        }
    }
}

