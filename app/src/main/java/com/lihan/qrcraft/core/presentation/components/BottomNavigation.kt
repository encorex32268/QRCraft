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
import com.lihan.qrcraft.core.presentation.model.BottomItem
import com.lihan.qrcraft.core.domain.Route
import com.lihan.qrcraft.core.presentation.model.bottomItems
import com.lihan.qrcraft.core.presentation.Scan
import com.lihan.qrcraft.ui.theme.LinkBG
import com.lihan.qrcraft.ui.theme.Primary
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher

@Composable
fun BottomNavigation(
    currentRoute: Route,
    modifier: Modifier = Modifier,
    items: List<BottomItem> = bottomItems,
    onItemClick: (BottomItem) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .background(SurfaceHigher,RoundedCornerShape(100))
                .clip(RoundedCornerShape(100))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(44.dp)
                        .clickable{
                            onItemClick(item)
                        }
                        .background(color = if (isSelected) LinkBG else Color.Transparent),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        modifier = Modifier.size(22.dp),
                        imageVector = ImageVector.vectorResource(item.resId),
                        contentDescription = item.name
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(64.dp)
                .background(color = Primary)
                .padding(horizontal = 4.dp)
                .clickable(
                    indication = null,
                    interactionSource = null
                ){
                    val scan = items.find { it.route == Route.Scan }
                    scan?.let {
                        onItemClick(it)
                    }
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


@Preview(showSystemUi = true)
@Composable
private fun BottomNavigationPreview() {
    QRCraftTheme {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            onItemClick = {},
            currentRoute = Route.Generate
        )
    }
}