package com.lihan.qrcraft.history.presentation

import com.lihan.qrcraft.core.presentation.model.QRCodeHistoryUi

data class ScanHistoryState(
    val scannedItems: List<QRCodeHistoryUi> = emptyList(),
    val generatedItems: List<QRCodeHistoryUi> = emptyList(),
    val isShowEditorBottomSheet: Boolean = false,
    val selectedId: Long?=null
)
