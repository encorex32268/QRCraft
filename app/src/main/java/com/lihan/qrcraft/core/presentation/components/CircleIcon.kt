package com.lihan.qrcraft.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleIcon(
    backgroundColor: Color,
    iconTintColor: Color,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    onClick: (() -> Unit)?=null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable(
                onClick = {
                    onClick?.invoke()
                }
            )
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = iconTintColor
        )
    }

}
