package com.lihan.qrcraft.core.presentation.screens.preview

import androidx.compose.foundation.text.input.TextFieldState
import com.lihan.qrcraft.core.presentation.model.QRCodeHistoryUi

data class PreviewState(
    val isEnabledTitle: Boolean = false,
    val title: TextFieldState = TextFieldState(),
    val qrCodeHistoryUi: QRCodeHistoryUi?=null
)
