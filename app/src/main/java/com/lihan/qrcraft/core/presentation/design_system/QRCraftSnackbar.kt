package com.lihan.qrcraft.core.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.ui.theme.OnSurface
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.Success

@Composable
fun QRCraftSnackbar(
    text: String,
    containerColor: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(6.dp),
    leadingIcon: @Composable (() -> Unit)?=null
) {
    Row(
        modifier = modifier
            .clip(shape)
            .background(
                color = containerColor,
                shape = shape
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        if (leadingIcon != null){
            leadingIcon()
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun QRCraftSnackbarPreview() {
    QRCraftTheme {
        QRCraftSnackbar(
            modifier = Modifier.fillMaxWidth(),
            text = "Action",
            containerColor = Success,
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.heart),
                    contentDescription = null,
                    tint = OnSurface
                )
            }
        )
    }
}