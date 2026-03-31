package com.lihan.qrcraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.lihan.qrcraft.scan.domain.Route
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
                NavHost(
                    navController = navController,
                    startDestination = Route.Scan
                ){
                    composable<Route.Scan>{
                        ScanScreenRoot(
                            navigateToResult = { type,content ->
                                println("navigateToResult Type: $type / Content: $content")
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
                }

            }
        }
    }
}
