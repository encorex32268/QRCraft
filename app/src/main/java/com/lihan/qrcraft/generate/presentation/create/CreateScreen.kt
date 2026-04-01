@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.qrcraft.generate.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.presentation.ArrowLeft
import com.lihan.qrcraft.core.presentation.design_system.buttons.QRCraftButton
import com.lihan.qrcraft.core.presentation.util.ObserveAsEvents
import com.lihan.qrcraft.generate.presentation.components.CreateQRTextField
import com.lihan.qrcraft.generate.presentation.model.QRCodeTypeUi
import com.lihan.qrcraft.ui.theme.Primary
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateScreenRoot(
    navigateToPreview: (Int,String) -> Unit,
    onBack: () -> Unit,
    viewModel: CreateViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { uiEvent ->
        when(uiEvent){
            is CreateUiEvent.NavigateToPreview -> navigateToPreview(uiEvent.type,uiEvent.dataString)
        }
    }

    CreateScreen(
        state = state,
        onAction = { action ->
            when(action){
                CreateAction.BackClick -> onBack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}


@Composable
fun CreateScreen(
    state: CreateState,
    onAction: (CreateAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    val qrCodeType = remember(state.type) { QRCodeType.getQRCodeType(state.type) }

    val focusRequesterSecond = remember { FocusRequester() }
    val focusRequesterThird = remember { FocusRequester() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = qrCodeType.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onAction(CreateAction.BackClick)
                    }
                ) {
                    Icon(
                        imageVector = ArrowLeft,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(16.dp),
            color = SurfaceHigher
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when(qrCodeType){
                    QRCodeType.Text,
                    QRCodeType.PhoneNumber,
                    QRCodeType.Link -> {
                        CreateQRTextField(
                            state = state.textFieldStateFirst,
                            placeholder = when(qrCodeType){
                                QRCodeType.Link -> stringResource(R.string.link)
                                QRCodeType.PhoneNumber -> stringResource(R.string.phone_number)
                                else -> stringResource(R.string.text)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActionHandler = {
                                focusManager.clearFocus(true)
                                keyboard?.hide()
                            }
                        )
                    }
                    QRCodeType.Geolocation -> {
                        CreateQRTextField(
                            state = state.textFieldStateFirst,
                            placeholder = stringResource(R.string.latitude),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActionHandler = {
                                focusRequesterSecond.requestFocus()
                            }
                        )
                        CreateQRTextField(
                            modifier = Modifier.focusRequester(focusRequesterSecond),
                            state = state.textFieldStateSecond,
                            placeholder = stringResource(R.string.longitude),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActionHandler = {
                                focusManager.clearFocus(true)
                                keyboard?.hide()
                            }
                        )
                    }
                    QRCodeType.WiFi,
                    QRCodeType.Contact -> {
                        CreateQRTextField(
                            state = state.textFieldStateFirst,
                            placeholder = if (qrCodeType == QRCodeType.WiFi){
                                stringResource(R.string.ssid)
                            }else{
                                stringResource(R.string.name)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActionHandler = {
                                focusRequesterSecond.requestFocus()
                            }
                        )
                        CreateQRTextField(
                            modifier = Modifier.focusRequester(focusRequesterSecond),
                            state = state.textFieldStateSecond,
                            placeholder = if (qrCodeType == QRCodeType.WiFi){
                                stringResource(R.string.password)
                            }else{
                                stringResource(R.string.email)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = if (qrCodeType == QRCodeType.WiFi){
                                    KeyboardType.Password
                                }else{
                                    KeyboardType.Email
                                },
                                imeAction = ImeAction.Next
                            ),
                            keyboardActionHandler = {
                                focusRequesterThird.requestFocus()
                            }
                        )
                        CreateQRTextField(
                            modifier = Modifier.focusRequester(focusRequesterThird),
                            state = state.textFieldStateThird,
                            placeholder = if (qrCodeType == QRCodeType.WiFi){
                                stringResource(R.string.encryption_type)
                            }else{
                                stringResource(R.string.phone_number)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActionHandler = {
                                focusManager.clearFocus(true)
                                keyboard?.hide()
                            }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                QRCraftButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.generate_qr_code),
                    onClick = {
                        focusManager.clearFocus(true)
                        keyboard?.hide()
                        onAction(CreateAction.GenerateButtonClick)
                    },
                    enabled = state.generateButtonEnabled,
                    containerColor = Primary
                )
            }
        }

    }
}





@Preview(showBackground = true)
@Composable
private fun CreateScreenPreview() {
    QRCraftTheme {
        CreateScreen(
            state = CreateState(
                type = QRCodeType.Text.type
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateScreenLinkPreview() {
    QRCraftTheme {
        CreateScreen(
            state = CreateState(
                type = QRCodeType.Link.type
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateScreenContactPreview() {
    QRCraftTheme {
        CreateScreen(
            state = CreateState(
                type = QRCodeType.Contact.type
            ),
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateScreenGeolocationPreview() {
    QRCraftTheme {
        CreateScreen(
            state = CreateState(
                type = QRCodeType.Geolocation.type
            ),
            onAction = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CreateScreenWiFiPreview() {
    QRCraftTheme {
        CreateScreen(
            state = CreateState(
                type = QRCodeType.WiFi.type
            ),
            onAction = {}
        )
    }
}