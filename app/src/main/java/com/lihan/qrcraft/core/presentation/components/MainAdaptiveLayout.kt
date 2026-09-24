package com.lihan.qrcraft.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.core.presentation.navigation.TopLevelDestination
import com.lihan.qrcraft.core.ui.theme.appColors

@Composable
fun MainAdaptiveLayout(
    selectedDestination: TopLevelDestination,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
    floatingBottomBar: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val isWideScreen = LocalConfiguration.current.screenWidthDp >= 600

    if (isWideScreen) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.appColors.surfaceHigher)
        ) {
            AdaptiveNavigationRail(
                selectedDestination = selectedDestination,
                onItemClick = onNavigateToDestination,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 16.dp, horizontal = 12.dp)
                    .systemBarsPadding()
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                content(PaddingValues())
                Box(modifier = Modifier.fillMaxSize()) {
                    snackbarHost()
                }
            }
        }
    } else {
        if (floatingBottomBar) {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = containerColor,
                contentWindowInsets = contentWindowInsets,
                snackbarHost = snackbarHost
            ) { _ ->
                Box(modifier = Modifier.fillMaxSize()) {
                    content(PaddingValues())
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Color.Transparent)
                            .navigationBarsPadding(),
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavigation(
                            selectedDestination = selectedDestination,
                            onItemClick = onNavigateToDestination
                        )
                    }
                }
            }
        } else {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = containerColor,
                contentWindowInsets = contentWindowInsets,
                snackbarHost = snackbarHost,
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .navigationBarsPadding(),
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavigation(
                            selectedDestination = selectedDestination,
                            onItemClick = onNavigateToDestination
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    content(innerPadding)
                }
            }
        }
    }
}
