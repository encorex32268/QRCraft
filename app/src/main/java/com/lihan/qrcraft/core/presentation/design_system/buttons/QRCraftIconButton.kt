package com.lihan.qrcraft.core.presentation.design_system.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.ui.theme.OnSurfaceDisabled
import com.lihan.qrcraft.ui.theme.Primary
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun QRCraftIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tintColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = Primary,
    iconSize: Dp = 16.dp,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = OnSurfaceDisabled
        )
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = imageVector,
            contentDescription = null,
            tint = tintColor
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun QRCraftIconButtonPreview() {
    QRCraftTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            QRCraftIconButton(
                imageVector = ImageVector.vectorResource(R.drawable.scan),
                onClick = {},
                enabled = true
            )
            QRCraftIconButton(
                imageVector = ImageVector.vectorResource(R.drawable.scan),
                onClick = {},
                enabled = false
            )
        }
    }

}