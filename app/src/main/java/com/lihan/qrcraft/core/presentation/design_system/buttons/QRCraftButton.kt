package com.lihan.qrcraft.core.presentation.design_system.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.ui.theme.OnSurfaceDisabled
import com.lihan.qrcraft.ui.theme.Primary
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun QRCraftButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (()->Unit)?=null,
    trailingIcon: @Composable (()->Unit)?=null,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = Color.Transparent
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = if (containerColor != Primary){
                Color.Transparent
            }else{
                MaterialTheme.colorScheme.surface
            },
            disabledContentColor = OnSurfaceDisabled
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null){
                leadingIcon()
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled){
                    textColor
                }else{
                    OnSurfaceDisabled
                }
            )
            if (trailingIcon != null){
                Spacer(Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

@Preview
@Composable
private fun QRCraftButtonPreview() {
    val qrCodeIconEnable = @Composable {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.scan),
            contentDescription = "Scan",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
    val qrCodeIconDisable = @Composable {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.scan),
            contentDescription = "Scan",
            tint = OnSurfaceDisabled
        )
    }
    val qrCodeIconError = @Composable {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.scan),
            contentDescription = "Scan",
            tint = MaterialTheme.colorScheme.error
        )
    }
    QRCraftTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            QRCraftButton(
                text = "Button",
                onClick = {},
                containerColor = Primary,
                leadingIcon = qrCodeIconEnable,
                trailingIcon = qrCodeIconEnable
            )
            QRCraftButton(
                text = "Button",
                onClick = {},
                containerColor = Primary,
                leadingIcon = qrCodeIconDisable,
                trailingIcon = qrCodeIconDisable,
                enabled = false
            )
            QRCraftButton(
                text = "Button",
                onClick = {},
                leadingIcon = qrCodeIconEnable,
                trailingIcon = qrCodeIconEnable,
                enabled = true
            )
            QRCraftButton(
                text = "Button",
                onClick = {},
                leadingIcon = qrCodeIconDisable,
                trailingIcon = qrCodeIconDisable,
                enabled = false
            )

            QRCraftButton(
                text = "Button",
                onClick = {},
                textColor = MaterialTheme.colorScheme.error,
                enabled = true,
                leadingIcon = qrCodeIconError,
                trailingIcon = qrCodeIconError,
            )
        }

    }

}