package com.lihan.qrcraft.core.presentation.screens.preview


sealed interface PreviewUiEvent {
    data object DataError: PreviewUiEvent
    data object Back: PreviewUiEvent
    data class ShareQRCode(val title: String, val content: String): PreviewUiEvent
    data object SaveSucceed: PreviewUiEvent
}