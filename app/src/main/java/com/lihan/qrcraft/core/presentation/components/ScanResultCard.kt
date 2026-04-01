package com.lihan.qrcraft.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.presentation.design_system.buttons.QRCraftButton
import com.lihan.qrcraft.core.presentation.util.asString
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
fun ScanResultCard(
    type: Int,
    content: String,
    onShare: () -> Unit,
    onCopy:() -> Unit,
    modifier: Modifier = Modifier
) {
    val qrCodeType = remember(type) {
        QRCodeType.getQRCodeType(type)
    }
    var isShowMoreTextShow by remember {
        mutableStateOf(false)
    }
    var isShowMoreText by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ){
        Surface(
            modifier = Modifier.padding(top = 80.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = qrCodeType.asString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(10.dp))
                when(qrCodeType){
                    QRCodeType.PhoneNumber,
                    QRCodeType.Geolocation,
                    QRCodeType.WiFi,
                    QRCodeType.Contact -> {
                        SelectionContainer(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    QRCodeType.Text -> {
                        SelectionContainer(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                overflow = if (isShowMoreText) TextOverflow.Visible else TextOverflow.Ellipsis,
                                maxLines = if (isShowMoreText) Int.MAX_VALUE else 6,
                                onTextLayout = {
                                    isShowMoreTextShow = it.hasVisualOverflow
                                }
                            )
                        }
                        if (isShowMoreTextShow){
                            Spacer(Modifier.height(4.dp))
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .clickable{
                                        isShowMoreText = true
                                    },
                                text = stringResource(R.string.show_more),
                                style = MaterialTheme.typography.labelLarge,
                                color = OnSurfaceAlt
                            )
                        }
                    }
                    QRCodeType.Link -> {
                        TextLinkButton(
                            text = content,
                            onClick = {

                            }
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QRCraftButton(
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.share),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        },
                        text = stringResource(R.string.share),
                        containerColor = SurfaceHigher,
                        onClick = onShare
                    )

                    QRCraftButton(
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.copy),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        },
                        text = stringResource(R.string.copy),
                        containerColor = SurfaceHigher,
                        onClick = onCopy
                    )
                }
            }
        }
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceHigher,
            modifier = Modifier
                .size(160.dp),
            shadowElevation = 4.dp
        ){
            Image(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                painter = rememberQrCodePainter(content),
                contentDescription = null
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun ScanResultCardPreview() {
    QRCraftTheme {
        ScanResultCard(
            modifier = Modifier
                .fillMaxWidth(),
            type = QRCodeType.WiFi.type,
            content = """
                Text
            """.trimIndent(),
            onCopy = {},
            onShare = {}
        )
    }
}