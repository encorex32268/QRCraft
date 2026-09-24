package com.lihan.qrcraft.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.lihan.qrcraft.core.presentation.Scan
import com.lihan.qrcraft.core.presentation.navigation.TopLevelDestination
import com.lihan.qrcraft.core.ui.theme.QRCraftTheme
import com.lihan.qrcraft.core.ui.theme.appColors

@Composable
fun BottomNavigation(
    selectedDestination: TopLevelDestination?,
    modifier: Modifier = Modifier,
    items: List<TopLevelDestination> = TopLevelDestination.entries,
    onItemClick: (TopLevelDestination) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.appColors.surfaceHigher,
                    shape = RoundedCornerShape(100)
                )
                .clip(RoundedCornerShape(100))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                if (item == TopLevelDestination.SCAN) {
                    Spacer(modifier = Modifier.size(44.dp))
                } else {
                    val isSelected = selectedDestination == item
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(44.dp)
                            .clickable{
                                onItemClick(item)
                            }
                            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            modifier = Modifier.size(22.dp),
                            imageVector = ImageVector.vectorResource(item.iconId),
                            contentDescription = item.name,
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(horizontal = 4.dp)
                .clickable(
                    indication = null,
                    interactionSource = null
                ){
                    onItemClick(TopLevelDestination.SCAN)
                },
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Scan,
                contentDescription = "Scan"
            )
        }
    }
}

@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    items: List<TopLevelDestination> = TopLevelDestination.entries,
    onItemClick: (TopLevelDestination) -> Unit,
) {
    val selectedDestination = items.firstOrNull { currentDestination?.hasRoute(it.routeClass) == true }
    BottomNavigation(
        selectedDestination = selectedDestination,
        modifier = modifier,
        items = items,
        onItemClick = onItemClick
    )
}

@Preview(showSystemUi = true)
@Composable
private fun BottomNavigationPreview() {
    QRCraftTheme {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            onItemClick = {},
            selectedDestination = TopLevelDestination.SCAN
        )
    }
}