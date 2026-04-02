package com.lihan.qrcraft.scan.presentation

sealed interface ScanUiEvent {
    data class NavigateToPreview(val id: Long): ScanUiEvent
}