package com.lihan.qrcraft.scan.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.presentation.AlertTriangle
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher

@Composable
fun NoQRCodeFoundDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceHigher, RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = AlertTriangle,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = stringResource(R.string.no_qrcode_found)
            )
            Text(
                text = stringResource(R.string.no_qrcode_found),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun NoQRCodeFoundDialogPreview() {
    QRCraftTheme {
        NoQRCodeFoundDialog(
            onDismiss = {}
        )
    }
}