package com.lihan.qrcraft.history.presentation

sealed interface ScanHistoryAction {
    data class ItemLongClick(val id: Long?): ScanHistoryAction
    data object DeleteClick: ScanHistoryAction
    data object ShareClick: ScanHistoryAction
    data object DismissEditorBottomSheet: ScanHistoryAction
}