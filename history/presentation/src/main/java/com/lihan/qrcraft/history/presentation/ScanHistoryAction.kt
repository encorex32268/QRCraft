package com.lihan.qrcraft.history.presentation

sealed interface ScanHistoryAction {
    data class ItemLongClick(val id: Long?): ScanHistoryAction
    data class ItemClick(val id: Long?): ScanHistoryAction
    data object DeleteClick: ScanHistoryAction
    data object ShareClick: ScanHistoryAction
    data object DismissEditorBottomSheet: ScanHistoryAction
    data class ItemFavoriteClick(val id: Long, val isFavorite: Boolean): ScanHistoryAction
}