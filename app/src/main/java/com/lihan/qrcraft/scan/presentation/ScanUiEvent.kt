package com.lihan.qrcraft.scan.presentation

sealed interface ScanUiEvent {
    data class ScanSuccessToResult(val type: Int,val content: String): ScanUiEvent
}