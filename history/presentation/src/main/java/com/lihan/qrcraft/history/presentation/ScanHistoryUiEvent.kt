package com.lihan.qrcraft.history.presentation

sealed interface ScanHistoryUiEvent {
    data class ShareQRCode(val title: String, val content: String): ScanHistoryUiEvent
    data class NavigateToPreview(val id: Long): ScanHistoryUiEvent

}