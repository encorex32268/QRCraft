package com.lihan.qrcraft.generate.presentation.create

sealed interface CreateUiEvent {
    data class NavigateToPreview(val id: Long): CreateUiEvent
}