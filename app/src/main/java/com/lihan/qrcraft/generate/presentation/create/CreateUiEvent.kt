package com.lihan.qrcraft.generate.presentation.create

sealed interface CreateUiEvent {
    data class NavigateToPreview(
        val type: Int,
        val dataString: String
    ): CreateUiEvent
}