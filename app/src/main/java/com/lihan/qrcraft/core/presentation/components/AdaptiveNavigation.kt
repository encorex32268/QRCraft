package com.lihan.qrcraft.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.lihan.qrcraft.core.presentation.Scan
import com.lihan.qrcraft.core.presentation.navigation.TopLevelDestination
import com.lihan.qrcraft.core.ui.theme.QRCraftTheme
import com.lihan.qrcraft.core.ui.theme.appColors

@Composable
fun AdaptiveNavigationRail(
    selectedDestination: TopLevelDestination?,
    modifier: Modifier = Modifier,
    items: List<TopLevelDestination> = TopLevelDestination.entries,
    onItemClick: (TopLevelDestination) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.appColors.surfaceHigher, RoundedCornerShape(100))
                .clip(RoundedCornerShape(100))
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach { item ->
                val isSelected = selectedDestination == item
                if (item == TopLevelDestination.SCAN) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(64.dp)
                            .background(color = MaterialTheme.colorScheme.primary)
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                onItemClick(item)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Scan,
                            contentDescription = item.name
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(44.dp)
                            .clickable {
                                onItemClick(item)
                            }
                            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(22.dp),
                            imageVector = ImageVector.vectorResource(item.iconId),
                            contentDescription = item.name
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdaptiveNavigationRail(
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    items: List<TopLevelDestination> = TopLevelDestination.entries,
    onItemClick: (TopLevelDestination) -> Unit,
) {
    val selectedDestination = items.firstOrNull { currentDestination?.hasRoute(it.routeClass) == true }
    AdaptiveNavigationRail(
        selectedDestination = selectedDestination,
        modifier = modifier,
        items = items,
        onItemClick = onItemClick
    )
}

@Preview(showSystemUi = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun AdaptiveNavigationRailPreview() {
    QRCraftTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            AdaptiveNavigationRail(
                onItemClick = {},
                selectedDestination = TopLevelDestination.SCAN
            )
        }
    }
}
