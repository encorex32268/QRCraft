package com.lihan.qrcraft.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.domain.formatTimeString
import com.lihan.qrcraft.core.presentation.components.CircleIcon
import com.lihan.qrcraft.generate.presentation.model.toQRCodeTypeUi
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.OnSurfaceDisabled
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun QRCodeHistoryItem(
    type: Int,
    content: String,
    timestamp: Long,
    modifier: Modifier = Modifier,
    title: String?=null
) {
    val qrCodeTypeUi = remember(type){
        QRCodeType.getQRCodeType(type).toQRCodeTypeUi()
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircleIcon(
                backgroundColor = qrCodeTypeUi.iconBackgroundColor,
                imageVector = ImageVector.vectorResource(qrCodeTypeUi.iconResId),
                iconTintColor = qrCodeTypeUi.iconTintColor
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title ?: stringResource(qrCodeTypeUi.stringResId),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceAlt
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = timestamp.formatTimeString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceDisabled
                )
            }
        }

    }





}

@Preview
@Composable
private fun QRCodeHistoryItemPreview() {
    QRCraftTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            QRCodeHistoryItem(
                type = QRCodeType.Link.type,
                content = "https://www.google.com/maps",
                timestamp = 1750746960000L
            )

            QRCodeHistoryItem(
                type = QRCodeType.Text.type,
                content = "Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum justo metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium.",
                timestamp = 1750746960000L
            )
        }

    }
}