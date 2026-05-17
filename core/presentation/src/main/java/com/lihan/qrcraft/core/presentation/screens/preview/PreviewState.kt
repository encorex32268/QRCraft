package com.lihan.qrcraft.core.presentation.screens.preview

import androidx.compose.foundation.text.input.TextFieldState
import com.lihan.qrcraft.core.presentation.model.QRCodeHistoryUi

data class PreviewState(
    val screenTitle: String = "",
    val title: TextFieldState = TextFieldState(),
    val qrCodeHistoryUi: QRCodeHistoryUi?=null
)
