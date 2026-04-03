package com.lihan.qrcraft.history.presentation.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.domain.util.formatTimeString
import com.lihan.qrcraft.core.presentation.Star
import com.lihan.qrcraft.core.presentation.StarFill
import com.lihan.qrcraft.core.presentation.components.CircleIcon
import com.lihan.qrcraft.core.presentation.design_system.buttons.QRCraftIconButton
import com.lihan.qrcraft.generate.presentation.model.toQRCodeTypeUi
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.OnSurfaceDisabled
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher

@Composable
fun QRCodeHistoryItem(
    type: Int,
    content: String,
    timestamp: Long,
    isFavorite: Boolean,
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String?=null
) {
    val qrCodeTypeUi = remember(type){
        QRCodeType.getQRCodeType(type).toQRCodeTypeUi()
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = onItemLongClick
            ),
        color = SurfaceHigher
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp)
                            .basicMarquee(),
                        text = title ?: stringResource(qrCodeTypeUi.stringResId),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
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
            QRCraftIconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(32.dp),
                imageVector = if (isFavorite){
                    StarFill
                }else{
                    Star
                },
                onClick = onFavoriteClick,
                containerColor = Color.Transparent,
                tintColor = if (isFavorite){
                    MaterialTheme.colorScheme.onSurface
                }else{
                    OnSurfaceDisabled
                }
            )
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
                timestamp = 1750746960000L,
                onItemLongClick = {},
                onItemClick = {},
                isFavorite = true,
                onFavoriteClick = {}
            )

            QRCodeHistoryItem(
                type = QRCodeType.Text.type,
                content = "Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum justo metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium.",
                timestamp = 1750746960000L,
                onItemLongClick = {},
                onItemClick = {},
                isFavorite = false,
                onFavoriteClick = {}
            )
        }

    }
}