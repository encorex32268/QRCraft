package com.lihan.qrcraft.core.presentation.screens.preview.components

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.presentation.Copy
import com.lihan.qrcraft.core.presentation.Share
import com.lihan.qrcraft.core.presentation.components.CircleIcon
import com.lihan.qrcraft.core.presentation.components.TextLinkButton
import com.lihan.qrcraft.core.presentation.design_system.buttons.QRCraftButton
import com.lihan.qrcraft.core.presentation.design_system.buttons.QRCraftIconButton
import com.lihan.qrcraft.core.presentation.util.asString
import com.lihan.qrcraft.core.presentation.util.openAddContact
import com.lihan.qrcraft.core.presentation.util.openBrowser
import com.lihan.qrcraft.core.presentation.util.openCallPhone
import com.lihan.qrcraft.core.presentation.util.openMapOrBrowser
import com.lihan.qrcraft.core.presentation.util.openWifiSettings
import com.lihan.qrcraft.ui.theme.Contact
import com.lihan.qrcraft.ui.theme.ContactBG
import com.lihan.qrcraft.ui.theme.Geo
import com.lihan.qrcraft.ui.theme.GeoBG
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.Phone
import com.lihan.qrcraft.ui.theme.PhoneBG
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher
import com.lihan.qrcraft.ui.theme.WiFi
import com.lihan.qrcraft.ui.theme.WiFiBG
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
fun QRCodePreviewCard(
    titleTextFieldState: TextFieldState,
    type: Int,
    title: String?,
    content: String,
    onShare: () -> Unit,
    onCopy:() -> Unit,
    onSave:() -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val qrCodeType = remember(type) {
        QRCodeType.getQRCodeType(type)
    }
    var isShowMoreTextShow by remember {
        mutableStateOf(false)
    }
    var isShowMoreText by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        if (!isFocused){
            keyboard?.hide()
        }
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
                Spacer(Modifier.height(20.dp))
                BasicTextField(
                    interactionSource = interactionSource,
                    state = titleTextFieldState,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    decorator = { innerTextField ->
                        Box(contentAlignment = Alignment.Center){
                            if (titleTextFieldState.text.isEmpty()){
                                Text(
                                    modifier = Modifier.alpha(
                                        if (isFocused) 0.2f else 1f
                                    ),
                                    text = title?:qrCodeType.asString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            innerTextField()
                        }
                    },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    onKeyboardAction = {
                        focusManager.clearFocus(true)
                    }
                )
                Spacer(Modifier.height(10.dp))
                when(qrCodeType){
                    QRCodeType.Link -> {
                        TextLinkButton(
                            text = content,
                            onClick = { context.openBrowser(content) }
                        )
                    }
                    QRCodeType.PhoneNumber -> {
                        val phoneNumber = when{
                            content.contains(":") -> {
                                content.split(":").getOrNull(1).toString()
                            }
                            content.contains("+") -> {
                                "+" + content.split("+").getOrNull(1).toString()
                            }
                            else -> content
                        }
                        TextLinkButton(
                            text = content,
                            onClick = { context.openCallPhone(phoneNumber) },
                            color = Phone,
                            background = PhoneBG
                        )
                    }
                    QRCodeType.Geolocation -> {
                        val lat = content.split(",").getOrNull(0)?.toDoubleOrNull()?:0.0
                        val lng = content.split(",").getOrNull(1)?.toDoubleOrNull()?:0.0
                        TextLinkButton(
                            text = content,
                            onClick = { context.openMapOrBrowser(lat,lng) },
                            color = Geo,
                            background = GeoBG,
                        )
                    }
                    QRCodeType.WiFi -> {
                        TextLinkButton(
                            text = content,
                            color = WiFi,
                            background = WiFiBG,
                            onClick = {
                                onCopy()
                                context.openWifiSettings()
                            }
                        )
                    }
                    QRCodeType.Contact -> {
                        val name = content.split("\n").getOrNull(0)?:"Unknown"
                        val email = content.split("\n").getOrNull(1)?:"email"
                        val phoneNumber = content.split("\n").getOrNull(2)?:"phoneNumber"
                        TextLinkButton(
                            text = content,
                            color = Contact,
                            background = ContactBG,
                            onClick = {
                                context.openAddContact(
                                    name = name,
                                    phone = phoneNumber,
                                    email = email
                                )
                            }
                        )
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
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircleIcon(
                        iconTintColor = MaterialTheme.colorScheme.onSurface,
                        backgroundColor = SurfaceHigher,
                        imageVector = Share,
                        onClick = onShare,
                        size = 44.dp
                    )
                    CircleIcon(
                        iconTintColor = MaterialTheme.colorScheme.onSurface,
                        backgroundColor = SurfaceHigher,
                        imageVector = Copy,
                        onClick = onCopy,
                        size = 44.dp
                    )
                    QRCraftButton(
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.download),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        },
                        text = stringResource(R.string.save),
                        containerColor = SurfaceHigher,
                        onClick = onSave
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
private fun QRCodePreviewCardPreview() {
    QRCraftTheme {
        QRCodePreviewCard(
            modifier = Modifier
                .fillMaxWidth(),
            type = QRCodeType.WiFi.type,
            content = """
                Text
            """.trimIndent(),
            onCopy = {},
            onShare = {},
            onSave = {},
            titleTextFieldState = TextFieldState(initialText = "Te"),
            title = "Teee"
        )
    }
}