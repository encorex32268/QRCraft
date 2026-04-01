package com.lihan.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.lihan.qrcraft.core.presentation.components.BottomNavigation
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.scan.presentation.ScanScreenRoot
import com.lihan.qrcraft.scan.presentation.result.ScanResultScreenRoot
import com.lihan.qrcraft.ui.theme.QRCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRCraftTheme {
                val navController = rememberNavController()


                var selectedRoute by remember {
                    mutableStateOf<Route>(Route.Scan)
                }


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0),
                    containerColor = Color.Transparent,
                    bottomBar = {
                        if (selectedRoute in Route.showBottomBarRoute()){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                                    .navigationBarsPadding(),
                                contentAlignment = Alignment.Center
                            ){
                                BottomNavigation(
                                    onItemClick = { bottomItem ->
                                        selectedRoute = bottomItem.route
                                        navController.navigate(bottomItem.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    selectedRoute = selectedRoute
                                )
                            }
                        }
                    }
                ) {  it
                    NavHost(
                        modifier =Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = Route.Scan
                    ){
                        composable<Route.Scan>{
                            ScanScreenRoot(
                                navigateToResult = { type,content ->
                                    navController.navigate(
                                        Route.ScanResult(
                                            type = type,
                                            content = content
                                        )
                                    )
                                },
                                closeApp = {
                                    finishActivity(101)
                                }
                            )
                        }
                        composable<Route.ScanResult>{
                            ScanResultScreenRoot(
                                onBack = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable<Route.History>{
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "History"
                                )
                            }
                        }
                        composable<Route.Generate>{
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "Generate"
                                )
                            }
                        }
                    }

                }


            }
        }
    }
}
